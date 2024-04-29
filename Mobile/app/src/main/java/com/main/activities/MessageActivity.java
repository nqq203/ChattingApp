package com.main.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.main.fragments.ColorPickerDialogFragment;

import java.util.ArrayList;
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
    private boolean dataSet = false;
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
        List<Story> storiesList = new ArrayList<>();
        Story item1 = new Story("Quynh Nga", "https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg", "1234");
        Story item2 = new Story("Quynh Ngan", "https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg", "1234");
        Story item3 = new Story("Quynh Nguyen", "https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg", "1234");

        storiesList.add(item1);
        storiesList.add(item2);
        storiesList.add(item3);

        container.setAdapter(new StoryAdapter(storiesList, this));
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
