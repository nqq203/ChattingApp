package com.main.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.group4.matchmingle.R;
import com.main.adapters.Subscription_Adapter;
import com.main.entities.SubscriptionItem;

import java.util.ArrayList;
import java.util.List;

public class Subscription_Activity extends AppCompatActivity {
    private EditText txtsearch;
    private ListView listView;
    private Button btn1;
    Dialog mDialog;
    private ImageView backBtn;
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
        subscriptionItemList.add(new SubscriptionItem("First Date", "Free","12/04/2020","28/12/2022"));
        subscriptionItemList.add(new SubscriptionItem("Love Date", "Free","12/04/2020","28/12/2022"));

        Subscription_Adapter adapter= new Subscription_Adapter(this,subscriptionItemList);
        listView.setAdapter(adapter);
         btn1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mDialog.setContentView(R.layout.add_newsub);
                 mDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                 mDialog.show();
             }
         });

    }

}
