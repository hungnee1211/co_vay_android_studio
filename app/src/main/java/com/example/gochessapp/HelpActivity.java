package com.example.gochessapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    @Override // Thêm @Override để báo hiệu ghi đè hàm hệ thống
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Dòng này cực kỳ quan trọng, không có sẽ báo lỗi

        // Bây giờ bạn đã có thể set view bình thường
        setContentView(R.layout.activity_help);

        // Sau dòng này bạn mới có thể ánh xạ ID (findViewById) các nút trong trang Select Mode
    }

}
