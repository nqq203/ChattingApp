package com.main.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.activities.ChatActivity;
import com.main.activities.MessageActivity;
import com.main.activities.UserSessionManager;

public class BlockUserDialogFragment extends DialogFragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private UserSessionManager sessionManager;
    private static final String ARG_GUEST_PHONE = "guestPhone";

    public static BlockUserDialogFragment newInstance(String guestPhone) {
        BlockUserDialogFragment fragment = new BlockUserDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GUEST_PHONE, guestPhone);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.dialog_block_user, null);
        Button btnBlock = view.findViewById(R.id.btn_block);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        btnBlock.setOnClickListener(v -> {
            // Handle block logic here
            final String guestPhone = getArguments().getString("guestPhone");
            onBlockUser(guestPhone);
            dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            // Dismiss dialog
            dismiss();
        });

        builder.setView(view)
                .setTitle("You can't unblock he/she! Are you sure to block this user? ");

        return builder.create();
    }

    private void onBlockUser(String userId) {
        sessionManager = new UserSessionManager(getContext());
        String myPhone = sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);

        databaseReference.child("User").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("fullname").getValue(String.class);
                // Logic to block the user
                databaseReference.child("BlockList").child(myPhone).child(userId).setValue(userName);
                databaseReference.child("Message").child(myPhone).child(userId).removeValue();
                databaseReference.child("Message").child(userId).child(myPhone).removeValue();
                databaseReference.child("Matches").child(myPhone).child(userId).removeValue();
                databaseReference.child("Matches").child(userId).child(myPhone).removeValue();
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
