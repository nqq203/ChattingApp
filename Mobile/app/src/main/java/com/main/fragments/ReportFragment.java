package com.main.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.activities.MessageActivity;
import com.main.activities.UserSessionManager;

import java.util.HashMap;

public class ReportFragment extends DialogFragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private UserSessionManager sessionManager;
    private static final String ARG_GUEST_PHONE = "guestPhone";
    public static ReportFragment newInstance(String guestPhone) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GUEST_PHONE, guestPhone);
        fragment.setArguments(args);
        return fragment;
    }
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
            final String guestPhone = getArguments().getString("guestPhone");
            Toast.makeText(getActivity(), "Report submitted!", Toast.LENGTH_SHORT).show();
            onRpUser(guestPhone, reportText);
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
    private void onRpUser(String userId, String description) {
        sessionManager = new UserSessionManager(getContext());
        String myPhone = sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);

        // Tạo một khóa duy nhất cho mỗi báo cáo
        String reportKey = databaseReference.child("Report").push().getKey();

        // Tạo một HashMap để lưu trữ thông tin báo cáo
        HashMap<String, Object> reportData = new HashMap<>();
        reportData.put("Sender", myPhone);
        reportData.put("Receiver", userId);
        reportData.put("Description", description);

        // Ghi thông tin báo cáo vào cơ sở dữ liệu tại vị trí được chỉ định bởi reportKey
        databaseReference.child("Report").child(reportKey).setValue(reportData)
                .addOnSuccessListener(aVoid -> {
                    // Xử lý thành công khi báo cáo được ghi vào cơ sở dữ liệu
                    Toast.makeText(getActivity(), "Report submitted!", Toast.LENGTH_SHORT).show();
                    // Tiến hành các hành động tiếp theo sau khi ghi báo cáo thành công, ví dụ: chuyển đến một Activity khác
                    Intent intent = new Intent(getActivity(), MessageActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi có lỗi xảy ra trong quá trình ghi báo cáo vào cơ sở dữ liệu
                    Toast.makeText(getActivity(), "Failed to submit report!", Toast.LENGTH_SHORT).show();
                });
    }


}
