package com.main.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class ProfileMainActivity  extends AppCompatActivity {

    TextView btnRateApp, btnComApp;
    Dialog RateAppDialog, ComAppDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_main);
        RateAppDialog = new Dialog(this);
        ComAppDialog = new Dialog(this);

        btnRateApp = (TextView) findViewById(R.id.rateapp_profile);
        btnComApp = (TextView) findViewById(R.id.next_improvement_profile);

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