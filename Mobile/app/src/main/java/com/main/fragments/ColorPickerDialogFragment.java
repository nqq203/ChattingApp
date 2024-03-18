package com.main.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class ColorPickerDialogFragment extends DialogFragment {

    public interface ColorPickerDialogListener {
        void onColorSelected(int color);
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
        builder.setItems(new String[]{"Blue", "White", "Purple"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        notifyColorSelected(getResources().getColor(R.color.blue_1));
                        break;
                    case 1:
                        notifyColorSelected(getResources().getColor(R.color.white));
                        break;
                    case 2:
                        notifyColorSelected(getResources().getColor(R.color.purple_1));
                        break;
                }
            }
        });

        return builder.create();
    }

    private void notifyColorSelected(int color) {
        if (listener != null) {
            listener.onColorSelected(color);
        }
        dismiss();
    }
}

