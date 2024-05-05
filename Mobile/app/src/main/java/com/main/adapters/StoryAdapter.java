package com.main.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.main.entities.User;

import java.util.List;
public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_STORY = 1;
    private static final int VIEW_TYPE_UPLOAD = 2;

    private static List<User> usersList;
    private Context context;

    public StoryAdapter(List<User> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        Log.d("abc", String.valueOf(viewType));

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
            User user = usersList.get(position);
            storyViewHolder.Fullname.setText(user.getFullname());
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(storyViewHolder.storyImage);
        } else if (holder instanceof UploadViewHolder) {
            // Đặt dữ liệu hoặc lắng nghe sự kiện cho nút tải lên story ở đây
        }
    }

    @Override
    public int getItemCount() {
        // Nếu không có người dùng nào match, itemCount là 1 (cho nút "tải lên story")
        // Nếu có người dùng match, itemCount là kích thước của usersList cộng
        return usersList.isEmpty() ? 1 : usersList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == usersList.size() ? VIEW_TYPE_UPLOAD : VIEW_TYPE_STORY;
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView storyImage;
        TextView Fullname;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.story_image);
            Fullname = itemView.findViewById(R.id.story_fullname);

            storyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Lấy story tương ứng với vị trí của phần tử được bấm
                        User clickedUser = usersList.get(position);

                        // Tạo Intent với userID của story được bấm
                        if (clickedUser != null && clickedUser.getPhoneNumber() != null) {
                            // Nếu clickedUser không null và số điện thoại không null
                            // Thực hiện việc gửi intent ở đây
                            Context context = view.getContext();
                            Intent intent = new Intent(context, StoryActivity.class);
                            intent.putExtra("userID", clickedUser.getPhoneNumber());
                            Log.d("abc", clickedUser.getPhoneNumber());
                            Log.d("abc", "ccccc");
                            context.startActivity(intent);
                        } else {
                            // Nếu clickedUser hoặc số điện thoại là null
                            // Hiển thị thông báo hoặc thực hiện hành động phù hợp
                            Log.d("abc", "clickedUser or phoneNumber is null");
                        }
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