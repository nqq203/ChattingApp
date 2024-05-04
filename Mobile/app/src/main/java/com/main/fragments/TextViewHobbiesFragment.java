package com.main.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.activities.MatchesActivity;
import com.main.activities.SharingHobbiesActivity;
import com.main.activities.UserSessionManager;
import com.main.adapters.MatchesAdapter;
import com.main.callbacks.FragmentCallbacks;
import com.main.callbacks.MainCallbacks;
import com.main.entities.MatchesItem;

import java.util.HashMap;

public class TextViewHobbiesFragment extends Fragment implements FragmentCallbacks {

    SharingHobbiesActivity main;
    TextView HobbiesText;
    String userId;
    Button ShareHobby;

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
        ShareHobby=(Button) viewDetail.findViewById(R.id.Shared_hobby);
        FirebaseDatabase firebaseDatabase1=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");

        UserSessionManager sessionManager = new UserSessionManager(getContext());
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        userId = userDetails.get(UserSessionManager.KEY_PHONE_NUMBER);

        DatabaseReference databaseReference1 = firebaseDatabase1.getReference("Hobbies/"+userId);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                        String allHobby = dataSnapshot.child("AllHobby").getValue(String.class);
                        Log.d("All HOBBY",allHobby);
                        HobbiesText.setText(allHobby);
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
        ShareHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase1=FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference databaseReference1 = firebaseDatabase1.getReference("Hobbies/"+userId);

                String hobbyentered=HobbiesText.getText().toString();
                databaseReference1.child("AllHobby").setValue(hobbyentered);

                main.onMsgFromFragToMain("push_Hobby", "push_Hobby");

            }
        });
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
            else if (allHobbies.contains(", "+hobby))
            {
                allHobbies=allHobbies.replaceAll(", "+hobby,"");
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
