package com.example.gochessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_screen); // Đảm bảo đây là file XML chứa nút PLAY

        Button btnExit = findViewById(R.id.btnExit);
        Button btnContinue = findViewById(R.id.btnContinue);

        btnExit.setOnClickListener(v -> {
            Intent intent = new Intent(SelectMenuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnContinue.setOnClickListener(v -> {
            finish();
        });

    }
}