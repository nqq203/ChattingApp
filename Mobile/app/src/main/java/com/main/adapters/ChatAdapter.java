package com.main.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group4.matchmingle.R;
import com.main.activities.UserSessionManager;
import com.main.entities.ChatList;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatList> chatList;
    private final Context context;
    private UserSessionManager sessionManager;
    private String myPhone;


    public ChatAdapter(List<ChatList> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
        this.sessionManager = new UserSessionManager(context.getApplicationContext());
        this.myPhone = this.sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);
    }
    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatList item = chatList.get(position);

        if (item.getMobile().equals(myPhone)) {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.guestLayout.setVisibility(View.GONE);
            holder.myMessage.setText(item.getMessage());
        }
        else {
            holder.myLayout.setVisibility(View.GONE);
            holder.guestLayout.setVisibility(View.VISIBLE);
            holder.guestMessage.setText(item.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void updateChatLists(List<ChatList> chatList) {
        this.chatList = chatList;
        notifyDataSetChanged();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout guestLayout, myLayout;
        private TextView guestMessage, myMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            guestLayout = itemView.findViewById(R.id.chat_item_guest);
            myLayout = itemView.findViewById(R.id.chat_item);

            guestMessage = itemView.findViewById(R.id.msg_item_guest);
            myMessage = itemView.findViewById(R.id.msg_item);
        }
    }
}