    package com.main.activities;

    import android.content.Intent;
    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.ImageButton;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.group4.matchmingle.R;
    import com.main.adapters.ProfileAdapter;

    import java.util.ArrayList;
    import java.util.List;


    public class ProfileView extends AppCompatActivity {
        private RecyclerView mRecyclerView;
        private ProfileAdapter mAdapter;

        private ImageButton backBtn;
        private Button postBtn;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.profile_view);

            mRecyclerView = findViewById(R.id.recyclerView);
            backBtn = findViewById(R.id.back_arrow);
            postBtn = findViewById(R.id.postProfileImgBtn);
            postBtn.setOnClickListener(v -> {
                // Tạo Intent để mở UploadImageType
                Intent intent = new Intent(ProfileView.this, UploadImageType.class);
                startActivity(intent);
            });
            backBtn.setOnClickListener(v -> onBackPressed());
            // Thiết lập GridLayoutManager với số cột tối đa là 3
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(layoutManager);

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
        }
    }
