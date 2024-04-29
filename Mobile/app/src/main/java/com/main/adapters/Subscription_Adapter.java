package com.main.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private Context mContext;
    private Dialog mDialog;
    String userId="us1";
    SubscriptionItem Subscription;

    public Subscription_Adapter (Context context, List<SubscriptionItem> Subscription)
    {
        super(context,0,Subscription);
        mContext = context;
        mDialog = new Dialog(mContext);
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
        return listitemView;
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
        //databaseReference.child("Test");
        // Lấy dữ liệu từ nút "us1"
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