package com.example.gochessapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PlayGameActivity extends AppCompatActivity {

    private GridLayout chessBoard;
    private ImageView[][] cells = new ImageView[8][8];
    private int[][] boardState = new int[8][8];

    private static final int EMPTY = 0;
    private static final int BLACK = 1;
    private static final int WHITE = 2;
    private int currentTurn = BLACK;

    // Logic Thời gian cho cả 2 bên
    private int blackSeconds = 0;
    private int whiteSeconds = 0;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private TextView tvBlackTime, tvWhiteTime, tvPlayerScore, tvBotScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamescreen);

        // Ánh xạ View - Đảm bảo ID khớp với layout header của bạn
        chessBoard = findViewById(R.id.gridBoard);
        tvBlackTime = findViewById(R.id.tvPlayerTime); // Thời gian quân Đen
        tvWhiteTime = findViewById(R.id.tvBotTime);    // Thời gian quân Trắng
        tvPlayerScore = findViewById(R.id.tvPlayerScore);
        tvBotScore = findViewById(R.id.tvBotScore);
        Button btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(PlayGameActivity.this, SelectMenuActivity.class);
            startActivity(intent);
        });

        initBoard();
        startTimer();
    }

    private void startTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                // Kiểm tra lượt hiện tại để tăng giây cho bên đó
                if (currentTurn == BLACK) {
                    blackSeconds++;
                    updateTimerText(tvBlackTime, blackSeconds);
                } else if (currentTurn == WHITE) {
                    whiteSeconds++;
                    updateTimerText(tvWhiteTime, whiteSeconds);
                }
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    // Hàm bổ trợ để hiển thị định dạng mm:ss
    private void updateTimerText(TextView tv, int totalSeconds) {
        if (tv != null) {
            int mins = totalSeconds / 60;
            int secs = totalSeconds % 60;
            tv.setText(String.format("%02d:%02d", mins, secs));
        }
    }

    private void initBoard() {
        chessBoard.removeAllViews();
        chessBoard.post(() -> {
            int cellSize = chessBoard.getWidth() / 8;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ImageView cell = new ImageView(this);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = cellSize;
                    params.height = cellSize;
                    params.rowSpec = GridLayout.spec(i);
                    params.columnSpec = GridLayout.spec(j);
                    cell.setLayoutParams(params);

                    cell.setBackgroundResource(R.drawable.cell_background);
                    cell.setPadding(0, 0, 0, 0);
                    cell.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    final int r = i, c = j;
                    cell.setOnClickListener(v -> onCellClick(r, c));

                    cells[i][j] = cell;
                    boardState[i][j] = EMPTY;
                    chessBoard.addView(cell);
                }
            }
            setupStartingPieces();
            updateScore();
        });
    }

    private void setupStartingPieces() {
        placePiece(3, 3, WHITE);
        placePiece(4, 4, WHITE);
        placePiece(3, 4, BLACK);
        placePiece(4, 3, BLACK);
    }

    private void placePiece(int r, int c, int type) {
        boardState[r][c] = type;
        if (type == BLACK) cells[r][c].setImageResource(R.drawable.stone_black);
        else if (type == WHITE) cells[r][c].setImageResource(R.drawable.stone_white);
    }

    private void onCellClick(int r, int c) {
        if (!isValidMove(r, c, currentTurn)) {
            Toast.makeText(this, "Nước đi không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        placePiece(r, c, currentTurn);
        flipPieces(r, c, currentTurn);
        updateScore();

        int nextPlayer = (currentTurn == BLACK) ? WHITE : BLACK;

        // Khi đổi lượt, logic trong startTimer sẽ tự động nhận diện currentTurn mới
        if (hasAnyValidMove(nextPlayer)) {
            currentTurn = nextPlayer;
        } else if (!hasAnyValidMove(currentTurn)) {
            checkGameOver(true);
        } else {
            Toast.makeText(this, "Đối thủ mất lượt!", Toast.LENGTH_SHORT).show();
            // currentTurn giữ nguyên, đồng hồ bên này vẫn chạy tiếp
        }
    }

    private void flipPieces(int r, int c, int player) {
        int opponent = (player == BLACK) ? WHITE : BLACK;
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1}, dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int nr = r + dr[i], nc = c + dc[i];
            if (nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && boardState[nr][nc] == opponent) {
                int tr = nr, tc = nc;
                while (tr >= 0 && tr < 8 && tc >= 0 && tc < 8 && boardState[tr][tc] == opponent) {
                    tr += dr[i]; tc += dc[i];
                }
                if (tr >= 0 && tr < 8 && tc >= 0 && tc < 8 && boardState[tr][tc] == player) {
                    int fr = nr, fc = nc;
                    while (boardState[fr][fc] == opponent) {
                        placePiece(fr, fc, player);
                        fr += dr[i]; fc += dc[i];
                    }
                }
            }
        }
    }

    private boolean isValidMove(int r, int c, int p) {
        if (boardState[r][c] != EMPTY) return false;
        int opp = (p == BLACK) ? WHITE : BLACK;
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1}, dc = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < 8; i++) {
            int nr = r + dr[i], nc = c + dc[i];
            boolean hasOpp = false;
            while (nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && boardState[nr][nc] == opp) {
                nr += dr[i]; nc += dc[i]; hasOpp = true;
            }
            if (hasOpp && nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && boardState[nr][nc] == p) return true;
        }
        return false;
    }

    private boolean hasAnyValidMove(int p) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (isValidMove(i, j, p)) return true;
        return false;
    }

    private void updateScore() {
        int b = 0, w = 0, empty = 0;
        for (int[] row : boardState) {
            for (int cell : row) {
                if (cell == BLACK) b++; else if (cell == WHITE) w++; else empty++;
            }
        }
        if (tvPlayerScore != null) tvPlayerScore.setText(String.valueOf(b));
        if (tvBotScore != null) tvBotScore.setText(String.valueOf(w));

        if (empty == 0 || b == 0 || w == 0) checkGameOver(true);
    }

    private void checkGameOver(boolean forceEnd) {
        if (!forceEnd) return;
        timerHandler.removeCallbacks(timerRunnable);

        int b = 0, w = 0;
        for (int[] row : boardState) {
            for (int cell : row) {
                if (cell == BLACK) b++; else if (cell == WHITE) w++;
            }
        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("BLACK_SCORE", b);
        intent.putExtra("WHITE_SCORE", w);
        intent.putExtra("GAME_RESULT", (b > w) ? "WIN" : (w > b ? "LOSE" : "DRAW"));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
}