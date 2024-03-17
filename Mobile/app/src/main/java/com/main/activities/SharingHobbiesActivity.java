package com.main.activities;
import static com.main.fragments.TextViewHobbiesFragment.newInstance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.GridView;
import android.widget.TextView;
import android.view.View;

import com.example.myapplication.R;
import com.main.adapters.HobbiesAdapter;
import com.main.callbacks.MainCallbacks;
import com.main.entities.HobbiesItem;
import com.main.fragments.GridHobbiesFragments;
import com.main.fragments.TextViewHobbiesFragment;


import java.util.ArrayList;
/*
public class SharingHobbiesActivity extends AppCompatActivity {

    private GridView gridView;
    private EditText searchHobbies;
    private TextView hobbiestext;
    String hobbiesEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharing_hobbies);
        hobbiestext=(TextView) findViewById(R.id.HobbiesText);
        searchHobbies=(EditText) findViewById(R.id.SearchBar);
        searchHobbies.setHint("Search hobbies...");

        gridView=(GridView) findViewById(R.id.grid_hobbies);
        ArrayList<HobbiesItem> HobbiesArrayList= new ArrayList<HobbiesItem>();

        HobbiesArrayList.add(new HobbiesItem("GAME",R.drawable.game,false));
        HobbiesArrayList.add(new HobbiesItem("MUSIC",R.drawable.music,false));
        HobbiesArrayList.add(new HobbiesItem("FILM",R.drawable.movie,false));
        HobbiesArrayList.add(new HobbiesItem("RUNNING",R.drawable.running,false));
        HobbiesArrayList.add(new HobbiesItem("COFFE",R.drawable.coffe,false));
        HobbiesArrayList.add(new HobbiesItem("CONNECT",R.drawable.connect,false));
        HobbiesArrayList.add(new HobbiesItem("FRIENDS",R.drawable.friends,false));
        HobbiesArrayList.add(new HobbiesItem("FOODS",R.drawable.food,false));
        HobbiesArrayList.add(new HobbiesItem("DRAWING",R.drawable.draw,false));
        HobbiesArrayList.add(new HobbiesItem("READING",R.drawable.reading,false));
        HobbiesArrayList.add(new HobbiesItem("PHOTOGRAPHY",R.drawable.photography,false));
        HobbiesArrayList.add(new HobbiesItem("MORE",R.drawable.more,false));

        HobbiesAdapter adapter= new HobbiesAdapter(this,HobbiesArrayList);
        gridView.setAdapter(adapter);



    }



}*/

public class SharingHobbiesActivity extends FragmentActivity implements MainCallbacks {

    FragmentTransaction ft;
    GridHobbiesFragments GridHobbies;
    TextViewHobbiesFragment TextViewHobbies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_sharing_hobbies);

        ft = getSupportFragmentManager().beginTransaction();
        GridHobbies = GridHobbiesFragments.newInstance("first-list");
        ft.replace(R.id.frame_list_fragment, GridHobbies);
        ft.commit();

        ft = getSupportFragmentManager().beginTransaction();
        TextViewHobbies = newInstance("first-detail");
        ft.replace(R.id.frame_detail_fragment, TextViewHobbies);
        ft.commit();
    }
    public void onMsgFromFragToMain (String sender, String hobby) {
        TextViewHobbies.onMsgFromMainToFragment(sender, hobby);
    }
}