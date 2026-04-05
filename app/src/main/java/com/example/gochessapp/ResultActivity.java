package com.example.gochessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Lấy dữ liệu từ Intent
        Intent intentData = getIntent();
        int blackScore = intentData.getIntExtra("BLACK_SCORE", 0);
        int whiteScore = intentData.getIntExtra("WHITE_SCORE", 0);
        int blackTime = intentData.getIntExtra("BLACK_TIME", 0);
        int whiteTime = intentData.getIntExtra("WHITE_TIME", 0);
        String currentMode = intentData.getStringExtra("GAME_MODE");

        // 2. Logic: Điểm cao thắng, Bằng điểm thì ai ít thời gian hơn thắng
        boolean isPlayerWin;
        if (blackScore > whiteScore) {
            isPlayerWin = true;
        } else if (whiteScore > blackScore) {
            isPlayerWin = false;
        } else {
            // BẰNG ĐIỂM -> So thời gian (nhỏ hơn là thắng)
            isPlayerWin = (blackTime <= whiteTime);
        }

        // 3. Thiết lập Layout dựa trên kết quả logic
        if (isPlayerWin) {
            setContentView(R.layout.activity_winer);
        } else {
            setContentView(R.layout.activity_loser);
        }

        // 4. Ánh xạ View (Phải làm sau setContentView)

        TextView tvScoreBot = findViewById(R.id.tvBotScore);
        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);
        Button btnBackMenu = findViewById(R.id.btnBackMenu);


        // 5. Xử lý nút Thử lại (Play Again)
        if (btnPlayAgain != null) {
            btnPlayAgain.setOnClickListener(v -> {
                Intent restartIntent = new Intent(ResultActivity.this, PlayGameActivity.class);
                // Truyền lại mode để ván mới biết là Easy hay Hard
                restartIntent.putExtra("GAME_MODE", currentMode);
                // Xóa stack cũ để tạo ván mới sạch sẽ
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(restartIntent);
                finish();
            });
        }

        // 6. Xử lý nút Menu
        if (btnBackMenu != null) {
            btnBackMenu.setOnClickListener(v -> {
                Intent menuIntent = new Intent(ResultActivity.this, SelectMenuActivity.class);
                menuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(menuIntent);
                finish();
            });
        }
    }
}