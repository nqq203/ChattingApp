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
import com.main.activities.XemHobbiesActivity;

public class InfoDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Đặt tiêu đề cho Dialog
        builder.setTitle("Options");

        // Sử dụng LayoutInflater để inflate custom layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.closefriend_options_diaglog, null);

        // Lấy tham chiếu đến Switch và thiết lập trạng thái ban đầu dựa trên SharedPreferences
        Switch closeFriendSwitch = dialogView.findViewById(R.id.switch_close_friend);
        SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        closeFriendSwitch.setChecked(prefs.getBoolean("CloseFriendEnabled", false));

        closeFriendSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Lưu trạng thái mới vào SharedPreferences
            prefs.edit().putBoolean("CloseFriendEnabled", isChecked).apply();
        });

        // Thêm danh sách lựa chọn
        builder.setItems(new String[]{"Report", "Unmatch", "Sharing Hobbies", "Change Theme Color"}, new DialogInterface.OnClickListener() {
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
                        // Unmatch
                        // Get list friend from database then remove it from list user
                        break;
                    case 2:
                        // Sharing Hobbies
                        Intent intentHobbies = new Intent(getActivity(), XemHobbiesActivity.class);
                        startActivity(intentHobbies);
                        break;
                    case 3:
                        dismiss();
                        ColorPickerDialogFragment newFragment = new ColorPickerDialogFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "INFO_DIALOG");
                        break;
                }
            }
        });
        builder.setView(dialogView);

        return builder.create();
    }

    private void toggleCloseFriend() {
        SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean isCloseFriendEnabled = prefs.getBoolean("CloseFriendEnabled", false); // Mặc định là false
        // Toggle giá trị
        prefs.edit().putBoolean("CloseFriendEnabled", !isCloseFriendEnabled).apply();

        // Cập nhật UI hoặc logic tại đây nếu cần
        if (isCloseFriendEnabled) {
            Log.d("InfoDialogFragment", "Close Friend disabled");
            // Thực hiện hành động khi tắt
        } else {
            Log.d("InfoDialogFragment", "Close Friend enabled");
            // Thực hiện hành động khi bật
        }
    }
}

