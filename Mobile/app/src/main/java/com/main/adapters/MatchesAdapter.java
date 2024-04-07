package com.main.adapters;

import android.app.Dialog;
import android.util.Log;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.activities.MatchesActivity;
import com.main.entities.MatchesItem;

import java.util.List;

public class MatchesAdapter extends ArrayAdapter<MatchesItem> {
    private Context mContext;
    private Dialog mDialog;
    Dialog DeleteMatch;
    MatchesItem matchesItem;
    String userId="us1";
    public MatchesAdapter(@NonNull Context context, List<MatchesItem> customer) {
        super(context, 0, customer);
        mContext = context;
        mDialog = new Dialog(mContext);

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull  ViewGroup parent) {
        matchesItem = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_pic, parent, false);
        }
        TextView IdText = (TextView) convertView.findViewById(R.id.txtView_NameAge);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.Mtch_Pic);
        Button btndelete=(Button) convertView.findViewById(R.id.btn_delete);
        Button btnchat=(Button) convertView.findViewById(R.id.btn_msg);

        IdText.setText(matchesItem.getName()+", "+matchesItem.getAge());
        //imageView.setBackgroundResource(matchesItem.getPic());
        Glide.with(getContext())
                .load(matchesItem.getPic())
                .into(imageView);

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                /*
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference databaseReference = firebaseDatabase.getReference("Matches/"+userId);
                //databaseReference.child("Test");

                // Lấy dữ liệu từ nút "us1"
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DatabaseReference m2Reference = databaseReference.child(matchesItem.getUserid());
                        m2Reference.removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra
                        System.out.println("Error: " + databaseError.getMessage());
                    }
                });*/
            }
        });
        return convertView;
    }
    private void showDialog() {
        // Thiết lập layout cho Dialog
        mDialog.setContentView(R.layout.deletepopup);
        Button btnDeleteDialog = mDialog.findViewById(R.id.dialog_delete_button);
        Button btnExitDialog= mDialog.findViewById(R.id.cancel_button);
        btnDeleteDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_matches(matchesItem.getUserid());
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
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Matches/"+userId);
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
