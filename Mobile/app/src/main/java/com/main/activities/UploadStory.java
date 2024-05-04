package com.main.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.group4.matchmingle.R;
import com.main.entities.Story;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class UploadStory extends Activity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private String fullname, userId;
    ImageView imageView;
    Button btnChoosePicture;
    ImageButton backBtn;
    LinearLayout btnUpload;
    EditText durationEditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_story);

        imageView = findViewById(R.id.imageViewStory);
        btnChoosePicture = findViewById(R.id.btnChoosePictureStory);
        btnUpload = findViewById(R.id.btnUploadStory);
        backBtn = findViewById(R.id.backBtn);
        durationEditTxt = findViewById(R.id.durationEditTxt);
        btnChoosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String durationString = durationEditTxt.getText().toString();
                long duration = Long.parseLong(durationString);
                if (imageUri != null) {
                    uploadImageToStorage(imageUri, duration);
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadStory.this, MessageActivity.class);
                startActivity(intent);
            }
        });

        UserSessionManager sessionManager = new UserSessionManager(getBaseContext());
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        String phoneNumber = userDetails.get(UserSessionManager.KEY_PHONE_NUMBER);
        userId = phoneNumber; // Thay đổi thành ID của user1
        databaseReference.child("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fullname = dataSnapshot.child("fullname").getValue(String.class);
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
    }

    private void uploadImageToStorage(Uri imageUri, long duration) {
        // Tạo tham chiếu tới Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Tạo tên ngẫu nhiên cho ảnh
        String imageName = UUID.randomUUID().toString();
        // Thêm đuôi .jpg cho tên ảnh
        final StorageReference imageRef = storageRef.child("images/" + imageName + ".jpg");

        // Upload ảnh lên Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Lấy URL của ảnh đã upload
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Lưu thông tin về ảnh và duration vào Firebase Database
                        saveImageInfoToDatabase(uri.toString(), duration);
                    });
                })
                .addOnFailureListener(e -> {
                    // Xử lý trường hợp upload thất bại
                });
    }

    private void saveImageInfoToDatabase(String imageUrl, long duration) {
        // Tạo tham chiếu tới Firebase Database
        DatabaseReference userStoryRef = databaseReference.child("Story").child(userId);

        // Lấy ngày hiện tại
        Date currentDate = new Date(); // Tạo một đối tượng Date đại diện cho ngày hiện tại

        // Tạo đối tượng Story mới với các thông tin cần lưu
        Story story = new Story(duration, imageUrl, currentDate, fullname);

        // Lưu thông tin của story vào Firebase Database
        userStoryRef.push().setValue(story)
                .addOnSuccessListener(aVoid -> {
                    //Intent intent = new Intent(UploadStory.this, MessageActivity.class);
                    //startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi lưu thất bại
                    // Ví dụ: hiển thị thông báo lỗi
                });
    }



    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                imageUri = data.getData();
                imageView.setVisibility(View.VISIBLE);
                btnUpload.setVisibility(View.VISIBLE);
                btnChoosePicture.setVisibility(View.GONE);
                imageView.setImageURI(imageUri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
