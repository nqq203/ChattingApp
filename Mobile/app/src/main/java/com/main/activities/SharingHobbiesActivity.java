package com.main.activities;
import static com.main.fragments.TextViewHobbiesFragment.newInstance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.GridView;
import android.widget.TextView;
import android.view.View;

import com.group4.matchmingle.R;
import com.main.adapters.HobbiesAdapter;
import com.main.callbacks.MainCallbacks;
import com.main.entities.HobbiesItem;
import com.main.fragments.GridHobbiesFragments;
import com.main.fragments.TextViewHobbiesFragment;


import java.util.ArrayList;

public class SharingHobbiesActivity extends FragmentActivity implements MainCallbacks {

    FragmentTransaction ft;
    GridHobbiesFragments GridHobbies;
    TextViewHobbiesFragment TextViewHobbies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_sharing_hobbies);

        ft = getSupportFragmentManager().beginTransaction();
        GridHobbies = GridHobbiesFragments.newInstance("first-list");
        ft.replace(R.id.frame_list_fragment, GridHobbies);
        ft.commit();

        ft = getSupportFragmentManager().beginTransaction();
        TextViewHobbies = newInstance("first-detail");
        ft.replace(R.id.frame_detail_fragment, TextViewHobbies);
        ft.commit();
    }
    public void onMsgFromFragToMain (String sender, String hobby) {
        TextViewHobbies.onMsgFromMainToFragment(sender, hobby);
        GridHobbies.onMsgFromMainToFragment(sender,hobby);
    }
}