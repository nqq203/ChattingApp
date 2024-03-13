package com.main.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.main.entities.MessageItem;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    private List<MessageItem> messageList;
    private Context context;

    public MessageListAdapter(List<MessageItem> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView name, content, count;
        ImageView thumbnail;
        RelativeLayout viewIndicator;

        MessageViewHolder(View view) {
            super(view);
//            name = view.findViewById(R.id.text_name);
//            content = view.findViewById(R.id.text_content);
//            thumbnail = view.findViewById(R.id.thumbnail);
//            viewIndicator = view.findViewById(R.id.layout_dot_indicator);
            name = null;
            content = null;
            thumbnail = null;
            viewIndicator = null;
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_message_item, parent, false);

        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, final int position) {
        final MessageItem item = messageList.get(position);
        holder.name.setText(item.getName());
        holder.content.setText(item.getContent());

        if(item.getCount() <= 0){
            holder.viewIndicator.setVisibility(View.INVISIBLE);
        }
        /*
        Glide.with(context)
                .load(item.getPicture())
                .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
