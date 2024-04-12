package com.main.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group4.matchmingle.R;
import com.main.adapters.ProfileAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class UploadProfileConfirm extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
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

        ArrayList<String> newImageUrls = getIntent().getStringArrayListExtra("imageUris");
        // Kiểm tra xem danh sách có null không trước khi gán
        if (newImageUrls != null && !newImageUrls.isEmpty()) {
            mAdapter = new ProfileAdapter(this, newImageUrls);
            mRecyclerView.setAdapter(mAdapter);
            Log.e("UploadProfileConfirm", "không null");
        } else {
            Log.e("UploadProfileConfirm", "Danh sách đường dẫn ảnh là null!");
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newImageUrls != null && !newImageUrls.isEmpty()) {
                    UserSessionManager sessionManager = new UserSessionManager(v.getContext());
                    HashMap<String, String> userDetails = sessionManager.getUserDetails();
                    String phoneNumber = userDetails.get(UserSessionManager.KEY_PHONE_NUMBER);
                    String userId = phoneNumber; // Thay đổi thành ID của user1
                    Log.e("id: ", userId);

                    // Kiểm tra mục profile của user có tồn tại hay không
                    databaseReference.child("Profiles").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                // Nếu mục profile của user không tồn tại, tạo mới
                                databaseReference.child("Profiles").child(userId).setValue(true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Nếu tạo mục profile thành công, tiến hành đẩy ảnh lên Storage
                                                pushImagesToStorage(userId, newImageUrls);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("UploadProfileConfirm", "Tạo profile thất bại: " + e.getMessage());
                                            }
                                        });
                            } else {
                                // Nếu mục profile của user đã tồn tại, tiếp tục kiểm tra mục userID
                                checkUserIdInProfile(userId, newImageUrls);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("UploadProfileConfirm", "onCancelled: " + error.getMessage());
                        }
                    });
                }
            }
        });
    }
    private void checkUserIdInProfile(String userId, ArrayList<String> imageUrls) {
        // Kiểm tra mục userID trong profile có tồn tại hay không
        databaseReference.child("Profiles").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Nếu mục userID không tồn tại, tạo mới và đẩy ảnh lên Storage
                    pushImagesToStorage(userId, imageUrls);
                } else {
                    // Nếu mục userID đã tồn tại, tiến hành đẩy ảnh lên Storage
                    pushImagesToStorage(userId, imageUrls);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UploadProfileConfirm", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void pushImagesToStorage(String userId, ArrayList<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            Uri uri = Uri.parse(imageUrl);
            String imageName = UUID.randomUUID().toString(); // Tạo tên ngẫu nhiên cho ảnh
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + imageName);

            storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Lấy URL của ảnh từ Firebase Storage
                            String imageUrl = downloadUri.toString();

                            // Đẩy URL vào mục userID của mục profiles trong database
                            databaseReference.child("Profiles").child(userId).push().setValue(imageUrl)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("UploadProfileConfirm", "Đẩy URL của ảnh vào database thành công");
                                            Intent intent = new Intent(UploadProfileConfirm.this, ProfileView.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("UploadProfileConfirm", "Đẩy URL của ảnh vào database thất bại: " + e.getMessage());
                                        }
                                    });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("UploadProfileConfirm", "Đẩy ảnh lên Firebase Storage thất bại: " + e.getMessage());
                }
            });
        }
    }
}
