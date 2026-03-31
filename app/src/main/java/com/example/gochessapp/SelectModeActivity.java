package com.example.gochessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity; // Quan trọng: phải import cái này

// Thêm "extends AppCompatActivity" vào đây
public class SelectModeActivity extends AppCompatActivity {

    @Override // Thêm @Override để báo hiệu ghi đè hàm hệ thống
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Dòng này cực kỳ quan trọng, không có sẽ báo lỗi

        // Bây giờ bạn đã có thể set view bình thường
        setContentView(R.layout.activity_select_mode);


        // Sau dòng này bạn mới có thể ánh xạ ID (findViewById) các nút trong trang Select Mode
        Button btnEasy = findViewById(R.id.btnEasy);
        Button btnHard = findViewById(R.id.btnHard);



        btnEasy.setOnClickListener(v -> {
            Intent intent = new Intent(SelectModeActivity.this, PlayGameActivity.class);
            startActivity(intent);
        });


        btnHard.setOnClickListener(v -> {
            Intent intentHelp = new Intent(SelectModeActivity.this, PlayGameActivity.class);
            startActivity(intentHelp);
        });




    }
}