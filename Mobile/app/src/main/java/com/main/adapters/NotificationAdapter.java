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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.main.entities.NotificationItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecyclerViewHolder> {
    private ArrayList<NotificationItem> NotiDataArrayList;
    private Context mcontext;

    // creating a constructor class.
    public NotificationAdapter(ArrayList<NotificationItem> recyclerDataArrayList, Context mcontext) {
        this.NotiDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
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

        holder.Description_Text.setText(recyclerData.getDescription());
        holder.ProfilePic.setImageResource(recyclerData.getProfile_pic());
        if(recyclerData.getStory_pic()!=0) {
            holder.PicStory.setImageResource(recyclerData.getStory_pic());
        } else
        {
            holder.PicStory.setVisibility(View.GONE);
        }
        if(recyclerData.getReply_story()!=null) {
            holder.StoryMess.setText(recyclerData.getReply_story());
        } else
        {
            holder.StoryMess.setVisibility(View.GONE);
        }
        holder.TimeNoti.setText(recyclerData.getTime());
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

}