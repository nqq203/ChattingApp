package com.main.activities;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
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
import com.main.adapters.HobbiesAdapter;
import com.main.adapters.MatchesAdapter;
import com.main.entities.HobbiesItem;
import com.main.entities.MatchesItem;

import java.util.ArrayList;
import java.util.HashMap;

public class XemHobbiesActivity extends AppCompatActivity {

    private GridView gridView;
    String userId="us1";
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xem_hobbies);

        ImageView backbtn=findViewById(R.id.back_arrow);
        Button iconHome = findViewById(R.id.icon_home);
        Button iconFavourite = findViewById(R.id.icon_favorite);
        Button iconChat = findViewById(R.id.icon_chat);
        Button iconProfile = findViewById(R.id.icon_profile);
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(XemHobbiesActivity.this, MessageActivity.class);
            startActivity(intent);
            finish();
        });


        TextView HobbiesText=(TextView) findViewById(R.id.HobbiesText);


        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Hobbies/"+userId);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String allHobby = dataSnapshot.child("AllHobby").getValue(String.class);
                    Log.d("All HOBBY",allHobby);
                    HobbiesText.setText(allHobby);
                }
                else {
                    System.out.println("Không tìm thấy dữ liệu cho người dùng có ID: " + userId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                System.out.println("Error: " + databaseError.getMessage());
            }
        });

        iconHome.setTextColor(getResources().getColor(R.color.black));
        iconFavourite.setTextColor(getResources().getColor(R.color.purple_2));
        iconChat.setTextColor(getResources().getColor(R.color.black));
        iconProfile.setTextColor(getResources().getColor(R.color.black));

        iconChat.setOnClickListener(v -> {
            Intent intent = new Intent(XemHobbiesActivity.this, MessageActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        iconProfile.setOnClickListener(v -> {
            Intent intent = new Intent(XemHobbiesActivity.this, ProfileMainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        iconHome.setOnClickListener(v -> {
            Intent intent = new Intent(XemHobbiesActivity.this, SwipeCardViewActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });


        gridView=(GridView) findViewById(R.id.grid_hobbies);
        ArrayList<HobbiesItem> HobbiesArrayList= new ArrayList<HobbiesItem>();


        DatabaseReference databaseReference = firebaseDatabase.getReference("Hobbies/"+userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);

                //System.out.println("Loading data: "+value);
                if (dataSnapshot.exists()) {
                    HobbiesArrayList.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        String key = snap.getKey();
                        String isSelected="0";
                        if(!key.equals("AllHobby")){
                            isSelected  = snap.child("isSelected").getValue(String.class);
                            Log.d("isSelected NE",isSelected);
                        }
                        if (key.equals("GAME")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("GAME", R.drawable.game, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("GAME", R.drawable.movie, true, R.drawable.border_hobbies_clicked));
                            }
                        }
                        if (key.equals("SPORT")) {

                            if (isSelected.equals("0") ){
                                Log.d("VO DAY NE","NO DA VO DC DAY");
                                HobbiesArrayList.add(new HobbiesItem("SPORT", R.drawable.border_sport, false, R.drawable.border_hobbies));
                            } else {
                                Log.d("SPORT","SPORT HERE");
                                HobbiesArrayList.add(new HobbiesItem("SPORT", R.drawable.border_sport, true, R.drawable.border_hobbies_clicked));
                            }
                        }
                        if (key.equals("PHOTOGRAPHY")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("PHOTOGRAPHY", R.drawable.photography, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("PHOTOGRAPHY", R.drawable.photography, true, R.drawable.border_hobbies_clicked));

                            }
                        }
                        if (key.equals("READING")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("READING", R.drawable.reading, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("READING", R.drawable.reading, true, R.drawable.border_hobbies_clicked));

                            }
                        }
                        if (key.equals("DRAWING")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("DRAWING", R.drawable.draw, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("DRAWING", R.drawable.draw, true, R.drawable.border_hobbies_clicked));
                            }
                        }
                        if (key.equals("FOODS")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("FOODS", R.drawable.food, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("FOODS", R.drawable.food, true, R.drawable.border_hobbies_clicked));

                            }
                        }
                        if (key.equals("FRIENDS")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("FRIENDS", R.drawable.friends, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("FRIENDS", R.drawable.friends, true, R.drawable.border_hobbies_clicked));

                            }
                        }
                        if (key.equals("CONNECT")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("CONNECT", R.drawable.connect, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("CONNECT", R.drawable.connect, true, R.drawable.border_hobbies_clicked));

                            }
                        }
                        if (key.equals("COFFE")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("COFFE", R.drawable.coffe, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("COFFE", R.drawable.coffe, true, R.drawable.border_hobbies_clicked));

                            }
                        }
                        if (key.equals("RUNNING")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("RUNNING", R.drawable.running, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("RUNNING", R.drawable.running, true, R.drawable.border_hobbies_clicked));

                            }
                        }
                        if (key.equals("FILM")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("FILM", R.drawable.movie, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("FILM", R.drawable.movie, true, R.drawable.border_hobbies_clicked));

                            }
                        }
                        if (key.equals("MUSIC")) {
                            if (isSelected.equals("0")) {
                                HobbiesArrayList.add(new HobbiesItem("MUSIC", R.drawable.movie, false, R.drawable.border_hobbies));
                            } else {
                                HobbiesArrayList.add(new HobbiesItem("MUSIC", R.drawable.movie, true, R.drawable.border_hobbies_clicked));
                            }
                        }

                    }
                    HobbiesAdapter adapter= new HobbiesAdapter(XemHobbiesActivity.this,HobbiesArrayList);
                    gridView.setAdapter(adapter);
                }
                else {

                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Hobbies/"+userId);

                    HashMap<String, Object> hobbiesMap = new HashMap<>();
                    hobbiesMap.put("AllHobby", "");
                    hobbiesMap.put("GAME", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("MUSIC", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("FILM", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("RUNNING", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("COFFE", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("CONNECT", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("FRIENDS", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("FOODS", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("DRAWING", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("READING", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("PHOTOGRAPHY", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    hobbiesMap.put("SPORT", new HashMap<String, Object>() {{
                        put("isSelected", "0");
                    }});
                    databaseReference.setValue(hobbiesMap);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
