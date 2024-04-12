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
import com.main.activities.StoryActivity;
import com.main.activities.UploadStory;
import com.main.entities.Story;

import java.util.List;
public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_STORY = 1;
    private static final int VIEW_TYPE_UPLOAD = 2;

    private List<Story> storiesList;
    private Context context;

    public StoryAdapter(List<Story> storiesList, Context context) {
        this.storiesList = storiesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_TYPE_STORY) {
            View view = inflater.inflate(R.layout.story_item, parent, false);
            viewHolder = new StoryViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.btn_upload_story, parent, false);
            viewHolder = new UploadViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StoryViewHolder) {
            StoryViewHolder storyViewHolder = (StoryViewHolder) holder;
            Story story = storiesList.get(position);
            storyViewHolder.storyFullname.setText(story.getFullname());
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            Glide.with(context).load(story.getImageUrl()).apply(requestOptions).into(storyViewHolder.storyImage);
        } else if (holder instanceof UploadViewHolder) {
            // Đặt dữ liệu hoặc lắng nghe sự kiện cho nút tải lên story ở đây
        }
    }

    @Override
    public int getItemCount() {
        return storiesList.size() + 1; // Cộng thêm 1 để hiển thị nút tải lên story
    }

    @Override
    public int getItemViewType(int position) {
        return position == storiesList.size() ? VIEW_TYPE_UPLOAD : VIEW_TYPE_STORY;
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
                        Intent intent = new Intent(context, StoryActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public static class UploadViewHolder extends RecyclerView.ViewHolder {
        ImageView storyImage;
        public UploadViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.story_upload);
            storyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, UploadStory.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}