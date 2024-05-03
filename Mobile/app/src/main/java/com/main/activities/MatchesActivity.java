package com.main.activities;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.GridView;
import android.widget.TextView;
import android.view.View;

import com.group4.matchmingle.R;
import com.main.adapters.MatchesAdapter;
import com.main.entities.MatchesItem;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class MatchesActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matches_list);

        UserSessionManager sessionManager = new UserSessionManager(getBaseContext());
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        String userId=userDetails.get(UserSessionManager.KEY_PHONE_NUMBER);

        Button iconHome = findViewById(R.id.icon_home);
        Button iconFavourite = findViewById(R.id.icon_favorite);
        Button iconChat = findViewById(R.id.icon_chat);
        Button iconProfile = findViewById(R.id.icon_profile);

        iconHome.setTextColor(getResources().getColor(R.color.black));
        iconFavourite.setTextColor(getResources().getColor(R.color.purple_2));
        iconChat.setTextColor(getResources().getColor(R.color.black));
        iconProfile.setTextColor(getResources().getColor(R.color.black));

        iconChat.setOnClickListener(v -> {
            Intent intent = new Intent(MatchesActivity.this, MessageActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        iconProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MatchesActivity.this, ProfileMainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        iconHome.setOnClickListener(v -> {
            Intent intent = new Intent(MatchesActivity.this, SwipeCardViewActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });


        gridView=(GridView)  findViewById(R.id.matchesGrid);
        ArrayList<MatchesItem> MatchesArrayList= new ArrayList<MatchesItem>();

        MatchesAdapter adapter= new MatchesAdapter(this,MatchesArrayList);
        gridView.setAdapter(adapter);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Matches/"+userId);
        //databaseReference.child("Test");

        // Lấy dữ liệu từ nút "us1"

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);

                //System.out.println("Loading data: "+value);
                if (dataSnapshot.exists()) {

                    MatchesArrayList.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        String key = snap.getKey();
                        //MatchesItem matchesItem = snap.getValue(MatchesItem.class);
                        String name = snap.child("name").getValue(String.class);
                        int age = snap.child("age").getValue(Integer.class);
                        String pic = snap.child("pic").getValue(String.class);
                        String id = snap.child("userid").getValue(String.class);
                        MatchesItem matchesItem= new MatchesItem(name,pic,age,key);

                        MatchesArrayList.add(matchesItem);
                    }
                    MatchesAdapter adapter = new MatchesAdapter(MatchesActivity.this, MatchesArrayList);
                    gridView.setAdapter(adapter);
                }
                else {
                    System.out.println("Cant find data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
        /*
        MatchesArrayList.add(new MatchesItem("Chi Sô","https://pm1.aminoapps.com/7979/d76f871ef7a8985cb3cc9dd8d117f9c1efc03888r1-720-806v2_uhq.jpg",21));
        MatchesArrayList.add(new MatchesItem("Hoa Hồng","https://pm1.aminoapps.com/7979/d76f871ef7a8985cb3cc9dd8d117f9c1efc03888r1-720-806v2_uhq.jpg",19));
        MatchesArrayList.add(new MatchesItem("Ren Ni","https://pm1.aminoapps.com/7979/d76f871ef7a8985cb3cc9dd8d117f9c1efc03888r1-720-806v2_uhq.jpg",20));
        //MatchesArrayList.add(new MatchesItem("Li Xa",R.drawable.lisa,19));

        MatchesAdapter adapter= new MatchesAdapter(this,MatchesArrayList);
        gridView.setAdapter(adapter);

         */
    }

}