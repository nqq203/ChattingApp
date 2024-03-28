package com.main.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class ProfileMainActivity  extends AppCompatActivity {

    TextView btnRateApp, btnComApp;
    Dialog RateAppDialog, ComAppDialog;
    ImageView backBtn;
    TextView editPlanBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_main);
        RateAppDialog = new Dialog(this);
        ComAppDialog = new Dialog(this);

        btnRateApp = (TextView) findViewById(R.id.rateapp_profile);
        btnComApp = (TextView) findViewById(R.id.next_improvement_profile);
        backBtn = (ImageView) findViewById(R.id.back_arrow);
        editPlanBtn = (TextView) findViewById(R.id.edit_plan_btn);

        Button iconHome = findViewById(R.id.icon_home);
        Button iconFavourite = findViewById(R.id.icon_favorite);
        Button iconChat = findViewById(R.id.icon_chat);
        Button iconProfile = findViewById(R.id.icon_profile);

        iconHome.setTextColor(getResources().getColor(R.color.black));
        iconFavourite.setTextColor(getResources().getColor(R.color.black));
        iconChat.setTextColor(getResources().getColor(R.color.black));
        iconProfile.setTextColor(getResources().getColor(R.color.purple_2));

        iconChat.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileMainActivity.this, MessageActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        iconHome.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileMainActivity.this, SwipeCardViewActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        iconFavourite.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileMainActivity.this, MatchesActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileMainActivity.this, SwipeCardViewActivity.class);
            startActivity(intent);
            finish();
        });

        editPlanBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileMainActivity.this, Subscription_Activity.class);
            startActivity(intent);
            finish();
        });
        btnRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateAppDialog.setContentView(R.layout.rating_app);
                RateAppDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                RateAppDialog.show();
            }
        });

        btnComApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComAppDialog.setContentView(R.layout.comments_improvement);
                ComAppDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                ComAppDialog.show();
            }
        });
    }


}