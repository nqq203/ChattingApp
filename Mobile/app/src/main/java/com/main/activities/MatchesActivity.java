package com.main.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.GridView;
import android.widget.TextView;
import android.view.View;

import com.example.myapplication.R;
import com.main.adapters.MatchesAdapter;
import com.main.entities.MatchesItem;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matches_list);
        gridView=(GridView) findViewById(R.id.matchesGrid);
        ArrayList<MatchesItem> MatchesArrayList= new ArrayList<MatchesItem>();

        MatchesArrayList.add(new MatchesItem("Chi Sô",R.drawable.jisoo,21));
        MatchesArrayList.add(new MatchesItem("Hoa Hồng",R.drawable.rose,19));
        MatchesArrayList.add(new MatchesItem("Ren Ni",R.drawable.nini,20));
        MatchesArrayList.add(new MatchesItem("Li Xa",R.drawable.lisa,19));

        MatchesAdapter adapter= new MatchesAdapter(this,MatchesArrayList);
        gridView.setAdapter(adapter);
    }

}
