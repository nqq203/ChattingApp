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
import com.main.fragments.ColorPickerDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity  extends AppCompatActivity {
    private final List<MessageList> messageLists = new ArrayList<>();
    private UserSessionManager sessionManager;
    private String myPhone;
    private RecyclerView messagesRecyclerView;
    private String lastMessage = "";
    private int unseenMessage = 0;
    private MessageAdapter messageAdapter;
    private String chatKey = "";
    private boolean isFirstTime = true;
    private int countIsFirstTime = 0;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_activity);
        sessionManager = new UserSessionManager(getApplicationContext());

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
        List<Story> storiesList = new ArrayList<>();
        Story item1 = new Story("Quynh Nga", "https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg", "1234", 3000L);
        Story item2 = new Story("Quynh Ngan", "https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg", "1234", 4000L);
        Story item3 = new Story("Quynh Nguyen", "https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg", "1234", 5000L);

        storiesList.add(item1);
        storiesList.add(item2);
        storiesList.add(item3);

        container.setAdapter(new StoryAdapter(storiesList, this));
    }


    private void populateMessageList(RecyclerView container) {
        container.setHasFixedSize(true);
        container.setLayoutManager(new LinearLayoutManager(this));
        myPhone = sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);

        databaseReference.child("Message").child(myPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final String getMobilePhone = dataSnapshot.getKey();
                    messageLists.clear();
                    chatKey = "";
                    unseenMessage = 0;
                    lastMessage = "";
//                    dataSet = false;
                    isFirstTime = true;
                    if (!getMobilePhone.equals(myPhone)) {
                        final String fullname = dataSnapshot.child("fullname").getValue(String.class);
                        final String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                        DatabaseReference chatRef = databaseReference.child("Chat");
                        chatRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                                Log.d(TAG, "onDataChange: " + chatKey);
                                if (chatSnapshot.child(myPhone + "_" + getMobilePhone).hasChild("user1") &&
                                        chatSnapshot.child(myPhone + "_" + getMobilePhone).hasChild("user2") &&
                                        chatSnapshot.child(myPhone + "_" + getMobilePhone).hasChild("messages")) {
                                    chatKey = myPhone + "_" + getMobilePhone;
                                    for (DataSnapshot chatDataSnapshot : chatSnapshot.child(chatKey).child("messages").getChildren()) {
                                        final Long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsg(MessageActivity.this, chatKey));
                                        final Long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                        final String guestPhone = chatDataSnapshot.child("phoneNumber").getValue(String.class);

                                        String typeMsg = chatDataSnapshot.child("type").getValue(String.class);
                                        lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                        if (typeMsg.equals("image")) {
                                            lastMessage = "Have sent a image message";
                                        }
                                        else if(lastMessage.length() > 30 && typeMsg.equals("text")) {
                                            lastMessage = lastMessage.substring(0, 30) + "...";
                                        }
                                        else if (lastMessage.equals("audio")) {
                                            lastMessage = "Have sent a voice message";
                                        }
                                        if (getMessageKey > getLastSeenMessage && !myPhone.equals(guestPhone)) {
                                            unseenMessage = 1;
                                        }
                                        else {
                                            unseenMessage = 0;
                                        }
                                    }
                                }
                                else if (chatSnapshot.child(getMobilePhone + "_" + myPhone).hasChild("user1") &&
                                        chatSnapshot.child(getMobilePhone + "_" + myPhone).hasChild("user2") &&
                                        chatSnapshot.child(getMobilePhone + "_" + myPhone).hasChild("messages")) {
                                    chatKey = getMobilePhone + "_" + myPhone;
                                    for (DataSnapshot chatDataSnapshot : chatSnapshot.child(chatKey).child("messages").getChildren()) {
                                        final Long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsg(MessageActivity.this, chatKey));
                                        final Long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                        final String guestPhone = chatDataSnapshot.child("phoneNumber").getValue(String.class);

                                        String typeMsg = chatDataSnapshot.child("type").getValue(String.class);
                                        lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                        if (typeMsg.equals("image")) {
                                            lastMessage = "Have sent a image message";
                                        }
                                        else if(lastMessage.length() > 30 && typeMsg.equals("text")) {
                                            lastMessage = lastMessage.substring(0, 30) + "...";
                                        }
                                        else if (lastMessage.equals("audio")) {
                                            lastMessage = "Have sent a voice message";
                                        }
                                        if (getMessageKey > getLastSeenMessage && !myPhone.equals(guestPhone)) {
                                            unseenMessage = 1;
                                        }
                                        else {
                                            unseenMessage = 0;
                                        }
                                    }
                                }
                                else {
                                    chatKey = myPhone + "_" + getMobilePhone;
                                    unseenMessage = 0;
                                    lastMessage = "";
                                }

                                if (isFirstTime) {
                                    if (countIsFirstTime == (int)snapshot.getChildrenCount() - 1) {
                                        isFirstTime = false;
                                    }
                                    MessageList messageList = new MessageList(fullname, getMobilePhone, lastMessage, imageUrl, unseenMessage, chatKey);
                                    messageLists.add(messageList);
                                    if (countIsFirstTime <= (int)snapshot.getChildrenCount() - 1) {
                                        messageAdapter.updateData(messageLists);
                                        countIsFirstTime++;
                                    }
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
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(); // Quay về Fragment trước đó
        } else {
            super.onBackPressed(); // Hành vi mặc định (có thể là đóng Activity)
        }
    }
}
