package com.main.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.group4.matchmingle.R;

public class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        Button loginGoogle = findViewById(R.id.login_goolge);
        Button loginFacebook = findViewById(R.id.login_facebook);
        Button loginPhone = findViewById(R.id.login_phone);
        TextView signupLogin = findViewById(R.id.sign_up_in_login);

        Drawable[] loginGoogleDrawables = loginGoogle.getCompoundDrawables();
        Drawable[] loginFacebookDrawables = loginFacebook.getCompoundDrawables();
        Drawable[] loginPhoneDrawables = loginPhone.getCompoundDrawables();
        if (loginGoogleDrawables[0] != null) {
            loginGoogleDrawables[0].setBounds(60, 0, 120, 60); // Set your desired width and height here
            loginGoogle.setCompoundDrawables(loginGoogleDrawables[0], null, null, null);
        }
        if (loginFacebookDrawables[0] != null) {
            loginFacebookDrawables[0].setBounds(60, 0, 120, 60); // Set your desired width and height here
            loginFacebook.setCompoundDrawables(loginFacebookDrawables[0], null, null, null);
        }
        if (loginPhoneDrawables[0] != null) {
            loginPhoneDrawables[0].setBounds(70, 0, 120, 60); // Set your desired width and height here
            loginPhone.setCompoundDrawables(loginPhoneDrawables[0], null, null, null);
        }

        loginGoogle.setOnClickListener(view -> {
            Intent intentHomePage = new Intent(SignInActivity.this, SwipeCardViewActivity.class);
            startActivity(intentHomePage);
            finish();
        });

        loginFacebook.setOnClickListener(view -> {
            Intent intentHomePage = new Intent(SignInActivity.this, SwipeCardViewActivity.class);
            startActivity(intentHomePage);
            finish();
        });

        loginPhone.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, VerifyPhoneNumberActivity.class);
            startActivity(intent);
            finish();
        });

        signupLogin.setOnClickListener(view -> {
            Intent intentSignUp = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intentSignUp);
        });
    }
}
