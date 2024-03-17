package com.example.myapplication;

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
}