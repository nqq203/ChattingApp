package com.main.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView textView = (TextView) findViewById(R.id.your_text_view);
//        textView.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));
////        textView.setText("\uf015");
        Intent signinIntent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(signinIntent);


        //        findViewById(R.id.navigate_message).setOnClickListener(view -> {
//            // Create an Intent to navigate to the message_activity
//            Intent intent = new Intent(MainActivity.this, MessageActivity.class);
//            startActivity(intent);
//        });
//        findViewById(R.id.navigate_signup).setOnClickListener(view -> {
//            // Create an Intent to navigate to the message_activity
//            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
//            startActivity(intent);
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}