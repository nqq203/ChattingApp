package com.main.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.MemoryData;
import com.main.adapters.ChatAdapter;
import com.main.entities.ChatList;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private final List<ChatList> chatLists = new ArrayList<>();
    private UserSessionManager sessionManager;
    private String chatKey;
    private String myPhone;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        sessionManager = new UserSessionManager(getApplicationContext());

        final ImageView backBtn = findViewById(R.id.back_chat);
        final TextView senderName = findViewById(R.id.user_chat_name);
        final EditText msgEditText = findViewById(R.id.edit_text_message);
        final TextView sendBtn = findViewById(R.id.button_send);
        final TextView sendImgBtn = findViewById(R.id.send_image);
        final TextView sendVoiceBtn = findViewById(R.id.send_voice);
        final ImageView profileImage = findViewById(R.id.user_chat_image);

        chatRecyclerView = findViewById(R.id.chat_container);

        // scroll to bottom when open keyboard
        final View activityRootView = findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(ChatActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // Scroll to bottom immediately
                    if (chatAdapter.getItemCount() > 0) {
                        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                    }
                }
            }
        });

        //get data from message adapter class
        final String senderFullname = getIntent().getStringExtra("fullname");
        final String senderProfilePic = getIntent().getStringExtra("imageUrl");
        chatKey = getIntent().getStringExtra("chatKey");
        final String senderMobile = getIntent().getStringExtra("mobile");

        //get my phone
        myPhone = sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);

        senderName.setText(senderFullname);
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(this).load(senderProfilePic).apply(requestOptions).into(profileImage);

        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        chatAdapter = new ChatAdapter(chatLists, ChatActivity.this);
        chatRecyclerView.setAdapter(chatAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (chatKey.isEmpty()) {
                    chatKey = "1";
                    if (snapshot.hasChild("Chat")) {
                        chatKey = String.valueOf(snapshot.child("Chat").getChildrenCount() + 1);
                    }
                }
                if (snapshot.hasChild("Chat")) {
                    if (snapshot.child("Chat").child(chatKey).hasChild("messages")) {
                        chatLists.clear();
                        for (DataSnapshot messagesSnapshot : snapshot.child("Chat").child(chatKey).child("messages").getChildren()) {
                            if (messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("phoneNumber")) {
                                final String messageTimestamps = messagesSnapshot.getKey();
                                final String getPhone = messagesSnapshot.child("phoneNumber").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);
                                final String getName = snapshot.child("Message").child(myPhone).child(getPhone).child("fullname").getValue(String.class);

                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                ChatList chatList = new ChatList(getPhone, getName, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                chatLists.add(chatList);

                                if (loadingFirstTime || Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastMsg(ChatActivity.this, chatKey))) {
                                    loadingFirstTime = false;
                                    MemoryData.saveLastMsg(messageTimestamps, chatKey, ChatActivity.this);

                                    chatAdapter.updateChatLists(chatLists);
                                    chatRecyclerView.scrollToPosition(chatLists.size() - 1);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getTextMsg = msgEditText.getText().toString();
                // get current timestamps
                final String currentTimestanp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                databaseReference.child("Chat").child(chatKey).child("user1").setValue(myPhone);
                databaseReference.child("Chat").child(chatKey).child("user2").setValue(senderMobile);
//                databaseReference.child("Chat").child(chatKey).child("unseenmessage").setValue(0);

                databaseReference.child("Chat").child(chatKey).child("messages").child(currentTimestanp).child("msg").setValue(getTextMsg);
                databaseReference.child("Chat").child(chatKey).child("messages").child(currentTimestanp).child("phoneNumber").setValue(myPhone);

                msgEditText.setText("");
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
}
