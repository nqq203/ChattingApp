package com.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.main.adapters.MessageListAdapter;
import com.main.adapters.StoryAdapter;
import com.main.entities.MessageItem;
import com.main.entities.Story;
import com.main.fragments.ChatFragment;
import com.main.fragments.ColorPickerDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity  extends AppCompatActivity implements ColorPickerDialogFragment.ColorPickerDialogListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_activity);


//      Handle Recycler View of stories and messages
        RecyclerView storiesContainer = findViewById(R.id.stories_container);
        populateStories(storiesContainer);
        RecyclerView messagesListContainer = findViewById(R.id.message_list_container);
        populateMessageList(messagesListContainer);

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
        container.setLayoutManager(new LinearLayoutManager(this));
        List<MessageItem> messages = new ArrayList<>();
        MessageItem item1 = new MessageItem("1234", "Quynh Nga", "di dau v?", "https://vcdn-giaitri.vnecdn.net/2024/02/14/snapinsta-app-422549648-362365-4477-9392-1707881492.jpg", 10000);

        messages.add(item1);
        messages.add(item1);
        messages.add(item1);
        messages.add(item1);
        messages.add(item1);
        messages.add(item1);
        messages.add(item1);
        messages.add(item1);
        messages.add(item1);
        messages.add(item1);

//      Create new instance
        MessageListAdapter adapter = new MessageListAdapter(messages, this);

        container.setAdapter(adapter);
        adapter.setOnItemClickListener(position ->  {
            ChatFragment chatFragment = new ChatFragment();

//          Pass data
            Bundle args = new Bundle();
            args.putInt("selected_position", position);
            chatFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.message_container, chatFragment, "CHAT_FRAGMENT_TAG")
                    .addToBackStack(null)
                    .commit();
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
