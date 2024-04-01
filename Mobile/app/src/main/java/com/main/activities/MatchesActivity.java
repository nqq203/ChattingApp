package com.main.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.GridView;
import android.widget.TextView;
import android.view.View;

import com.group4.matchmingle.R;
import com.main.adapters.MatchesAdapter;
import com.main.entities.MatchesItem;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matches_list);

        Button iconHome = findViewById(R.id.icon_home);
        Button iconFavourite = findViewById(R.id.icon_favorite);
        Button iconChat = findViewById(R.id.icon_chat);
        Button iconProfile = findViewById(R.id.icon_profile);

        iconHome.setTextColor(getResources().getColor(R.color.black));
        iconFavourite.setTextColor(getResources().getColor(R.color.purple_2));
        iconChat.setTextColor(getResources().getColor(R.color.black));
        iconProfile.setTextColor(getResources().getColor(R.color.black));

        iconChat.setOnClickListener(v -> {
            Intent intent = new Intent(MatchesActivity.this, MessageActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        iconProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MatchesActivity.this, ProfileMainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        iconHome.setOnClickListener(v -> {
            Intent intent = new Intent(MatchesActivity.this, SwipeCardViewActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        gridView=(GridView) findViewById(R.id.matchesGrid);
        ArrayList<MatchesItem> MatchesArrayList= new ArrayList<MatchesItem>();

        MatchesArrayList.add(new MatchesItem("Chi Sô",R.drawable.jisoo,21));
        MatchesArrayList.add(new MatchesItem("Hoa Hồng",R.drawable.rose,19));
        MatchesArrayList.add(new MatchesItem("Ren Ni",R.drawable.nini,20));
        MatchesArrayList.add(new MatchesItem("Li Xa",R.drawable.lisa,19));

        MatchesAdapter adapter= new MatchesAdapter(this,MatchesArrayList);
        gridView.setAdapter(adapter);
    }

}
