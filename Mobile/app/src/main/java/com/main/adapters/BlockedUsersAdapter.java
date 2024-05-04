package com.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group4.matchmingle.R;
import com.main.entities.BlockedUser;

import java.util.List;

public class BlockedUsersAdapter extends RecyclerView.Adapter<BlockedUsersAdapter.ViewHolder> {

    private List<BlockedUser> blockedUsers;
    private OnUnblockClickListener listener;

    public interface OnUnblockClickListener {
        void onUnblockClick(BlockedUser user);
    }

    public BlockedUsersAdapter(List<BlockedUser> blockedUsers, OnUnblockClickListener listener) {
        this.blockedUsers = blockedUsers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BlockedUser user = blockedUsers.get(position);
        holder.userName.setText(user.getName());
        holder.unblockButton.setOnClickListener(v -> listener.onUnblockClick(user));
    }

    @Override
    public int getItemCount() {
        return blockedUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        Button unblockButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textUserName);
            unblockButton = itemView.findViewById(R.id.buttonUnblock);
        }
    }
}
