package com.main.fragments;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.main.activities.MessageActivity;
import com.main.activities.SharingHobbiesActivity;
import com.main.adapters.HobbiesAdapter;
import com.main.entities.HobbiesItem;

import java.util.ArrayList;
import java.util.List;
import com.main.callbacks.MainCallbacks;

public class GridHobbiesFragments extends Fragment{

    SharingHobbiesActivity main;
    Context context = null;

    private GridView gridView;

    private EditText searchHobbies;
    private FrameLayout hobbiesButn;
    String hobbiesEnter;
    private ImageView backBtn;

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
        ArrayList<HobbiesItem> HobbiesArrayList= new ArrayList<HobbiesItem>();



        HobbiesArrayList.add(new HobbiesItem("GAME",R.drawable.game,false,R.drawable.border_hobbies));
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
        HobbiesArrayList.add(new HobbiesItem("MORE",R.drawable.more,false,R.drawable.border_hobbies));

        HobbiesAdapter adapter= new HobbiesAdapter(getContext(),HobbiesArrayList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HobbiesItem selectedHobby = adapter.getItem(position);
                if(selectedHobby.getSelected()==false) {

                    selectedHobby.setBackground(R.drawable.border_hobbies_clicked);
                    //hobbiesButn.setBackgroundResource(selectedHobby.getBackground());
                    selectedHobby.setSelected(true);

                    main.onMsgFromFragToMain("add", selectedHobby.getHobbies());
                }
                else if(selectedHobby.getSelected()==true)
                {
                    selectedHobby.setBackground(R.drawable.border_hobbies);
                    //
                    selectedHobby.setSelected(false);
                    main.onMsgFromFragToMain("delete", selectedHobby.getHobbies());
                }//MSg từ frag tới main
                gridView.setAdapter(adapter);
            }
        });
        return viewList;

    }
}
