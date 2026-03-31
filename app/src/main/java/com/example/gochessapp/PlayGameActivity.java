package com.example.gochessapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlayGameActivity extends AppCompatActivity {

    private GridLayout chessBoard;
    private ImageView[][] cells = new ImageView[8][8]; // Mảng quản lý các ô
    private int[][] boardState = new int[8][8]; // 0: trống, 1: đen, 2: trắng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamescreen);

        Button btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(v -> {
            Intent intentHelp = new Intent(PlayGameActivity.this, SelectMenuActivity.class);
            startActivity(intentHelp);
        });

        chessBoard = findViewById(R.id.gridBoard);
        initBoard();
    }

    private void initBoard() {
        int cellSize = getResources().getDisplayMetrics().widthPixels / 9; // Tính size ô cờ theo màn hình

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ImageView cell = new ImageView(this);

                // Thiết kế kích thước và margin cho từng ô
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = cellSize;
                params.height = cellSize;
                params.setMargins(1, 1, 1, 1); // Tạo đường kẻ ranh giới giữa các ô
                cell.setLayoutParams(params);

                cell.setBackgroundColor(Color.parseColor("#4CAF50")); // Màu nền ô cờ
                cell.setPadding(5, 5, 5, 5);
                cell.setScaleType(ImageView.ScaleType.FIT_CENTER);

                // Gán tọa độ để xử lý click sau này
                final int row = i;
                final int col = j;
                cell.setOnClickListener(v -> onCellClick(row, col));

                cells[i][j] = cell;
                chessBoard.addView(cell);
            }
        }

        // Đặt 4 quân cờ khởi đầu (Luật Othello)
        setupStartingPieces();
    }

    private void setupStartingPieces() {
        // Giả sử 1 là đen, 2 là trắng
        placePiece(3, 3, 2);
        placePiece(3, 4, 1);
        placePiece(4, 3, 1);
        placePiece(4, 4, 2);
    }

    private void placePiece(int row, int col, int type) {
        boardState[row][col] = type;
        if (type == 1) {
            cells[row][col].setImageResource(R.drawable.stone_black);
        } else if (type == 2) {
            cells[row][col].setImageResource(R.drawable.stone_white);
        }
    }

    private void onCellClick(int r, int c) {
        // Logic kiểm tra nước đi hợp lệ và lật quân sẽ viết ở đây
        Toast.makeText(this, "Bạn vừa nhấn ô: " + r + "," + c, Toast.LENGTH_SHORT).show();
    }


    // Hàm kiểm tra xem nước đi tại (r, c) của người chơi 'player' có hợp lệ không
    private boolean isValidMove(int r, int c, int player) {
        // Nếu ô đã có quân rồi thì không đặt được nữa
        if (boardState[r][c] != 0) return false;

        int opponent = (player == 1) ? 2 : 1;
        boolean canMove = false;

        // Quét 8 hướng xung quanh ô (r, c)
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            boolean hasOpponentBetween = false;

            // Đi tiếp theo hướng đó xem có quân đối thủ không
            while (nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && boardState[nr][nc] == opponent) {
                nr += dr[i];
                nc += dc[i];
                hasOpponentBetween = true;
            }

            // Nếu kết thúc chuỗi quân đối thủ là quân của mình thì nước đi hợp lệ
            if (hasOpponentBetween && nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && boardState[nr][nc] == player) {
                canMove = true;
                break;
            }
        }
        return canMove;
    }



}