package com.main.activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class Story extends AppCompatActivity {

    private ProgressBar timeProgressBar;
    private static final int PROGRESS_MAX = 5000; // 5000 milliseconds (5 seconds)
    private static final int PROGRESS_INTERVAL = 10; // Update interval for the progress bar animation (in milliseconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.myapplication.R.layout.story);

        timeProgressBar = findViewById(R.id.timeProgressBar);

        // Start the progress bar animation
        startProgressBarAnimation();
    }
    private void startProgressBarAnimation() {
        // Create a new Handler on the main (UI) thread
        Handler mainHandler = new Handler();

        // Start a new thread to update the progress bar
        new Thread(() -> {
            // Calculate the progress increment for each interval
            int progressIncrement = PROGRESS_MAX / (PROGRESS_MAX / PROGRESS_INTERVAL);

            // Incrementally update the progress bar from 0 to max
            for (int progress = 0; progress <= PROGRESS_MAX; progress += progressIncrement) {
                // Post the progress update to the main thread
                int finalProgress = progress;
                mainHandler.post(() -> updateProgressBar(finalProgress));

                // Pause for a short time to simulate progress animation
                try {
                    Thread.sleep(PROGRESS_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateProgressBar(int progress) {
        // Update the progress of the ProgressBar
        timeProgressBar.setProgress(progress);
    }

}