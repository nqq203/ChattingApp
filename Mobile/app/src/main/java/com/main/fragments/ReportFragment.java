package com.main.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class ReportFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Report User");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.report_fragment, null);

        Button submitButton = dialogView.findViewById(R.id.submit_report_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_report_button);
        EditText reportEditText = dialogView.findViewById(R.id.report_edit_text);

        submitButton.setOnClickListener(v -> {
            String reportText = reportEditText.getText().toString().trim();
            // Xử lý việc gửi báo cáo ở đây, ví dụ lưu vào database hoặc gửi qua API
            Toast.makeText(getActivity(), "Report submitted!", Toast.LENGTH_SHORT).show();
            // Đóng fragment sau khi gửi báo cáo
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

        builder.setView(dialogView);
        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
