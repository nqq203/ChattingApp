package com.main.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.main.activities.SharingHobbiesActivity;
import com.main.entities.HobbiesItem;
import com.main.entities.MatchesItem;

import java.util.List;


public class HobbiesAdapter extends ArrayAdapter<HobbiesItem> {

    public HobbiesAdapter(@NonNull Context context, List<HobbiesItem> hobbies) {
        super(context, 0, hobbies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HobbiesItem hobbiesItems = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_hobbies, parent, false);
        }

        TextView IdText = (TextView) convertView.findViewById(R.id.hobbiesName);
        View imageView = (View) convertView.findViewById(R.id.hobbiesPic);
        FrameLayout hobbiesButn =(FrameLayout) convertView.findViewById(R.id.HobbiesButton);
        hobbiesButn.setBackgroundResource(hobbiesItems.getBackground());
        IdText.setText(hobbiesItems.getHobbies());
        imageView.setBackgroundResource(hobbiesItems.getPicHobbies());


        return convertView;
    }
}
