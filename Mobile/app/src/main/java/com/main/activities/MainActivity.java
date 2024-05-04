package com.main.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.group4.matchmingle.R;
import com.main.MemoryData;
import com.main.entities.MessageList;
import com.main.entities.User;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        Log.d(TAG, "session: " + sessionManager.isLoggedIn());
        if (sessionManager.isLoggedIn()) {
            String phoneNumber = sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);
            if(getIntent().getExtras() != null){
                String UserID = getIntent().getExtras().getString("userID");
                databaseReference.child("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Tìm thấy User với userID tương ứng trong cơ sở dữ liệu
                            // Điều này cho phép bạn làm bất kỳ xử lý nào bạn muốn với thông tin của User này
                            // Ví dụ:
                            User user = new User();
                            user.setPhoneNumber(UserID);
                            user.setFullname(dataSnapshot.child("fullname").getValue(String.class));
                            user.setImageUrl(dataSnapshot.child("ImageUrl").getValue(String.class));
                            databaseReference.child("Chat").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int chatCounts = (int)snapshot.getChildrenCount();
                                    if (chatCounts > 0) {
                                        for (DataSnapshot dataSnapshot1: snapshot.getChildren()) {
                                            final String getKey = dataSnapshot1.getKey();
                                            String chatKey = getKey;

                                            if (dataSnapshot1.hasChild("user1") && dataSnapshot1.hasChild("user2") && dataSnapshot1.hasChild("messages")) {
                                                final String getUserOne = dataSnapshot1.child("user1").getValue(String.class);
                                                final String getUserTwo = dataSnapshot1.child("user2").getValue(String.class);

                                                if ((getUserOne.equals(user.getPhoneNumber()) && getUserTwo.equals(phoneNumber)) || (getUserOne.equals(phoneNumber) && getUserTwo.equals(user.getPhoneNumber()))) {
                                                    Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                                                    intent.putExtra("mobile", user.getPhoneNumber());
                                                    intent.putExtra("fullname", user.getFullname());
                                                    intent.putExtra("imageUrl", user.getImageUrl());
                                                    intent.putExtra("chatKey", dataSnapshot1.getKey());
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            if (user != null) {
                                // Bạn có thể chuyển người dùng đến màn hình nào đó hoặc thực hiện các hành động khác
                                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                                intent.putExtra("user", user.getPhoneNumber()); // Chuyển đối tượng User sang MessageActivity
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // Không tìm thấy User với userID tương ứng trong cơ sở dữ liệu
                            Log.d(TAG, "Không tìm thấy User với userID: " + UserID);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Xử lý lỗi nếu cần thiết
                        Log.e(TAG, "Lỗi khi truy vấn cơ sở dữ liệu: " + databaseError.getMessage());
                    }
                });
                Intent intent = new Intent(this, MessageActivity.class);
                startActivity(intent);
                finish();
            }
            Intent intent = new Intent(MainActivity.this, SwipeCardViewActivity.class);
            startActivity(intent);
        }
        else {
            Intent signinIntent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(signinIntent);
        }
        getFCMToken();

    }
    void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                Log.i("mytoken: ", token);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
