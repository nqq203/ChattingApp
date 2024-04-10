package com.main.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group4.matchmingle.R;
import com.main.adapters.ProfileAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class UploadProfileConfirm extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ProfileAdapter mAdapter;

    private ImageButton backBtn;
    private Button confirmBtn;
    private TextView txt;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_profile_confirm);
        txt = findViewById(R.id.txtstring);
        image = findViewById(R.id.image);
        mRecyclerView = findViewById(R.id.recyclerViewConfirm);
        backBtn = findViewById(R.id.closeBtn);
        confirmBtn = findViewById(R.id.confirmUploadBtn);

        backBtn.setOnClickListener(v -> onBackPressed());
        // Thiết lập GridLayoutManager với số cột tối đa là 3
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        /*
        // Khai báo một mảng các ID tài nguyên Drawable (đây là ví dụ, bạn có thể thay thế bằng dữ liệu thực tế của bạn)
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg"); // Thêm đường dẫn ảnh
        imageUrls.add("https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg");
        imageUrls.add("https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg");
        imageUrls.add( "https://th.bing.com/th/id/OIP.BwEaQx16dGDo7uPa-ARwDgHaHZ?w=189&h=188&c=7&r=0&o=5&dpr=1.3&pid=1.7");
        imageUrls.add( "https://th.bing.com/th/id/OIP.BwEaQx16dGDo7uPa-ARwDgHaHZ?w=189&h=188&c=7&r=0&o=5&dpr=1.3&pid=1.7");
        imageUrls.add( "https://th.bing.com/th/id/OIP.BwEaQx16dGDo7uPa-ARwDgHaHZ?w=189&h=188&c=7&r=0&o=5&dpr=1.3&pid=1.7");

        mAdapter = new ProfileAdapter(this, imageUrls);
        mRecyclerView.setAdapter(mAdapter);
        */
        ArrayList<String> newImageUrls = getIntent().getStringArrayListExtra("imageUris");

        // Kiểm tra xem danh sách có null không trước khi gán
        if (newImageUrls != null && !newImageUrls.isEmpty()) {
            mAdapter = new ProfileAdapter(this, newImageUrls);
            mRecyclerView.setAdapter(mAdapter);
            Log.e("UploadProfileConfirm", "không null");
        } else {
            Log.e("UploadProfileConfirm", "Danh sách đường dẫn ảnh là null!");
        }
    }
}
