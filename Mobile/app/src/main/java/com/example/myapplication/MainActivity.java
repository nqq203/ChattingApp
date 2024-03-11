package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ProfileAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);

        // Thiết lập GridLayoutManager với số cột tối đa là 3
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);

        // Khai báo một mảng các ID tài nguyên Drawable (đây là ví dụ, bạn có thể thay thế bằng dữ liệu thực tế của bạn)
        Integer[] imageIds = {R.drawable.avt3, R.drawable.story, R.mipmap.avtboy, R.drawable.story, R.drawable.story};

        mAdapter = new ProfileAdapter(this, imageIds);
        mRecyclerView.setAdapter(mAdapter);
    }
}
