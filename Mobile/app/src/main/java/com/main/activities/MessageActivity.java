package com.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.main.adapters.MessageListAdapter;
import com.main.adapters.StoryAdapter;
import com.main.entities.MessageItem;
import com.main.entities.Story;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        RecyclerView storiesContainer = findViewById(R.id.stories_container);
        populateStories(storiesContainer);
        RecyclerView messagesListContainer = findViewById(R.id.message_list_container);
        populateMessageList(messagesListContainer);

        ImageView backPress = (ImageView) findViewById(R.id.back_message);
        backPress.setOnClickListener(view -> {
            // Create an Intent to navigate to the message_activity
            Intent intent = new Intent(MessageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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
        container.setLayoutManager(new LinearLayoutManager(this));
        List<MessageItem> messages = new ArrayList<>();
        MessageItem item1 = new MessageItem("1234", "Quynh Nga", "di dau v?", "/@mipmap/avt1", 10000);
        messages.add(item1);
        container.setAdapter(new MessageListAdapter(messages, this));
    }
}
