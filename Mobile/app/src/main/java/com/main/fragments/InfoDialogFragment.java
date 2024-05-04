package com.main.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.group4.matchmingle.R;
import com.main.activities.SharingHobbiesActivity;

public class InfoDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Đặt tiêu đề cho Dialog
        builder.setTitle("Options");

        // Thêm danh sách lựa chọn
        builder.setItems(new String[]{"Report", "Sharing Hobbies", "Change Theme Color", "Block"}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý sự kiện click vào từng item
                // 'which' là vị trí của item trong danh sách
                switch (which) {
                    case 0:
                        dismiss();
                        ReportFragment reportFragment = new ReportFragment();
                        reportFragment.show(getActivity().getSupportFragmentManager(), "REPORT_DIALOG");
                        break;
                    case 1:
                        // Sharing Hobbies
                        Intent intentHobbies = new Intent(getActivity(), SharingHobbiesActivity.class);
                        startActivity(intentHobbies);
                        break;
                    case 2:
                        dismiss();
                        ColorPickerDialogFragment newFragment = new ColorPickerDialogFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "INFO_DIALOG");
                        break;
                    case 3:
                        dismiss();
                        BlockUserDialogFragment blockFragment = new BlockUserDialogFragment();
                        blockFragment.show(getActivity().getSupportFragmentManager(), "BLOCK_FRAGMENT");
                        break;
                }
            }
        });

        return builder.create();
    }
}

