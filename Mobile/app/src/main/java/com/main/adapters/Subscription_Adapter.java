package com.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.group4.matchmingle.R;
import com.main.entities.SubscriptionItem;

import java.util.List;

public class Subscription_Adapter extends ArrayAdapter<SubscriptionItem> {
    public Subscription_Adapter (Context context, List<SubscriptionItem> Subscription)
    {
        super(context,0,Subscription);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listitemView = convertView;
        SubscriptionItem Subscription=getItem(position);
        if(listitemView==null)
        {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.subscrip_item,parent,false);
        }
        TextView title=(TextView) listitemView.findViewById(R.id.Sub_title);
        TextView plan=(TextView) listitemView.findViewById(R.id.cur_plan);
        TextView timeper=(TextView) listitemView.findViewById(R.id.time_plan);
        title.setText(Subscription.getTitle());
        plan.setText(Subscription.getPlan());
        timeper.setText(Subscription.getStartDate()+" - "+Subscription.getEndDate());

        return listitemView;
    }
}
