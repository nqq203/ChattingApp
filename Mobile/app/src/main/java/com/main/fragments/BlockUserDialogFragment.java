package com.main.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
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
import com.main.activities.UserSessionManager;

public class BlockUserDialogFragment extends DialogFragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private UserSessionManager sessionManager;
    public static BlockUserDialogFragment  newInstance() {
        return new BlockUserDialogFragment();
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
            onBlockUser("abc");
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

        // Logic to block the user
        databaseReference.child("BlockList").child(myPhone).child(userId).setValue("block");
    }
}
