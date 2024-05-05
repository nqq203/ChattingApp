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

import com.google.android.material.slider.RangeSlider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import java.util.Calendar;
import java.util.List;

public class PreferencesEdit extends AppCompatActivity {
    Button backbtn,savebtn;
    String userId="us1";
    Context context;
    EditText  genderpre_View;
    TextView heightrange_View,agerange_View;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_pre);
        context = this;
        savebtn=(Button) findViewById(R.id.save_button);


        genderpre_View=(EditText) findViewById(R.id.gender_profile);
        heightrange_View=(TextView) findViewById(R.id.PreHeight_profile);
        agerange_View=(TextView) findViewById(R.id.agerange_profile);

        RangeSlider AgeRange = findViewById(R.id.SliderAgeRank_profile);
        RangeSlider HeightRange = findViewById(R.id.SliderHeight_profile);


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

        iconChat.setOnClickListener(v -> {
            Intent intent = new Intent(PreferencesEdit.this, MessageActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        iconHome.setOnClickListener(v -> {
            Intent intent = new Intent(PreferencesEdit.this, SwipeCardViewActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        iconFavourite.setOnClickListener(v -> {
            Intent intent = new Intent(PreferencesEdit.this, MatchesActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        back_arrow.setOnClickListener(v -> {
            Intent intent = new Intent(PreferencesEdit.this, ProfileMainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gender = genderpre_View.getText().toString();
                String agerange = agerange_View.getText().toString();
                String heightrange = heightrange_View.getText().toString();

                databaseReference1.child("GenderPre").setValue(gender);
                databaseReference1.child("Age range").setValue(agerange);
                databaseReference1.child("Height range").setValue(heightrange);


            }
        });


        AgeRange.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
                // Lấy giá trị khi RangeSlider thay đổi
                List<Float> values = slider.getValues();
                int minValue =Math.round(values.get(0));
                int maxValue = Math.round(values.get(1));
                agerange_View.setText(""+minValue+"-"+maxValue);
            }
        });

        HeightRange.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
                // Lấy giá trị khi RangeSlider thay đổi
                List<Float> values = slider.getValues();
                int minValue =Math.round(values.get(0));
                int maxValue = Math.round(values.get(1));
                heightrange_View.setText(""+minValue+"-"+maxValue);
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
                    String gender = dataSnapshot.child("GenderPre").getValue(String.class);
                    String agerange = dataSnapshot.child("Age range").getValue(String.class);
                    String height = dataSnapshot.child("Height range").getValue(String.class);
                    System.out.println(height);
                    genderpre_View.setText(gender);
                    agerange_View.setText(agerange);
                    heightrange_View.setText(height);


                    String[] agerange_Array = agerange.split("-");
                    float x_age = Float.parseFloat(agerange_Array[0]);
                    float y_age = Float.parseFloat(agerange_Array[1]);
                    AgeRange.setValues(x_age, y_age);

                    String[] heightrange_Array = height.split("-");
                    float x_height = Float.parseFloat(heightrange_Array[0]);
                    float y_height = Float.parseFloat(heightrange_Array[1]);
                    HeightRange.setValues(x_height, y_height);


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
}

