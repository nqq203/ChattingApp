
package com.main.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.NotificationAdapter;
import com.example.myapplication.R;
import com.main.adapters.MatchesAdapter;
import com.main.entities.MatchesItem;
import com.main.entities.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private ListView listView;
    private List<NotificationItem> notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_main);

        listView = (ListView) findViewById(R.id.noti_list);
        notification = new ArrayList<>();
        notification.add(new NotificationItem("Dennis Nerdy just replied on your story", R.drawable.jisoo, "Xinh thế", "Last Wednesday at 9:42 AM", R.drawable.nini));
        notification.add(new NotificationItem("Its a match you just matched with Henry Cavill", R.drawable.lisa, "Last Wednesday at 9:42 AM"));
        notification.add(new NotificationItem("You have 3 unread message of Bryson Potts. Let’s catch up!", R.drawable.rose, "Last Wednesday at 9:42 AM"));

        NotificationAdapter adapter = new NotificationAdapter(this, notification);
        listView.setAdapter(adapter);

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Customer selectedCustomer=adapter.getItem(position);
                txtMsg.setText("Your selection is:"+ selectedCustomer.getName());
            }
        });
         */
    }
}





