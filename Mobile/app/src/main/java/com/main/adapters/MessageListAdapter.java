package com.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.main.entities.MessageItem;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    public interface OnMessageItemClickListener {
        void onItemCLick(int position);
    }

    private List<MessageItem> messageItemList;
    private Context context;
    private OnMessageItemClickListener listener;

    public MessageListAdapter(List<MessageItem> messageItemList, Context context) {
        this.messageItemList = messageItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageItem message = messageItemList.get(position);
        holder.senderName.setText(message.getSenderName());
        holder.messageText.setText(message.getContent());
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(context).load(message.getImageUrl()).apply(requestOptions).into(holder.userImg);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemCLick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageItemList.size();
    }

    public void setOnItemClickListener(OnMessageItemClickListener listener) {
        this.listener = listener;
    }
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView senderName;
        TextView messageText;
        TextView timestamp;
        ImageView userImg;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.sender_name);
            messageText = itemView.findViewById(R.id.message_text);
            timestamp = itemView.findViewById(R.id.timestamp);
            userImg = itemView.findViewById(R.id.user_msg_image);
        }
    }
}