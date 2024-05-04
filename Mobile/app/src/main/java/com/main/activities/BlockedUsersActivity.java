package com.main.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group4.matchmingle.R;
import com.main.adapters.BlockedUsersAdapter;
import com.main.entities.BlockedUser;

import java.util.ArrayList;

public class BlockedUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BlockedUsersAdapter adapter;
    private ArrayList<BlockedUser> blockedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);

        recyclerView = findViewById(R.id.recyclerViewBlockedUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data for demonstration
        blockedUsers = new ArrayList<>();
        blockedUsers.add(new BlockedUser("1", "John Doe"));
        blockedUsers.add(new BlockedUser("2", "Jane Smith"));

        adapter = new BlockedUsersAdapter(blockedUsers, user -> {
            // Handle unblock logic here
        });

        recyclerView.setAdapter(adapter);
    }
}
