package com.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
//import com.main.callbacks.FilterCallback;
import com.main.fragments.SwipeCardFragment;

public class SwipeCardViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeCardFragment swipeCardFragment = new SwipeCardFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.swipe_card_container, swipeCardFragment)
                .commit();
        setContentView(R.layout.swipe_cardview_activity);

        Button iconHome = findViewById(R.id.icon_home);
        Button iconFavourite = findViewById(R.id.icon_favorite);
        Button iconChat = findViewById(R.id.icon_chat);
        Button iconProfile = findViewById(R.id.icon_profile);

        iconHome.setTextColor(getResources().getColor(R.color.purple_2));
        iconFavourite.setTextColor(getResources().getColor(R.color.black));
        iconChat.setTextColor(getResources().getColor(R.color.black));
        iconProfile.setTextColor(getResources().getColor(R.color.black));

        Button matchButton = findViewById(R.id.match_button);
        Button noMatchButton = findViewById(R.id.no_match_button);

        matchButton.setOnClickListener(v -> swipeCardFragment.simulateSwipeRight());
        noMatchButton.setOnClickListener(v -> swipeCardFragment.simulateSwipeLeft());

        iconChat.setOnClickListener(v -> {
            Intent intent = new Intent(SwipeCardViewActivity.this, MessageActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        iconProfile.setOnClickListener(v -> {
            Intent intent = new Intent(SwipeCardViewActivity.this, ProfileMainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

    }
}
