package com.main.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import java.util.Calendar;
import java.util.HashMap;

public class AboutMeEdit extends AppCompatActivity {
    Button backbtn,savebtn;
    String userId;
    Context context;
    EditText  dob_View,email_View,location_View,height_View,gender_View, name_View,number_View;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_aboutme);
        context = this;
        savebtn=(Button) findViewById(R.id.save_button);

        UserSessionManager sessionManager = new UserSessionManager(getBaseContext());
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        userId=userDetails.get(UserSessionManager.KEY_PHONE_NUMBER);


        name_View=(EditText) findViewById(R.id.name_profile);
        gender_View=(EditText) findViewById(R.id.genderus_profile);
        number_View=(EditText) findViewById(R.id.phonenumber_profile);
        dob_View=(EditText)findViewById(R.id.datebirth_profile);
        email_View=(EditText) findViewById(R.id.email_profile);
        location_View=(EditText) findViewById(R.id.address_profile);
        height_View=(EditText) findViewById(R.id.height_profile);

        Button iconHome = findViewById(R.id.icon_home);
        Button iconFavourite = findViewById(R.id.icon_favorite);
        Button iconChat = findViewById(R.id.icon_chat);
        Button iconProfile = findViewById(R.id.icon_profile);
        ImageView back_arrow=findViewById(R.id.back_arrow);
        iconHome.setTextColor(getResources().getColor(R.color.black));
        iconFavourite.setTextColor(getResources().getColor(R.color.black));
        iconChat.setTextColor(getResources().getColor(R.color.black));
        iconProfile.setTextColor(getResources().getColor(R.color.purple_2));

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Information/"+userId);
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Information/"+userId);
        DatabaseReference databaseReference_User = firebaseDatabase.getReference("User/"+userId);

        iconChat.setOnClickListener(v -> {
            Intent intent = new Intent(AboutMeEdit.this, MessageActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        iconHome.setOnClickListener(v -> {
            Intent intent = new Intent(AboutMeEdit.this, SwipeCardViewActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        iconFavourite.setOnClickListener(v -> {
            Intent intent = new Intent(AboutMeEdit.this, MatchesActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        back_arrow.setOnClickListener(v -> {
            Intent intent = new Intent(AboutMeEdit.this, ProfileMainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        dob_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = name_View.getText().toString();
                String number = number_View.getText().toString();
                String email = email_View.getText().toString();
                String dob = dob_View.getText().toString();
                String location = location_View.getText().toString();
                String height = height_View.getText().toString();
                String gender = gender_View.getText().toString();
                databaseReference1.child("name").setValue(name);
                databaseReference1.child("number").setValue(number);
                databaseReference1.child("Email").setValue(email);
                databaseReference1.child("date of birth").setValue(dob);
                databaseReference1.child("location").setValue(location);
                databaseReference1.child("Height").setValue(height);
                databaseReference1.child("Gender").setValue(gender);

                databaseReference_User.child("fullname").setValue(name);
                databaseReference_User.child("date").setValue(dob);
                databaseReference_User.child("gender").setValue(gender);

            }
        });
        //databaseReference.child("Test");

        // Lấy dữ liệu từ nút "us1"

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);
                //System.out.println("Loading data: "+value);
                //String key = dataSnapshot.getKey();
                if (dataSnapshot.exists()) {
                    //MatchesItem matchesItem = snap.getValue(MatchesItem.class);
                    String gender = dataSnapshot.child("Gender").getValue(String.class);
                    String email = dataSnapshot.child("Email").getValue(String.class);
                    String height = dataSnapshot.child("Height").getValue(String.class);
                    String dob = dataSnapshot.child("date of birth").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String phoneNumber = dataSnapshot.child("number").getValue(String.class);

                    name_View.setText(name);
                    number_View.setText(phoneNumber);
                    location_View.setText(location);
                    dob_View.setText(dob);
                    height_View.setText(height);
                    email_View.setText(email);
                    gender_View.setText(gender);
                }
                else {
                    Log.d("UserData", "No data exists");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                System.out.println("Error: " + databaseError.getMessage());
            }
        });




    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Xử lý khi người dùng chọn ngày
                dob_View.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}