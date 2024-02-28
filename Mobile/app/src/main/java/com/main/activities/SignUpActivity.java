package com.main.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.main.adapters.SignUpAdapter;
import com.main.entities.User;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextFullname, editTextGender, editTextBirthDate, editTextPhoneNumber;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editTextFullname = (EditText) findViewById(R.id.editTextFullname);
        editTextGender = (EditText) findViewById(R.id.editTextGender);
        editTextBirthDate = (EditText) findViewById(R.id.editTextPhoneNumber);
        buttonSignUp = (Button) findViewById(R.id.signUpButton);

        buttonSignUp.setOnClickListener(view -> {
            String fullname = editTextFullname.getText().toString();
            String gender = editTextGender.getText().toString();
            String birthDate = editTextBirthDate.getText().toString();
            String phoneNumer = editTextPhoneNumber.getText().toString();

            new SignUpAdapter().signupUser(fullname, gender, birthDate, phoneNumer, new SignUpAdapter.SignupCallback() {
                @Override
                public void onSuccess(User user) {
//                   handle navigate.
                }
                public void onError(String message) {
//                   show error message
                }
            });
        });
    }
}
