package com.main.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ImageViewHolder> {
    private Context mContext;
    private Integer[] mImageIds;

    public ProfileAdapter(Context context, Integer[] imageIds) {
        mContext = context;
        mImageIds = imageIds;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_grid_item_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        int imageResourceId = mImageIds[position];
        Drawable drawable = ContextCompat.getDrawable(mContext, imageResourceId);
        holder.imageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return mImageIds.length;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
