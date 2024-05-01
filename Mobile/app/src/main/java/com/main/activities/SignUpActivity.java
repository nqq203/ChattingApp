package com.main.activities;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.Manifest;
import com.group4.matchmingle.R;
import com.main.adapters.SignUpAdapter;
import com.main.entities.User;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private EditText editFullname, editGender, editBirthDate, editPassword, editPhoneNumber, editConPassword;
    Button buttonSignUp;
    ImageView backSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        editFullname = (EditText) findViewById(R.id.editFullname);
        editGender = (EditText) findViewById(R.id.editGender);
        editBirthDate = (EditText) findViewById(R.id.editDate);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        editConPassword = (EditText) findViewById(R.id.editConPassword);
        buttonSignUp = (Button) findViewById(R.id.signUpButton);backSignUp = (ImageView) findViewById(R.id.back_signup);

        backSignUp.setOnClickListener(view -> {
            Intent intentSignIn = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intentSignIn);
            finish();
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = editFullname.getText().toString();
                String gender = editGender.getText().toString();
                String date = editBirthDate.getText().toString();
                String password = editPassword.getText().toString();
                String phoneNumber = editPhoneNumber.getText().toString();
                String mPhoneNumber = "+84" + phoneNumber.substring(1);
                String conPassword = editConPassword.getText().toString();
                if (fullname.isEmpty() || gender.isEmpty() || date.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || conPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Required all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!password.equals(conPassword)) {
                    Toast.makeText(SignUpActivity.this, "Password are not matching", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(mPhoneNumber)) {
                                Toast.makeText(SignUpActivity.this, "Phone number is already exist", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("User").child(mPhoneNumber).child("fullname").setValue(fullname);
                                databaseReference.child("User").child(mPhoneNumber).child("date").setValue(date);
                                databaseReference.child("User").child(mPhoneNumber).child("gender").setValue(gender);
                                databaseReference.child("User").child(mPhoneNumber).child("password").setValue(password);
                                databaseReference.child("User").child(mPhoneNumber).child("IsSetup").setValue(false);

                                // Initialize SuggestionList for new user
                                initializeSuggestionList(mPhoneNumber);

                                Toast.makeText(SignUpActivity.this, "User register successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, SetUpAccountActivity.class);
                                intent.putExtra("mPhoneNumber", mPhoneNumber);
                                startActivity(intent);
                                finish();
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

    private void initializeSuggestionList(String mPhoneNumber) {
        DatabaseReference suggestionRef = databaseReference.child("SuggestionList").child(mPhoneNumber);
        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()) {
                    if (user.getKey() != mPhoneNumber) {
                        String dbFullname = user.child("fullname").getValue(String.class);
                        String dbDate = user.child("date").getValue(String.class);
                        String dbGender = user.child("gender").getValue(String.class);
                        String dbImageUrl = user.child("imageUrl").getValue(String.class);

                        suggestionRef.child(user.getKey()).child("fullname").setValue(dbFullname);
                        suggestionRef.child(user.getKey()).child("date").setValue(dbDate);
                        suggestionRef.child(user.getKey()).child("gender").setValue(dbGender);
                        suggestionRef.child(user.getKey()).child("imageUrl").setValue(dbImageUrl);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
