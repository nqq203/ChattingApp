    package com.main.activities;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.Button;
    import android.widget.ImageButton;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.group4.matchmingle.R;
    import com.main.adapters.ProfileAdapter;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;

    public class ProfileView extends AppCompatActivity {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        private RecyclerView mRecyclerView;
        private ProfileAdapter mAdapter;
        private TextView ProfileName;
        private ImageButton backBtn;
        private Button postBtn;
        List<String> imageUrls = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.profile_view);
            ProfileName = findViewById(R.id.ProfileName);
            mRecyclerView = findViewById(R.id.recyclerView);
            backBtn = findViewById(R.id.back_arrow);
            postBtn = findViewById(R.id.postProfileImgBtn);

            backBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileView.this, ProfileMainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });

            // Thiết lập GridLayoutManager với số cột tối đa là 3
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(layoutManager);

            UserSessionManager sessionManager = new UserSessionManager(getBaseContext());
            HashMap<String, String> userDetails = sessionManager.getUserDetails();
            String phoneNumber = userDetails.get(UserSessionManager.KEY_PHONE_NUMBER);
            String userId = phoneNumber; // Thay đổi thành ID của user1

            databaseReference.child("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Lấy thông tin full name từ dataSnapshot
                        String fullName = dataSnapshot.child("fullname").getValue(String.class);
                        // Gán full name vào TextView ProfileName
                        ProfileName.setText(fullName);
                    } else {
                        Log.d("ProfileView", "Không tìm thấy dữ liệu trong mục user");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    Log.e("ProfileView", "Đã xảy ra lỗi khi đọc dữ liệu từ Firebase", databaseError.toException());
                }
            });
            // Khởi tạo Adapter trước khi lắng nghe sự kiện
            mAdapter = new ProfileAdapter(this, imageUrls);
            mRecyclerView.setAdapter(mAdapter);

            // Lắng nghe sự thay đổi dữ liệu trên Firebase
            databaseReference.child("Profiles").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Xóa danh sách ảnh cũ để cập nhật lại
                        imageUrls.clear();

                        // Lặp qua các ảnh trong dataSnapshot và thêm chúng vào imageUrls
                        for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                            String imageUrl = imageSnapshot.getValue(String.class);
                            imageUrls.add(imageUrl);
                        }

                        // Thông báo cho Adapter rằng dữ liệu đã thay đổi
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("ProfileView", "Không tìm thấy dữ liệu trong profile.user");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    Log.e("ProfileView", "Đã xảy ra lỗi khi đọc dữ liệu từ Firebase", databaseError.toException());
                }
            });

            postBtn.setOnClickListener(v -> {
                // Tạo Intent để mở UploadImageType
                Intent intent = new Intent(ProfileView.this, UploadImageType.class);
                startActivity(intent);
            });
        }
    }
