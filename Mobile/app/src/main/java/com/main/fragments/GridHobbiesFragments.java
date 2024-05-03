package com.main.fragments;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.activities.MatchesActivity;
import com.main.activities.MessageActivity;
import com.main.activities.SharingHobbiesActivity;
import com.main.adapters.HobbiesAdapter;
import com.main.adapters.MatchesAdapter;
import com.main.entities.HobbiesItem;

import java.util.ArrayList;
import java.util.List;
import com.main.callbacks.MainCallbacks;
import com.main.entities.MatchesItem;

public class GridHobbiesFragments extends Fragment{

    SharingHobbiesActivity main;
    Context context = null;

    private GridView gridView;

    private EditText searchHobbies;
    private FrameLayout hobbiesButn;
    String hobbiesEnter;
    private ImageView backBtn;
    String userId="us1";
    HobbiesAdapter adapter;
    ArrayList<HobbiesItem> HobbiesArrayList;
    FirebaseDatabase firebaseDatabase_UPD=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");

    public static GridHobbiesFragments newInstance(String strArg) {
        GridHobbiesFragments fragment = new GridHobbiesFragments();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            main = (SharingHobbiesActivity) getActivity();
        }
        catch(IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewList= inflater.inflate(R.layout.frag_gridhobbies, container, false);
        searchHobbies=(EditText) viewList.findViewById(R.id.SearchBar);
        backBtn = (ImageView) viewList.findViewById(R.id.back_arrow);

        backBtn.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        searchHobbies.setHint("Search hobbies...");

        gridView=(GridView) viewList.findViewById(R.id.grid_hobbies);
        HobbiesArrayList= new ArrayList<HobbiesItem>();

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
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
                    adapter= new HobbiesAdapter(getContext(),HobbiesArrayList);
                    gridView.setAdapter(adapter);
                }
                else {
                    System.out.println("Cant find data");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
        /*
        HobbiesArrayList.add(new HobbiesItem("GAME",R.drawable.game,true,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("MUSIC",R.drawable.music,false,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("FILM",R.drawable.movie,false,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("RUNNING",R.drawable.running,false,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("COFFE",R.drawable.coffe,false,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("CONNECT",R.drawable.connect,false,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("FRIENDS",R.drawable.friends,false,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("FOODS",R.drawable.food,false,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("DRAWING",R.drawable.draw,false,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("READING",R.drawable.reading,false,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("PHOTOGRAPHY",R.drawable.photography,false,R.drawable.border_hobbies));
        HobbiesArrayList.add(new HobbiesItem("SPORT",R.drawable.border_sport,false,R.drawable.border_hobbies));

        HobbiesAdapter adapter= new HobbiesAdapter(getContext(),HobbiesArrayList);
        gridView.setAdapter(adapter);*/

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HobbiesItem selectedHobby = adapter.getItem(position);

                if(selectedHobby.getSelected()==false) {
                    selectedHobby.setBackground(R.drawable.border_hobbies_clicked);
                    selectedHobby.setSelected(true);
                    HobbiesArrayList.get(position).setSelected(true);
                    HobbiesArrayList.get(position).setBackground(R.drawable.border_hobbies_clicked);
                    main.onMsgFromFragToMain("add", selectedHobby.getHobbies());
                }
                else if(selectedHobby.getSelected()==true)
                {
                    selectedHobby.setBackground(R.drawable.border_hobbies);
                    //
                    selectedHobby.setSelected(false);
                    HobbiesArrayList.get(position).setSelected(false);
                    main.onMsgFromFragToMain("delete", selectedHobby.getHobbies());
                    HobbiesArrayList.get(position).setBackground(R.drawable.border_hobbies);
                }//MSg từ frag tới main
                gridView.setAdapter(adapter);
            }
        });
        return viewList;


    }
    public void onMsgFromMainToFragment(String context, String hobby) { //msg từ main đến frag //nhận
        String isSelected1="0";
        if(context.equals("push_Hobby")) {
            for (int i = 0; i < HobbiesArrayList.size(); i++) {
                HobbiesItem item = HobbiesArrayList.get(i);

                String HobbyUPD=item.getHobbies();

                DatabaseReference databaseReference_UPD = firebaseDatabase_UPD.getReference("Hobbies/"+userId+"/"+HobbyUPD);

                Boolean selected=item.getSelected();
                Log.d(item.getHobbies(),String. valueOf(item.getSelected()));

                if(selected.equals(true)){
                    Log.d("TRUE NE","TRUE NE");
                    isSelected1="1";
                }
                else
                {
                    Log.d("FALSE NE","FALSE NE");
                    isSelected1="0";
                }


                databaseReference_UPD.child("isSelected").setValue(isSelected1);
            }
        }
    }


}
