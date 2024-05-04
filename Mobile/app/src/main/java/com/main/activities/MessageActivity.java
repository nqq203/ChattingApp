package com.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;


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
import com.main.MemoryData;
import com.main.adapters.MessageAdapter;
import com.main.adapters.StoryAdapter;
import com.main.entities.MessageList;
import com.main.entities.Story;
import com.main.entities.User;
import com.main.fragments.ColorPickerDialogFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageActivity  extends AppCompatActivity implements ColorPickerDialogFragment.ColorPickerDialogListener{
    private final List<MessageList> messageLists = new ArrayList<>();
    private UserSessionManager sessionManager;
    private String myPhone;
    private RecyclerView messagesRecyclerView;
    private String lastMessage = "";
    private int unseenMessage = 0;
    private MessageAdapter messageAdapter;
    private String chatKey = "";
    private String myUserID;
    private boolean dataSet = false;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_activity);
        sessionManager = new UserSessionManager(getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        myUserID = userDetails.get(UserSessionManager.KEY_PHONE_NUMBER);

//      Handle Recycler View of stories and messages
        RecyclerView storiesContainer = findViewById(R.id.stories_container);
        populateStories(storiesContainer);
        messagesRecyclerView = findViewById(R.id.message_list_container);
        messageAdapter = new MessageAdapter(messageLists, MessageActivity.this);
        messagesRecyclerView.setAdapter(messageAdapter);
        populateMessageList(messagesRecyclerView);

//      Change the color of toolbar icon
        Button iconHome = findViewById(R.id.icon_home);
        Button iconFavourite = findViewById(R.id.icon_favorite);
        Button iconChat = findViewById(R.id.icon_chat);
        Button iconProfile = findViewById(R.id.icon_profile);

        iconHome.setTextColor(getResources().getColor(R.color.black));
        iconFavourite.setTextColor(getResources().getColor(R.color.black));
        iconChat.setTextColor(getResources().getColor(R.color.purple_2));
        iconProfile.setTextColor(getResources().getColor(R.color.black));

        Button backPress = findViewById(R.id.back_message);
        backPress.setOnClickListener(view -> {
            // Create an Intent to navigate to the message_activity
            Intent intent = new Intent(MessageActivity.this, SwipeCardViewActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        iconHome.setOnClickListener(v -> {
            Intent intent = new Intent(MessageActivity.this, SwipeCardViewActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        iconFavourite.setOnClickListener(v -> {
            Intent intent = new Intent(MessageActivity.this, MatchesActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        iconProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MessageActivity.this, ProfileMainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void populateStories(RecyclerView container) {
        container.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<User> usersList = new ArrayList<>();

        DatabaseReference myMatchesRef = FirebaseDatabase.getInstance().getReference("Matches").child(myUserID);
        myMatchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot matchSnapshot : dataSnapshot.getChildren()) {
                        String matchedUserID = matchSnapshot.getKey();
                        DatabaseReference theirMatchesRef = FirebaseDatabase.getInstance().getReference("Matches").child(matchedUserID);
                        theirMatchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot theirMatchSnapshot : dataSnapshot.getChildren()) {
                                        String theirMatchedUserID = theirMatchSnapshot.getKey();
                                        if (theirMatchedUserID.equals(myUserID)) {
                                            Log.d("abc", theirMatchedUserID);
                                            // Nếu người dùng đã match lại với bạn trong OneWayMatchesList của họ
                                            DatabaseReference userStoriesRef = FirebaseDatabase.getInstance().getReference("Story").child(matchedUserID);
                                            userStoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        boolean hasRecentStory = false; // Biến này để kiểm tra xem người dùng có story trong vòng 24h không
                                                        for (DataSnapshot storySnapshot : dataSnapshot.getChildren()) {
                                                            long duration = storySnapshot.child("duration").getValue(long.class);
                                                            String image = storySnapshot.child("imageUrl").getValue(String.class);
                                                            Date date = storySnapshot.child("timeCreated").getValue(Date.class);
                                                            String name = storySnapshot.child("fullname").getValue(String.class);
                                                            Story story = new Story(duration, image, date, name);
                                                            if (story != null){// && story.getTimeCreated().getTime() > System.currentTimeMillis() - (24 * 60 * 60 * 1000)) {
                                                                hasRecentStory = true;
                                                                Log.d("abc", String.valueOf(hasRecentStory));
                                                                break; // Nếu có story được tạo trong vòng 24 giờ, thoát vòng lặp
                                                            }
                                                        }
                                                        if (hasRecentStory) {
                                                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(matchedUserID);

                                                            User user = matchSnapshot.getValue(User.class);
                                                            user.setPhoneNumber(matchedUserID);
                                                            if (user != null) {
                                                                usersList.add(user);
                                                            }
                                                        }
                                                    }
                                                    // Đặt Adapter sau khi đã duyệt qua tất cả người dùng

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    // Xử lý lỗi
                                                }
                                            });
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý lỗi
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
        if(!usersList.isEmpty()) {
            container.setAdapter(new StoryAdapter(usersList, MessageActivity.this));
        }
        else{
            container.setAdapter(new StoryAdapter(new ArrayList<User>(), MessageActivity.this));
        }
    }








    private void populateMessageList(RecyclerView container) {
        container.setHasFixedSize(true);
        container.setLayoutManager(new LinearLayoutManager(this));
        myPhone = sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageLists.clear();
                lastMessage = "";
                unseenMessage = 0;
                chatKey = "";

                for (DataSnapshot dataSnapshot : snapshot.child("Message").child(myPhone).getChildren()) {
                    final String getMobilePhone = dataSnapshot.getKey();
                    dataSet = false;
                    if (!getMobilePhone.equals(myPhone)) {
                        final String fullname = dataSnapshot.child("fullname").getValue(String.class);
                        final String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                        databaseReference.child("Chat").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int chatCounts = (int)snapshot.getChildrenCount();
                                if (chatCounts > 0) {
                                    for (DataSnapshot dataSnapshot1: snapshot.getChildren()) {
                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;

                                        if (dataSnapshot1.hasChild("user1") && dataSnapshot1.hasChild("user2") && dataSnapshot1.hasChild("messages")) {
                                            final String getUserOne = dataSnapshot1.child("user1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user2").getValue(String.class);

                                            if ((getUserOne.equals(getMobilePhone) && getUserTwo.equals(myPhone)) || (getUserOne.equals(myPhone) && getUserTwo.equals(getMobilePhone))) {
                                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {
                                                    final Long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsg(MessageActivity.this, getKey));
                                                    final Long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final String getGuestPhone = chatDataSnapshot.child("phoneNumber").getValue(String.class);

                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if (lastMessage.length() > 30) {
                                                        lastMessage = lastMessage.substring(0, 30) + "...";
                                                    }
                                                    if (getMessageKey > getLastSeenMessage && !myPhone.equals(getGuestPhone)) {
                                                        unseenMessage = 1;
                                                    }
                                                    else {
                                                        unseenMessage = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (!dataSet) {
                                    dataSet  = true;
                                    MessageList messageList = new MessageList(fullname, getMobilePhone, lastMessage, imageUrl, unseenMessage, chatKey);
                                    messageLists.add(messageList);
                                    messageAdapter.updateData(messageLists);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onColorSelected(int color) {
        RelativeLayout chatLayout = findViewById(R.id.chat_layout);
        chatLayout.setBackgroundColor(color);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(); // Quay về Fragment trước đó
        } else {
            super.onBackPressed(); // Hành vi mặc định (có thể là đóng Activity)
        }
    }
}
