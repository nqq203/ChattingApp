package com.main.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.main.adapters.MatchesAdapter;
import com.main.entities.MatchesItem;

import org.w3c.dom.Text;

public class ProfileMainActivity  extends AppCompatActivity {

    TextView btnRateApp, btnComApp,btnAboutMe,btnAboutMe2,btn_pre;
    Button ViewProfileBtn;
    Dialog RateAppDialog, ComAppDialog;
    ImageView backBtn;
    TextView editPlanBtn, btnLogout;
    UserSessionManager sessionManager;
    TextView phone_View,
            dob_View,email_View,location_View,height_View,curdatingplan_View,gender_View,
            feedback_View, heightrhange_View,name_View,number_View,agerange_View,genderpre_View,
            comment_View,language_View,rate_View;

    String userId="us1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_main);
        RateAppDialog = new Dialog(this);
        ComAppDialog = new Dialog(this);
        sessionManager = new UserSessionManager(this);

        btn_pre=(TextView) findViewById(R.id.btn_pre);
        btnRateApp = (TextView) findViewById(R.id.rateapp_profile);
        btnComApp = (TextView) findViewById(R.id.next_improvement_profile);
        backBtn = (ImageView) findViewById(R.id.back_arrow);
        editPlanBtn = (TextView) findViewById(R.id.edit_plan_btn);
        btnLogout = (TextView) findViewById(R.id.btnLogout);
        btnAboutMe=(TextView) findViewById(R.id.btn_edit_aboutme);
        btnAboutMe2=(TextView) findViewById(R.id.btn_aboutme2);

        genderpre_View=(TextView)findViewById(R.id.gender_profile);
        name_View=(TextView) findViewById(R.id.name_profile);
        gender_View=(TextView) findViewById(R.id.genderus_profile);
        phone_View=(TextView) findViewById(R.id.phonenumber_profile);
        dob_View=(TextView) findViewById(R.id.datebirth_profile);
        email_View=(TextView) findViewById(R.id.email_profile);
        location_View=(TextView) findViewById(R.id.address_profile);
        height_View=(TextView) findViewById(R.id.height_profile);
        curdatingplan_View=(TextView) findViewById(R.id.DatingPlan_Profile);
        genderpre_View=(TextView) findViewById(R.id.gender_profile);
        agerange_View=(TextView) findViewById(R.id.agerange_profile);
        heightrhange_View=(TextView) findViewById(R.id.PreHeight_profile);
        language_View=(TextView) findViewById(R.id.language_profile);

        ViewProfileBtn = (Button) findViewById(R.id.ViewProfileBtn);


        Button iconHome = findViewById(R.id.icon_home);
        Button iconFavourite = findViewById(R.id.icon_favorite);
        Button iconChat = findViewById(R.id.icon_chat);
        Button iconProfile = findViewById(R.id.icon_profile);

        iconHome.setTextColor(getResources().getColor(R.color.black));
        iconFavourite.setTextColor(getResources().getColor(R.color.black));
        iconChat.setTextColor(getResources().getColor(R.color.black));
        iconProfile.setTextColor(getResources().getColor(R.color.purple_2));

        ViewProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileMainActivity.this, ProfileView.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

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
        btn_pre.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileMainActivity.this, PreferencesEdit.class);
            startActivity(intent);
            finish();
        });
        btnAboutMe.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileMainActivity.this, AboutMeEdit.class);
            startActivity(intent);
            finish();
        });
        btnAboutMe2.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileMainActivity.this, AboutMeEdit.class);
            startActivity(intent);
            finish();
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

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Information/"+userId);
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
                    String ageRange = dataSnapshot.child("Age range").getValue(String.class);
                    String comment = dataSnapshot.child("Comment").getValue(String.class);
                    Integer currentDatingPlan = dataSnapshot.child("Current Dating Plan").getValue(Integer.class);
                    String email = dataSnapshot.child("Email").getValue(String.class);
                    String feedback = dataSnapshot.child("Feedback").getValue(String.class);
                    String height = dataSnapshot.child("Height").getValue(String.class);
                    String heightRange = dataSnapshot.child("Height range").getValue(String.class);
                    String language = dataSnapshot.child("Language").getValue(String.class);
                    String dob = dataSnapshot.child("date of birth").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String phoneNumber = dataSnapshot.child("number").getValue(String.class);
                    String genderPre=dataSnapshot.child("GenderPre").getValue(String.class);
                    String gender=dataSnapshot.child("Gender").getValue(String.class);
                    String dtingplan=Integer.toString(currentDatingPlan);
                    Log.d("UserData", "Name: " + name);
                    Log.d("UserData", "Email: " + email);
                    Log.d("UserData", "Date of Birth: " + dob);
                    Log.d("UserData", "Location: " + location);
                    //Log.d("UserData", "Phone Number: " + phoneNumber);
                    Log.d("UserData", "Language: " + language);
                    Log.d("UserData", "Age Range: " + ageRange);
                    Log.d("UserData", "Height: " + height);
                    Log.d("UserData", "Height Range: " + heightRange);
                    //Log.d("UserData", "Current Dating Plan: " + currentDatingPlan);
                    Log.d("UserData", "Feedback: " + feedback);
                    Log.d("UserData", "Comment: " + comment);

                        name_View.setText(name);
                        phone_View.setText(phoneNumber);
                        location_View.setText(location);
                        dob_View.setText(dob);
                        language_View.setText(language);
                        heightrhange_View.setText(heightRange);
                        height_View.setText(height);
                        email_View.setText(email);
                        curdatingplan_View.setText(dtingplan);
                        agerange_View.setText(ageRange);
                        genderpre_View.setText(genderPre);
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

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout();
            }
        });
    }

    public void onLogout() {
        sessionManager.logoutUser();
        Intent intent = new Intent(ProfileMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}