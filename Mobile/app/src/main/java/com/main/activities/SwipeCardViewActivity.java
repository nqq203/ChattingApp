package com.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
//import com.main.callbacks.FilterCallback;
import com.main.fragments.SwipeCardFragment;

public class SwipeCardViewActivity extends AppCompatActivity {
//    FilterCallback filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeCardFragment swipeCardFragment = new SwipeCardFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.swipe_card_container, swipeCardFragment)
                .commit();
        setContentView(R.layout.swipe_cardview_activity);

        Button matchButton = findViewById(R.id.match_button);
        Button noMatchButton = findViewById(R.id.no_match_button);
        ImageView filterIcon = findViewById(R.id.icon_filter);
        TextView iconHome = findViewById(R.id.icon_home);
        TextView iconFavourite = findViewById(R.id.icon_favorite);
        TextView iconChat = findViewById(R.id.icon_chat);
        TextView iconProfile = findViewById(R.id.icon_profile);

        iconHome.setTextColor(getResources().getColor(R.color.purple_2));

        matchButton.setOnClickListener(v -> swipeCardFragment.simulateSwipeRight());
        noMatchButton.setOnClickListener(v -> swipeCardFragment.simulateSwipeLeft());
//        filterIcon.setOnClickListener(view -> {
//            Intent intent = new Intent(SwipeCardViewActivity.this, FilterActivity.class);
//            startActivity(intent);
//            filter.blurBackground();
//        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        filter.finishFilterLayout();
//        return true;
//    }
}
