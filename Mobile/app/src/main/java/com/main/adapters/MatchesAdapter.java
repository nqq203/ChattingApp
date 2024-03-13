package com.main.adapters;

import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.myapplication.R;
import com.main.entities.MatchesItem;

import java.util.List;

public class MatchesAdapter extends ArrayAdapter<MatchesItem> {
    public MatchesAdapter(@NonNull Context context, List<MatchesItem> customer) {
        super(context, 0, customer);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull  ViewGroup parent) {
        MatchesItem matchesItem = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_pic, parent, false);
        }

        TextView IdText = (TextView) convertView.findViewById(R.id.txtView_NameAge);
        ImageButton imageView = (ImageButton) convertView.findViewById(R.id.Mtch_Pic);

        IdText.setText(matchesItem.getName()+", "+matchesItem.getAge());
        imageView.setBackgroundResource(matchesItem.getPicture());
        return convertView;
    }

}
