package com.example.gochessapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class PlayGameActivity extends AppCompatActivity {
    private GridLayout chessBoard;
    private ImageView[][] cells = new ImageView[8][8];
    private int[][] boardState = new int[8][8];
    private int currentTurn = HandleGamePlay.BLACK;
    private String gameMode;
    private HandleBot bot = new HandleBot();

    private int blackSec = 0, whiteSec = 0;
    private Handler handler = new Handler();
    private TextView tvBTime, tvWTime, tvBScore, tvWScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamescreen);
        gameMode = getIntent().getStringExtra("GAME_MODE");

        tvBTime = findViewById(R.id.tvPlayerTime);
        tvWTime = findViewById(R.id.tvBotTime);
        tvBScore = findViewById(R.id.tvPlayerScore);
        tvWScore = findViewById(R.id.tvBotScore);
        chessBoard = findViewById(R.id.gridBoard);

        initBoard();
        startTimer();
    }

    private void startTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentTurn == HandleGamePlay.BLACK) blackSec++;
                else whiteSec++;
                updateUI();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void initBoard() {
        chessBoard.post(() -> {
            int size = chessBoard.getWidth() / 8;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ImageView cell = new ImageView(this);
                    cell.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
                    cell.getLayoutParams().width = size; cell.getLayoutParams().height = size;
                    cell.setBackgroundResource(R.drawable.cell_background);
                    final int r = i, c = j;
                    cell.setOnClickListener(v -> onCellClick(r, c));
                    cells[i][j] = cell; boardState[i][j] = HandleGamePlay.EMPTY;
                    chessBoard.addView(cell);
                }
            }
            placePiece(3, 3, HandleGamePlay.WHITE); placePiece(4, 4, HandleGamePlay.WHITE);
            placePiece(3, 4, HandleGamePlay.BLACK); placePiece(4, 3, HandleGamePlay.BLACK);
        });
    }

    private void onCellClick(int r, int c) {
        if (currentTurn == HandleGamePlay.WHITE || !HandleGamePlay.isValidMove(r, c, currentTurn, boardState)) return;
        executeMove(r, c, HandleGamePlay.BLACK);

        if (hasMoves(HandleGamePlay.WHITE)) {
            currentTurn = HandleGamePlay.WHITE;
            makeBotMove();
        } else if (!hasMoves(HandleGamePlay.BLACK)) finishGame();
    }

    private void makeBotMove() {
        new Handler().postDelayed(() -> {
            int[] move = bot.getBestMove(boardState, gameMode);
            if (move[0] != -1) {
                executeMove(move[0], move[1], HandleGamePlay.WHITE);
                if (hasMoves(HandleGamePlay.BLACK)) currentTurn = HandleGamePlay.BLACK;
                else if (hasMoves(HandleGamePlay.WHITE)) makeBotMove();
                else finishGame();
            }
        }, 1000);
    }

    private void executeMove(int r, int c, int p) {
        boardState = HandleGamePlay.simulateMove(r, c, p, boardState);
        for(int i=0; i<8; i++) for(int j=0; j<8; j++) {
            if (boardState[i][j] == HandleGamePlay.BLACK) cells[i][j].setImageResource(R.drawable.stone_black);
            else if (boardState[i][j] == HandleGamePlay.WHITE) cells[i][j].setImageResource(R.drawable.stone_white);
        }
        updateUI();
    }

    private boolean hasMoves(int p) {
        for(int i=0; i<8; i++) for(int j=0; j<8; j++)
            if (HandleGamePlay.isValidMove(i, j, p, boardState)) return true;
        return false;
    }

    private void updateUI() {
        int b=0, w=0;
        for(int[] row : boardState) for(int cell : row) { if(cell==HandleGamePlay.BLACK) b++; else if(cell==HandleGamePlay.WHITE) w++; }
        tvBScore.setText(String.valueOf(b)); tvWScore.setText(String.valueOf(w));
        tvBTime.setText(String.format("%02d:%02d", blackSec/60, blackSec%60));
        tvWTime.setText(String.format("%02d:%02d", whiteSec/60, whiteSec%60));
    }

    private void placePiece(int r, int c, int type) {
        boardState[r][c] = type;
        cells[r][c].setImageResource(type == HandleGamePlay.BLACK ? R.drawable.stone_black : R.drawable.stone_white);
    }

    private void finishGame() {
        handler.removeCallbacksAndMessages(null);

        int blackScore = 0;
        int whiteScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardState[i][j] == HandleGamePlay.BLACK) blackScore++;
                else if (boardState[i][j] == HandleGamePlay.WHITE) whiteScore++;
            }
        }

        Intent intent = new Intent(PlayGameActivity.this, ResultActivity.class);
        // Gửi điểm số
        intent.putExtra("BLACK_SCORE", blackScore);
        intent.putExtra("WHITE_SCORE", whiteScore);
        // Gửi thời gian để xét hòa
        intent.putExtra("BLACK_TIME", blackSec);
        intent.putExtra("WHITE_TIME", whiteSec);
        // Gửi chế độ chơi để "Thử lại"
        intent.putExtra("GAME_MODE", gameMode);

        startActivity(intent);
        finish();
    }
}