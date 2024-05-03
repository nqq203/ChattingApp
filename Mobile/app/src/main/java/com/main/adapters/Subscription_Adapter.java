package com.main.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.entities.SubscriptionItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subscription_Adapter extends ArrayAdapter<SubscriptionItem> {
    private Context mContext,UpContext;
    private EditText titleEditText,StartDateEditText,EndDateEditText,NoteEditText;
    private Dialog mDialog,UpdateDialog;
    String userId="us1";
    SubscriptionItem Subscription;

    public Subscription_Adapter (Context context, List<SubscriptionItem> Subscription)
    {
        super(context,0,Subscription);
        mContext = context;
        UpContext = context;
        mDialog = new Dialog(mContext);
        UpdateDialog=new Dialog(UpContext);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listitemView = convertView;
        Subscription=getItem(position);
        if(listitemView==null)
        {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.subscrip_item,parent,false);
        }
        TextView title=(TextView) listitemView.findViewById(R.id.Sub_title);
        TextView plan=(TextView) listitemView.findViewById(R.id.cur_plan);
        TextView timeper=(TextView) listitemView.findViewById(R.id.time_plan);
        Button but_Un=(Button) listitemView.findViewById(R.id.but_Un);
        Button but_Up=(Button) listitemView.findViewById(R.id.but_Up);
        title.setText(Subscription.getTitle());
        plan.setText(Subscription.getPlan());
        timeper.setText(Subscription.getStartDate()+" - "+Subscription.getEndDate());


        but_Un.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
        but_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowUpdateDialog();
            }
        });
        return listitemView;
    }


    private void ShowUpdateDialog()
    {
        UpdateDialog.setContentView(R.layout.add_newsub);
        Button btnSaveDialog = UpdateDialog.findViewById(R.id.save_button);
        Button btnExitDialog= UpdateDialog.findViewById(R.id.cancel_button);
        titleEditText=(EditText) UpdateDialog.findViewById(R.id.Title);
        StartDateEditText=(EditText)UpdateDialog.findViewById(R.id.StartDate);
        EndDateEditText=(EditText)UpdateDialog.findViewById(R.id.EndDate);
        NoteEditText=(EditText)UpdateDialog.findViewById(R.id.Note);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Subscription/"+userId+"/"+Subscription.getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);
                //System.out.println("Loading data: "+value);
                //String key = dataSnapshot.getKey();
                if (dataSnapshot.exists()) {
                    //MatchesItem matchesItem = snap.getValue(MatchesItem.class);
                    String endDate = dataSnapshot.child("endDate").getValue(String.class);
                    String plan = dataSnapshot.child("plan").getValue(String.class);
                    String startDate = dataSnapshot.child("startDate").getValue(String.class);
                    String title = dataSnapshot.child("title").getValue(String.class);
                    Log.d("endDate",endDate);
                    Log.d("endDate",plan);
                    Log.d("endDate",startDate);
                    Log.d("endDate",title);
                    titleEditText.setText(title);
                    StartDateEditText.setText(startDate);
                    NoteEditText.setText(plan);
                    EndDateEditText.setText(endDate);
                }
                else {
                    Log.d("UserData", "No data exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
        btnExitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDialog.dismiss();
            }
        });
        btnSaveDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String endDate = EndDateEditText.getText().toString();
                String plan = NoteEditText.getText().toString();
                String startDate = StartDateEditText.getText().toString();
                String title = titleEditText.getText().toString();
                databaseReference.child("title").setValue(title);
                databaseReference.child("startDate").setValue(startDate);
                databaseReference.child("plan").setValue(plan);
                databaseReference.child("endDate").setValue(endDate);

                UpdateDialog.dismiss();
            }
        });
        UpdateDialog.show();

    }
    private void showDeleteDialog() {
        // Thiết lập layout cho Dialog
        mDialog.setContentView(R.layout.deletesub);


        Button btnDeleteDialog = mDialog.findViewById(R.id.dialog_delete_button);
        Button btnExitDialog= mDialog.findViewById(R.id.cancel_button);
        btnDeleteDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_matches(Subscription.getUserId());
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
    private void delete_matches(String userId_Delete)
    {
        Log.d("Delete",userId_Delete);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Subscription/"+userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference m2Reference = databaseReference.child(userId_Delete);
                m2Reference.removeValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }
}
