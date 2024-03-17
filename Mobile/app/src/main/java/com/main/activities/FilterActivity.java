package com.main.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class FilterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
        // Thiết lập vị trí của window
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // Ví dụ: đặt activity ở trung tâm màn hình
        params.gravity = Gravity.TOP;
        // Áp dụng các thay đổi về thuộc tính
        window.setAttributes(params);

        ImageView backBtn = findViewById(R.id.icon_back);
        TextView checkBtn = findViewById(R.id.icon_check);
        Spinner distanceSpinner = (Spinner) findViewById(R.id.distance_spinner);
        ArrayAdapter<CharSequence> distanceChoicesAdapter = ArrayAdapter.createFromResource(this, R.array.distance_spinner_items, android.R.layout.simple_spinner_item);
        distanceChoicesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(distanceChoicesAdapter);

        backBtn.setOnClickListener(v -> {
            finish();
        });

        checkBtn.setOnClickListener(v -> {
            finish();
        });
    }
}
