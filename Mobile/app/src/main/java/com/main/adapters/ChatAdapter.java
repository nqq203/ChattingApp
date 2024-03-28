package com.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<String> chatItemList;
    private Context context;

    public ChatAdapter(List<String> chatItemList, Context context) {
        this.chatItemList = chatItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String msg = chatItemList.get(position);
        holder.msgItem.setText(msg);
    }

    @Override
    public int getItemCount() {
        return chatItemList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView msgItem ;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            msgItem = itemView.findViewById(R.id.msg_item);
        }
    }
}
