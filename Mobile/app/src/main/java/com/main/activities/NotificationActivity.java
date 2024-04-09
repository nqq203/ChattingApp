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
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.android.material.snackbar.Snackbar;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class NotificationActivity extends AppCompatActivity {
    // creating a variable for recycler view,
    // array list and adapter class.
    String userId="us1";
    private RecyclerView notiRV;
    private ArrayList<NotificationItem> notiArrayList;
    private NotificationAdapter notiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_noti);

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
                        int key1=Integer.parseInt(key);
                        //MatchesItem matchesItem = snap.getValue(MatchesItem.class);
                        String Description = snap.child("Description").getValue(String.class);
                        String Reply_Story = snap.child("Reply_Story").getValue(String.class);
                        String Time = snap.child("Time").getValue(String.class);
                        String Type = snap.child("Type").getValue(String.class);
                        String profile_pic = snap.child("profile_pic").getValue(String.class);
                        String story_pic = snap.child("story_pic").getValue(String.class);
                        NotificationItem notificationItem= new NotificationItem(Description,profile_pic,Type,Reply_Story,Time,story_pic,key1);
                        System.out.println(key+"TEN NE HUHUHUHUHUHUHU");
                        Log.d("MyActivity", "Description: " + notificationItem.toString());
                        
                        notiArrayList.add(notificationItem);
                    }
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
        // in below line we are adding data to our array list.
        /*
        notiArrayList.add(new NotificationItem("Dennis Nerdy just replied on your story",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTF2tbJGtc87FhbEROZ8u-Q-01Z3oDBt1J3ww&usqp=CAU","Story" ,"Xinh thế",
                "Last Wednesday at 9:42 AM", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTF2tbJGtc87FhbEROZ8u-Q-01Z3oDBt1J3ww&usqp=CAU",1));
        notiArrayList.add(new NotificationItem("Its a match you just matched with Henry Cavill",
                "https://preview.redd.it/230812-danielle-phoning-photo-update-v0-det75s625ohb1.jpg?width=640&crop=smart&auto=webp&s=6617d49f38832c201f9659cc8759d5fc8a5595f1"
                , "Normal",null,"Last Wednesday at 9:42 AM",null,1));
        notiArrayList.add(new NotificationItem("Dennis Nerdy just like your story",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTF2tbJGtc87FhbEROZ8u-Q-01Z3oDBt1J3ww&usqp=CAU","Story" ,null,
                "Last Wednesday at 9:42 AM", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTF2tbJGtc87FhbEROZ8u-Q-01Z3oDBt1J3ww&usqp=CAU",3));
        */
        // initializing our adapter class with our array list and context.
        notiAdapter = new NotificationAdapter(notiArrayList, this);

        // below line is to set layout manager for our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);

        // setting layout manager for our recycler view.
        notiRV.setLayoutManager(manager);

        // below line is to set adapter
        // to our recycler view.
        notiRV.setAdapter(notiAdapter);

        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                NotificationItem deletedCourse = notiArrayList.get(viewHolder.getAdapterPosition());
                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();
                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                notiArrayList.remove(viewHolder.getAdapterPosition());
                // below line is to notify our item is removed from adapter.
                notiAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // below line is to display our snackbar with action.
                Snackbar.make(notiRV, "You've just deleted a notification", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        notiArrayList.add(position, deletedCourse);

                        // below line is to notify item is
                        // added to our adapter class.
                        notiAdapter.notifyItemInserted(position);
                    }
                }).show();
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
