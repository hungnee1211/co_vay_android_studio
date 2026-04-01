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

        // 1. Lấy dữ liệu từ Intent gửi sang
        String result = getIntent().getStringExtra("GAME_RESULT");
        int blackScore = getIntent().getIntExtra("BLACK_SCORE", 0);
        int whiteScore = getIntent().getIntExtra("WHITE_SCORE", 0);

        // 2. Kiểm tra kết quả để hiển thị Giao diện tương ứng
        if ("WIN".equals(result)) {
            setContentView(R.layout.activity_winer); // Hiện trang thắng
        } else if ("LOSE".equals(result)) {
            setContentView(R.layout.activity_loser); // Hiện trang thua
        } else {
            // Nếu hòa, Hưng có thể dùng trang winner nhưng sửa chữ thành "Hòa cờ"
            setContentView(R.layout.activity_winer);
        }

        // 3. Ánh xạ các nút bấm (Vì cả 2 layout đều có nút PlayAgain và Menu)
        // Hưng hãy đảm bảo ID các nút ở 2 file XML giống hệt nhau để code không bị lỗi
        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);
        Button btnBackMenu = findViewById(R.id.btnBackMenu);

        // Xử lý nút Chơi lại
        if (btnPlayAgain != null) {
            btnPlayAgain.setOnClickListener(v -> {
                Intent intent = new Intent(ResultActivity.this, PlayGameActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Xử lý nút Về Menu chính
        if (btnBackMenu != null) {
            btnBackMenu.setOnClickListener(v -> {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
}