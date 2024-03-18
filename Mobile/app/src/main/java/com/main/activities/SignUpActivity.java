package com.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.main.adapters.SignUpAdapter;
import com.main.entities.User;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        EditText editFullname = (EditText) findViewById(R.id.editFullname);
        EditText editGender = (EditText) findViewById(R.id.editGender);
        EditText editBirthDate = (EditText) findViewById(R.id.editDate);
        EditText editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        EditText editPassword = (EditText) findViewById(R.id.editPassword);
        Button buttonSignUp = (Button) findViewById(R.id.signUpButton);
        ImageView backSignUp = (ImageView) findViewById(R.id.back_signup);

        backSignUp.setOnClickListener(view -> {
            Intent intentSignIn = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intentSignIn);
            finish();
        });

        buttonSignUp.setOnClickListener(view -> {
            String fullname = editFullname.getText().toString();
            String gender = editGender.getText().toString();
            String birthDate = editBirthDate.getText().toString();
            String phoneNumber = editPhoneNumber.getText().toString();
            String password = editPassword.getText().toString();

            new SignUpAdapter().signupUser(fullname, gender, birthDate, phoneNumber, password, new SignUpAdapter.SignupCallback() {
                @Override
                public void onSuccess(User user) {
//                   handle navigate.
                    Intent intentSignin = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intentSignin);
                    finish();
                }
                public void onError(String message) {
//                   show error message
                }
            });
        });
    }
}
