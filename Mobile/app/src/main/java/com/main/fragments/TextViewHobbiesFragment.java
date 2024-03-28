package com.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.main.activities.SharingHobbiesActivity;
import com.main.callbacks.FragmentCallbacks;
import com.main.callbacks.MainCallbacks;

public class TextViewHobbiesFragment extends Fragment implements FragmentCallbacks {

    SharingHobbiesActivity main;
    TextView HobbiesText;

    public static TextViewHobbiesFragment newInstance(String strArg1) {
        TextViewHobbiesFragment fragment = new TextViewHobbiesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("arg1", strArg1);
        fragment.setArguments(bundle);
        return fragment;
    }
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);

        if (!((getActivity()) instanceof MainCallbacks)) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
        main = (SharingHobbiesActivity) getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View viewDetail = inflater.inflate(R.layout.grid_hobbies_textview, container, false);
        HobbiesText=(TextView) viewDetail.findViewById(R.id.HobbiesText);

        return viewDetail;
    }
    public void onMsgFromMainToFragment(String context, String hobby) { //msg từ main đến frag //nhận
        String allHobbies= HobbiesText.getText().toString();

        if(context.equals("delete")) {

            String toRemove=hobby+", ";

            if(allHobbies.contains(toRemove) ){
                allHobbies=allHobbies.replaceAll(toRemove,"");

                HobbiesText.setText(allHobbies);
            }
            else if (allHobbies.contains(hobby))
            {
                allHobbies=allHobbies.replaceAll(hobby,"");
                HobbiesText.setText(allHobbies);
            }

        }
        else if(context.equals("add")) {
            if(allHobbies.equals("") || allHobbies.charAt(allHobbies.length() - 1) == ' ') {
                allHobbies=allHobbies+hobby;
                HobbiesText.setText(allHobbies);
            }
            else
            {
                allHobbies=allHobbies+", "+hobby;
                HobbiesText.setText(allHobbies);
            }

        }
    }
}
