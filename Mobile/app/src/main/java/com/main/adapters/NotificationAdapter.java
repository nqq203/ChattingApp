/*package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.entities.NotificationItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends ArrayAdapter<NotificationItem> {
    public NotificationAdapter(Context context, List<NotificationItem> notification) {
        super(context, 0, notification);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationItem notification = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_item, parent, false);
        }
        TextView Description_Text = (TextView) convertView.findViewById(R.id.noti_descrip);
        CircleImageView ProfilePic=(CircleImageView) convertView.findViewById(R.id.Profile_Pic);
        ImageView PicStory=(ImageView) convertView.findViewById(R.id.pic_story);
        TextView StoryMess = (TextView) convertView.findViewById(R.id.story_mess);
        TextView TimeNoti = (TextView) convertView.findViewById(R.id.time_noti);

        Description_Text.setText(notification.getDescription());
        ProfilePic.setImageResource(notification.getProfile_pic());

        if(notification.getStory_pic()!=0) {
        PicStory.setImageResource(notification.getStory_pic());
        } else
        {
            PicStory.setVisibility(View.GONE);
        }
        if(notification.getReply_story()!=null) {
            StoryMess.setText(notification.getReply_story());
        } else
        {
            StoryMess.setVisibility(View.GONE);
        }
        TimeNoti.setText(notification.getTime());




        return convertView;
    }
}*/
package com.main.adapters;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.activities.ChatActivity;
import com.main.activities.UserSessionManager;
import com.main.entities.NotificationItem;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.cache.DiskLruCache;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecyclerViewHolder> {
    private ArrayList<NotificationItem> NotiDataArrayList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private Context mcontext;
    String userId,profile_pic,fullname;
    FirebaseDatabase firebaseDatabase1=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");

    
    public NotificationAdapter(ArrayList<NotificationItem> recyclerDataArrayList, Context mcontext) {
        this.NotiDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;

        UserSessionManager sessionManager = new UserSessionManager(mcontext);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        userId = userDetails.get(UserSessionManager.KEY_PHONE_NUMBER);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview from our modal class.
        NotificationItem recyclerData = NotiDataArrayList.get(position);
        DatabaseReference databaseReference1 = firebaseDatabase1.getReference("User/"+recyclerData.getUserid());
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    profile_pic = dataSnapshot.child("imageUrl").getValue(String.class);
                    fullname=dataSnapshot.child("fullname").getValue(String.class);
                    Glide.with(mcontext)
                            .load(profile_pic)
                            .into(holder.ProfilePic);
                }
                else {
                    System.out.println("Không tìm thấy dữ liệu cho người dùng có ID: " + userId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                System.out.println("Error: " + databaseError.getMessage());
            }
        });



        holder.Description_Text.setText(recyclerData.getDescription());



        if(!recyclerData.getStory_pic().isEmpty()) {
            Log.d("NULL TAAAAAAAAAAAAA", "RUNNING RN222222");
            //holder.PicStory.setImageResource(recyclerData.getStory_pic());
            Glide.with(mcontext)
                    .load(recyclerData.getStory_pic())
                    .into(holder.PicStory);
        } else
        {
            holder.PicStory.setVisibility(View.GONE);
        }


        if(!recyclerData.getReply_story().isEmpty()) {
            Log.d("NULL TAAAAAAAAAAAAA", "RUNNING RN2222222222222222");
            holder.StoryMess.setText(recyclerData.getReply_story());
        } else
        {
            holder.StoryMess.setVisibility(View.GONE);
        }
        holder.TimeNoti.setText(recyclerData.getTime());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect_Chat(recyclerData.getUserid(),profile_pic,fullname);
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method returns
        // the size of recyclerview
        return NotiDataArrayList.size();
    }
    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        // creating a variable for our text view.
        TextView TimeNoti;
        TextView StoryMess;
        ImageView PicStory;
        CircleImageView ProfilePic;
        TextView Description_Text;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
             Description_Text = (TextView) itemView.findViewById(R.id.noti_descrip);
             ProfilePic=(CircleImageView) itemView.findViewById(R.id.Profile_Pic);
             PicStory=(ImageView) itemView.findViewById(R.id.pic_story);
             StoryMess = (TextView) itemView.findViewById(R.id.story_mess);
             TimeNoti = (TextView) itemView.findViewById(R.id.time_noti);
        }
    }

    public void redirect_Chat(String guestId,String profile_pic,String fullname)
    {
        String chatKey;
        DatabaseReference dataref=databaseReference.child("Chat");
        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("UserId",fullname);
                Log.d("GUESTId",profile_pic);
                if(snapshot.child(userId+"_"+guestId).hasChild("user1"))
                {
                    Intent intent =new Intent(mcontext, ChatActivity.class);
                    intent.putExtra("chatKey",userId+"_"+guestId);
                    intent.putExtra("fullname",fullname);
                    intent.putExtra("imageUrl",profile_pic);
                    mcontext.startActivity(intent);

                }
                else if (snapshot.child(guestId+"_"+userId).hasChild("user1"))
                {
                    Intent intent =new Intent(mcontext, ChatActivity.class);
                    intent.putExtra("chatKey",guestId+"_"+userId);
                    intent.putExtra("fullname",fullname);
                    intent.putExtra("imageUrl",profile_pic);
                    mcontext.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}