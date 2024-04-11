package com.main.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.adapters.MatchesAdapter;
import com.main.adapters.NotificationAdapter;
import com.main.adapters.Subscription_Adapter;
import com.main.entities.MatchesItem;
import com.main.entities.NotificationItem;
import com.main.entities.SubscriptionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subscription_Activity extends AppCompatActivity {
    private EditText txtsearch,title,StartDate,EndDate,Note;
    private ListView listView;
    private Button btn1;
    Dialog mDialog;
    private ImageView backBtn;
    String userId="us1";
    Subscription_Adapter adapter;
    private List<SubscriptionItem> subscriptionItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_subscription);

        txtsearch=(EditText) findViewById(R.id.SearchBar);
        listView=(ListView) findViewById(R.id.grid_sub);
        btn1=(Button) findViewById(R.id.addSub);
        backBtn=(ImageView) findViewById(R.id.back_arrow);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Subscription_Activity.this, ProfileMainActivity.class);
            startActivity(intent);
            finish();
        });
        mDialog=new Dialog(this);

        subscriptionItemList=new ArrayList<>();
        /*
        subscriptionItemList.add(new SubscriptionItem("First Date", "Free","12/04/2020","28/12/2022"));
        subscriptionItemList.add(new SubscriptionItem("Love Date", "Free","12/04/2020","28/12/2022"));*/
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Subscription/"+userId);
        //databaseReference.child("Test");

        // Lấy dữ liệu từ nút "us1"

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);

                //System.out.println("Loading data: "+value);
                if (dataSnapshot.exists()) {

                    subscriptionItemList.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        String key = snap.getKey();
                        //MatchesItem matchesItem = snap.getValue(MatchesItem.class);
                        String title = snap.child("title").getValue(String.class);
                        String plan = snap.child("plan").getValue(String.class);
                        String startDate = snap.child("startDate").getValue(String.class);
                        String endDate = snap.child("endDate").getValue(String.class);
                        SubscriptionItem subscriptionItem= new SubscriptionItem(title,plan,startDate,endDate,key);

                        subscriptionItemList.add(subscriptionItem);
                    }
                    adapter = new Subscription_Adapter(Subscription_Activity.this, subscriptionItemList);
                    listView.setAdapter(adapter);
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


        adapter= new Subscription_Adapter(this,subscriptionItemList);
        listView.setAdapter(adapter);
         btn1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 showCreateDialog();
             }
         });


    }
    private void showCreateDialog() {
        // Thiết lập layout cho Dialog
        mDialog.setContentView(R.layout.add_newsub);
        mDialog.show();
        Button btnSaveDialog = mDialog.findViewById(R.id.save_button);
        Button btnExitDialog= mDialog.findViewById(R.id.cancel_button);
        title=mDialog.findViewById(R.id.Title);
        StartDate=mDialog.findViewById(R.id.StartDate);
        EndDate=mDialog.findViewById(R.id.EndDate);
        Note=mDialog.findViewById(R.id.Note);

        btnSaveDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_Sub();
                mDialog.dismiss();
            }
        });

        btnExitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        // Hiển thị Dialog
        mDialog.show();
    }

    private void Create_Sub()
    {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Subscription/"+userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the number of children (current data size)
                long count = dataSnapshot.getChildrenCount();

                // Create a new subscription entry with the incremented key
                DatabaseReference newSubscriptionRef = databaseReference.child(String.valueOf(count));

                // Set the values for the new subscription entry
                Map<String, Object> newSubscriptionValues = new HashMap<>();
                newSubscriptionValues.put("endDate", EndDate.getText().toString());
                newSubscriptionValues.put("plan", Note.getText().toString());
                newSubscriptionValues.put("startDate", StartDate.getText().toString());
                newSubscriptionValues.put("title", title.getText().toString());

                // Update the database with the new subscription entry
                newSubscriptionRef.setValue(newSubscriptionValues)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Subscription successfully created
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

}
