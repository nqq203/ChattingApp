package com.main.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


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

public class MessageActivity  extends AppCompatActivity {
    private final List<MessageList> messageLists = new ArrayList<>();
    private UserSessionManager sessionManager;
    private String myPhone;
    private RecyclerView messagesRecyclerView;
    private String lastMessage = "";
    private MessageAdapter messageAdapter;
    private String chatKey = "";
    private String myUserID;
    private boolean isFirstTime = true;
    private boolean dataSet = false;
    private int countIsFirstTime = 0;
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
        messageAdapter = new MessageAdapter(messageLists, MessageActivity.this, new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MessageList message) {
                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                intent.putExtra("mobile", message.getMobile());
                intent.putExtra("fullname", message.getName());
                intent.putExtra("imageUrl", message.getImageUrl());
                intent.putExtra("chatKey", message.getChatKey());
                startActivity(intent);
                finish();
            }
        });
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

        DatabaseReference myMatchesRef = databaseReference.child("Message").child(myUserID);
        myMatchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot matchSnapshot : dataSnapshot.getChildren()) {
                        String matchedUserID = matchSnapshot.getKey();
                        DatabaseReference theirMatchesRef = databaseReference.child("Message").child(matchedUserID);
                        theirMatchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot theirMatchSnapshot : dataSnapshot.getChildren()) {
                                        String theirMatchedUserID = theirMatchSnapshot.getKey();
                                        if (theirMatchedUserID.equals(myUserID)) {
                                            DatabaseReference userStoriesRef = databaseReference.child("Story").child(matchedUserID);
                                            userStoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        boolean hasRecentStory = false;
                                                        for (DataSnapshot storySnapshot : dataSnapshot.getChildren()) {
                                                            long duration = storySnapshot.child("duration").getValue(long.class);
                                                            String image = storySnapshot.child("imageUrl").getValue(String.class);
                                                            Date date = storySnapshot.child("timeCreated").getValue(Date.class);
                                                            String name = storySnapshot.child("fullname").getValue(String.class);
                                                            Story story = new Story(duration, image, date, name);
                                                            if (story != null){
                                                                hasRecentStory = true;
                                                                break;
                                                            }
                                                        }
                                                        if (hasRecentStory) {
                                                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(matchedUserID);
                                                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()) {
                                                                            // Cập nhật thông tin cho user

                                                                        String PhoneNumber = matchedUserID;
                                                                        String fullname = dataSnapshot.child("fullname").getValue(String.class);
                                                                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                                                                        User user = new User(matchedUserID, fullname, imageUrl);
                                                                        usersList.add(user);

                                                                        // Đặt Adapter sau khi đã duyệt qua tất cả người dùng
                                                                        if (!usersList.isEmpty()) {
                                                                            container.setAdapter(new StoryAdapter(usersList, MessageActivity.this));
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
                                                    else {
                                                        container.setAdapter(new StoryAdapter(new ArrayList<User>(), MessageActivity.this));
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
                                else {
                                    container.setAdapter(new StoryAdapter(new ArrayList<User>(), MessageActivity.this));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý lỗi
                            }
                        });
                    }
                }
                else {
                    container.setAdapter(new StoryAdapter(new ArrayList<User>(), MessageActivity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }








    private void populateMessageList(RecyclerView container) {
        container.setHasFixedSize(true);
        container.setLayoutManager(new LinearLayoutManager(this));
        myPhone = sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);

        DatabaseReference chatRef = databaseReference.child("Chat");
        databaseReference.child("Message").child(myPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageLists.clear();  // Clear once at the start of the update
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final String otherPhone = dataSnapshot.getKey();
                    if (!otherPhone.equals(myPhone)) {
                        final String fullname = dataSnapshot.child("fullname").getValue(String.class);
                        final String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                                String compositeKey = myPhone + "_" + otherPhone;
                                if (!chatSnapshot.hasChild(compositeKey)) {
                                    compositeKey = otherPhone + "_" + myPhone;
                                }

                                if (chatSnapshot.child(compositeKey).hasChild("messages")) {
                                    DataSnapshot lastMessageSnapshot = getLastMessageSnapshot(chatSnapshot.child(compositeKey).child("messages"));
                                    int mySeen = chatSnapshot.child(compositeKey).child(myPhone+"_seen").getValue(Integer.class);
                                    processLastMessage(lastMessageSnapshot, fullname, otherPhone, imageUrl, compositeKey, mySeen);
                                } else {
                                    addMessageToList(fullname, otherPhone, "No messages", imageUrl, 0, compositeKey);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private DataSnapshot getLastMessageSnapshot(DataSnapshot messagesSnapshot) {
        DataSnapshot lastMessageSnapshot = null;
        for (DataSnapshot child : messagesSnapshot.getChildren()) {
            lastMessageSnapshot = child;
        }
        return lastMessageSnapshot;
    }

    private void processLastMessage(DataSnapshot dataSnapshot, String fullname, String mobile, String imageUrl, String chatKey, int mySeen) {
        String lastMessage = dataSnapshot.child("msg").getValue(String.class);
        String messageType = dataSnapshot.child("type").getValue(String.class);

        if ("image".equals(messageType)) {
            lastMessage = "Image message";
        } else if ("audio".equals(messageType)) {
            lastMessage = "Audio message";
        } else if (lastMessage != null && lastMessage.length() > 30) {
            lastMessage = lastMessage.substring(0, 30) + "...";
        }

//        long lastMessageTime = Long.parseLong(dataSnapshot.getKey());
//        long lastSeenTime = Long.parseLong(MemoryData.getLastMsg(MessageActivity.this, chatKey));

//        if (lastMessageTime > lastSeenTime && !myPhone.equals(mobile)) {
//            unseenCount = 0;
//        }
//        int unseenCnt = Integer.parseInt(mySeen);
        addMessageToList(fullname, mobile, lastMessage, imageUrl, mySeen, chatKey);
    }

    private void addMessageToList(String fullname, String mobile, String lastMessage, String imageUrl, int unseenCount, String chatKey) {
        MessageList message = new MessageList(fullname, mobile, lastMessage, imageUrl, unseenCount, chatKey);
        messageLists.add(message);
        messageAdapter.updateData(messageLists);
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
