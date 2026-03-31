package com.example.gochessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đảm bảo đây là file XML chứa nút PLAY

        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnHelp = findViewById(R.id.btnHelp);

        // Khi ấn PLAY chuyển sang trang chọn Mode
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelectModeActivity.class);
            startActivity(intent);
        });

        // Khi ấn HELP chuyển sang trang hướng dẫn (nếu bạn đã tạo)
        btnHelp.setOnClickListener(v -> {
            Intent intentHelp = new Intent(MainActivity.this, HelpActivity.class);
             startActivity(intentHelp);
        });
    }
}