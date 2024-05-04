package com.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.adapters.BlockedUsersAdapter;
import com.main.entities.BlockedUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockedUsersActivity extends AppCompatActivity implements BlockedUsersAdapter.OnUnblockClickListener{

    private RecyclerView recyclerView;
    private BlockedUsersAdapter adapter;
    private List<BlockedUser> blockedUsers = new ArrayList<>();;
    private ImageView backBtn;
    private UserSessionManager sessionManager;
    private String myPhone;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);

        backBtn = findViewById(R.id.back_block);
        recyclerView = findViewById(R.id.recyclerViewBlockedUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sessionManager = new UserSessionManager(getApplicationContext());
        myPhone = sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(BlockedUsersActivity.this, ProfileMainActivity.class);
            startActivity(intent);
            finish();
        });
        databaseReference.child("BlockList").child(myPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                blockedUsers.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String otherPhone = userSnapshot.getKey();
                    String otherName = userSnapshot.getValue(String.class);
                    BlockedUser user = new BlockedUser(otherPhone, otherName);
                    blockedUsers.add(user);
                }
                adapter = new BlockedUsersAdapter(blockedUsers, u -> {
                    onUnblockClick(u);
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BlockedUsersActivity", "Error loading blocked users", error.toException());
            }
        });
    }

    @Override
    public void onUnblockClick(BlockedUser user) {
        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String date = dataSnapshot.child(user.getUserId()).child("date").getValue(String.class);
                String imageUrl = dataSnapshot.child(user.getUserId()).child("imageUrl").getValue(String.class);
                String gender = dataSnapshot.child(user.getUserId()).child("gender").getValue(String.class);
                HashMap<String, Object> newUser1 = new HashMap<>();
                newUser1.put("date", date);
                newUser1.put("imageUrl", imageUrl);
                newUser1.put("gender", gender);
                newUser1.put("fullname", user.getName());
                String date2 = dataSnapshot.child(myPhone).child("date").getValue(String.class);
                String imageUrl2 = dataSnapshot.child(myPhone).child("imageUrl").getValue(String.class);
                String gender2 = dataSnapshot.child(myPhone).child("gender").getValue(String.class);
                String fullname2 = dataSnapshot.child(myPhone).child("fullname").getValue(String.class);
                HashMap<String, Object> newUser2 = new HashMap<>();
                newUser2.put("date", date2);
                newUser2.put("imageUrl", imageUrl2);
                newUser2.put("gender", gender2);
                newUser2.put("fullname", fullname2);
                databaseReference.child("SuggestionList").child(myPhone).child(user.getUserId()).setValue(newUser1);
                databaseReference.child("SuggestionList").child(user.getUserId()).child(myPhone).setValue(newUser2);
                databaseReference.child("BlockList").child(myPhone).child(user.getUserId()).removeValue();
                Intent intent = new Intent(BlockedUsersActivity.this, SwipeCardViewActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
