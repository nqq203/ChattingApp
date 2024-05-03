package com.main.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group4.matchmingle.R;

public class ColorPickerDialogFragment extends DialogFragment {

    public interface ColorPickerDialogListener {
        void onColorSelected(String color);
    }

    private ColorPickerDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ColorPickerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ColorPickerDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Change Theme Color");
        builder.setItems(new String[]{"Blue", "Orange", "Purple"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        notifyColorSelected("blue");
                        break;
                    case 1:
                        notifyColorSelected("orange");
                        break;
                    case 2:
                        notifyColorSelected("purple");
                        break;
                }
            }
        });

        return builder.create();
    }

    private void notifyColorSelected(String color) {
        if (listener != null) {
            listener.onColorSelected(color);
        }
        dismiss();
    }
}

