package com.main.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myapplication.R;

public class GridAdapter extends BaseAdapter {

    Context  context;
    String[] matches;
    int [] image;
    LayoutInflater inflater;

    public GridAdapter(Context context, String[] matches, int[] image) {
        this.context = context;
        this.matches = matches;
        this.image = image;
    }

    @Override
    public int getCount() {
        return matches.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater==null)
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.grid_pic,root: null);
        }*/
        ImageButton imagebutton=convertView.findViewById(R.id.grid_image);
        Button buttonDel=convertView.findViewById(R.id.grid_ButDelete);
        Button buttonChat=convertView.findViewById(R.id.grid_ButChat);

        return null;
    }
}
