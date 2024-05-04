/*
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Customer selectedCustomer=adapter.getItem(position);
                txtMsg.setText("Your selection is:"+ selectedCustomer.getName());
            }
        });

    }
}*/

package com.main.activities;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.main.adapters.NotificationAdapter;
import com.group4.matchmingle.R;
import com.group4.matchmingle.databinding.ActivityMainBinding;
import com.main.adapters.MatchesAdapter;
import com.main.entities.MatchesItem;
import com.main.entities.NotificationItem;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.android.material.snackbar.Snackbar;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class NotificationActivity extends AppCompatActivity {
    // creating a variable for recycler view,
    // array list and adapter class.
    String userId;
    private RecyclerView notiRV;
    private ArrayList<NotificationItem> notiArrayList;
    private NotificationAdapter notiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_noti);

        UserSessionManager sessionManager = new UserSessionManager(getBaseContext());
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        userId=userDetails.get(UserSessionManager.KEY_PHONE_NUMBER);



        // initializing our variables.
        notiRV = findViewById(R.id.noti_list);
        ImageView backBtn = findViewById(R.id.back_arrow);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, SwipeCardViewActivity.class);
            startActivity(intent);
            finish();
        });
        notiArrayList = new ArrayList<>();

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Notification/"+userId);
        // creating new array list.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);
                //System.out.println("Loading data: "+value);
                if (dataSnapshot.exists()) {
                    notiArrayList.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        String key = snap.getKey();
                        //MatchesItem matchesItem = snap.getValue(MatchesItem.class);
                        String Description = snap.child("Description").getValue(String.class);
                        String Time = snap.child("Time").getValue(String.class);
                        String Type = snap.child("Type").getValue(String.class);
                        String userID = snap.child("UserId").getValue(String.class);
                        NotificationItem notificationItem= new NotificationItem(Description,"",Type,"",Time,"",key,userID);
                        Log.d("MyActivity", "Description: " + notificationItem.toString());
                        notiArrayList.add(notificationItem);
                    }
                    Collections.reverse(notiArrayList);
                    notiAdapter = new NotificationAdapter(notiArrayList, NotificationActivity.this);

                    // below line is to set layout manager for our recycler view.
                    LinearLayoutManager manager = new LinearLayoutManager(NotificationActivity.this);

                    // setting layout manager for our recycler view.
                    notiRV.setLayoutManager(manager);

                    // below line is to set adapter
                    // to our recycler view.
                    notiRV.setAdapter(notiAdapter);
                }
                else {
                    System.out.println("Cant find data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
        notiAdapter = new NotificationAdapter(notiArrayList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);


        notiRV.setLayoutManager(manager);


        notiRV.setAdapter(notiAdapter);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                NotificationItem deletedCourse = notiArrayList.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
                notiArrayList.remove(viewHolder.getAdapterPosition());

                notiAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                DatabaseReference databaseReference = firebaseDatabase.getReference("Notification/"+userId);
                //databaseReference.child("Test");
                // Lấy dữ liệu từ nút "us1"
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DatabaseReference m2Reference = databaseReference.child(deletedCourse.getKey());
                        Log.d("Xoa thong bao",deletedCourse.getUserid());
                        m2Reference.removeValue();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra
                        System.out.println("Error: " + databaseError.getMessage());
                    }
                });

                Snackbar.make(notiRV, "You've just deleted a notification", Snackbar.LENGTH_LONG).show();
            }
            // at last we are adding this
            // to our recycler view.
            @Override
            public void onChildDraw (@NonNull Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(NotificationActivity.this, R.color.red))
                        .addActionIcon(R.drawable.baseline_delete_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(notiRV);
    }


    }
