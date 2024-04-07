package com.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.group4.matchmingle.R;
import com.main.activities.MessageActivity;
import com.main.entities.Story;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    private List<Story> storiesList;
    private Context context;

    public StoryAdapter(List<Story> storiesList, Context context) {
        this.storiesList = storiesList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = storiesList.get(position);
        holder.storyFullname.setText(story.getFullname());
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(context).load(story.getImageUrl()).apply(requestOptions).into(holder.storyImage);
    }

    @Override public int getItemCount() {
        return storiesList.size();
    }
    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView storyImage;
        TextView storyFullname;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.story_image);
            storyFullname = itemView.findViewById(R.id.story_fullname);

            storyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, Story.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}