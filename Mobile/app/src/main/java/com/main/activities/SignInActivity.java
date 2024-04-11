package com.main.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;

public class SignInActivity extends AppCompatActivity {
    private EditText editPhoneNumber, editPassword;
    private Button btnSignIn;
    private TextView intentSignUp;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editPassword = findViewById(R.id.editPassword);
        btnSignIn = findViewById(R.id.signIn);
        intentSignUp = findViewById(R.id.sign_up_in_login);

        intentSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editPhoneNumber.getText().toString();
                String mPhoneNumber = "+84" + phoneNumber.substring(1);
                String password = editPassword.getText().toString();
                if (phoneNumber.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Required both phone number and password!", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(mPhoneNumber)) {
                                String getPassword = snapshot.child(mPhoneNumber).child("password").getValue(String.class);
                                if (getPassword.equals(password)) {
                                    Toast.makeText(SignInActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                                    Boolean isSetup = snapshot.child(mPhoneNumber).child("IsSetup").getValue(Boolean.class);
                                    if (isSetup) {
                                        Intent intent = new Intent(SignInActivity.this, SwipeCardViewActivity.class);
                                        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
                                        sessionManager.createUserLoginSession(mPhoneNumber);
                                        intent.putExtra("mPhoneNumber", mPhoneNumber);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Intent intent = new Intent(SignInActivity.this, SetUpAccountActivity.class);
                                        intent.putExtra("mPhoneNumber", mPhoneNumber);
                                        startActivity(intent);
                                    }
                                }
                                else {
                                    Toast.makeText(SignInActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(SignInActivity.this, "Phone number haven't register yet", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}
