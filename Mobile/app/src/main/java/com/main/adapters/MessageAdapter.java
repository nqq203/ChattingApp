package com.main.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.MemoryData;
import com.main.activities.ChatActivity;
import com.main.entities.MessageList;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(MessageList message);
    }
    private List<MessageList> messageLists;
    private final Context context;
    private OnItemClickListener listener;

    public MessageAdapter(List<MessageList> messageLists, Context context, OnItemClickListener listener) {
        this.messageLists = messageLists;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_adapter, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        MessageList item = messageLists.get(position);
        holder.name.setText(item.getName());
        holder.lastMessage.setText(item.getLastMessage());
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(context).load(item.getImageUrl()).apply(requestOptions).into(holder.profileImg);
        if (item.getUnseenMessage() == 0) {
            holder.unseenMessage.setVisibility(View.GONE);
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.gray));
        }
        else {
            holder.unseenMessage.setVisibility(View.VISIBLE);
            holder.unseenMessage.setText(item.getUnseenMessage()+"");
            holder.unseenMessage.setTextColor(context.getResources().getColor(R.color.white));
            holder.unseenMessage.setBackground(context.getResources().getDrawable(R.drawable.circle_background));
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.purple_2));
        }
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(item);
                }
//                Intent intent = new Intent(context, ChatActivity.class);
//                Log.d(TAG, "mobile: " + item.getMobile());
//                Log.d(TAG, "fullname: " + item.getName());
//                Log.d(TAG, "imageUrl: " + item.getImageUrl());
//                Log.d(TAG, "chatKey: " + item.getChatKey());
//
//                intent.putExtra("mobile", item.getMobile());
//                intent.putExtra("fullname", item.getName());
//                intent.putExtra("imageUrl", item.getImageUrl());
//                intent.putExtra("chatKey", item.getChatKey());
//
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImg;
        private TextView name;
        private TextView lastMessage;
        private TextView unseenMessage;
        private LinearLayout rootLayout;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.user_msg_image);
            name = itemView.findViewById(R.id.sender_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            unseenMessage = itemView.findViewById(R.id.unseen_message);
            rootLayout = itemView.findViewById(R.id.message_root_layout);
        }
    }

    public void updateData(List<MessageList> messageLists) {
        this.messageLists = messageLists;
        notifyDataSetChanged();
    }
}