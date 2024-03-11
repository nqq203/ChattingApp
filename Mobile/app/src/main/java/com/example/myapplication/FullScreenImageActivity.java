package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FullScreenImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        // Nhận ảnh từ Intent
        int imageResourceId = getIntent().getIntExtra("imageResourceId", 0);

        // Hiển thị ảnh trong ImageView
        ImageView imageView = findViewById(R.id.fullscreen_image_view);
        imageView.setImageResource(imageResourceId);
    }
}
