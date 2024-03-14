package com.main.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
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


        Button matchButton = findViewById(R.id.match_button);
        Button noMatchButton = findViewById(R.id.no_match_button);

        matchButton.setOnClickListener(v -> swipeCardFragment.simulateSwipeRight());
        noMatchButton.setOnClickListener(v -> swipeCardFragment.simulateSwipeLeft());
    }

}
