
package com.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;


import com.group4.matchmingle.R;
import com.main.entities.Story;

import jp.shts.android.storiesprogressview.StoriesProgressView;
import com.squareup.picasso.Picasso;
public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener  {

    private StoriesProgressView storiesProgressView;
    private ImageView image;
    private TextView textViewName;

    private int counter = 0;
    private static final Story[] stories = new Story[]{
            new Story("Quynh Nga", "https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg", "1234", 4000L),
            new Story("Quynh Nga", "https://th.bing.com/th/id/OIP.BwEaQx16dGDo7uPa-ARwDgHaHZ?w=189&h=188&c=7&r=0&o=5&dpr=1.3&pid=1.7", "1234", 10000L),
            new Story("Quynh Nga", "https://th.bing.com/th/id/OIP.DaUck59gMGq5ESkNJ5lKhQAAAA?rs=1&pid=ImgDetMain", "1234", 1500L),
            new Story("Quynh Nga", "https://emanuelnine.epistles.faith/wp-content/uploads/emanuel-nine-tribute-portraits/Daniel-Simmons-Welcome-Judy-Takacs-810x1024.jpg", "1234", 4000L),
            new Story("Quynh Nga", "https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg", "1234", 5000L),
            new Story("Quynh Nga", "https://i.pinimg.com/originals/5e/67/fa/5e67fa0bcd0230fb933e9c7a6169e953.jpg", "1234", 1000L),
    };
    private static final int PROGRESS_COUNT = stories.length;

    long pressTime = 0L;
    long limit = 500L;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        storiesProgressView = findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoryDuration(stories[counter].getDuration());
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories();
        textViewName = findViewById(R.id.nameStory);
        textViewName.setText(stories[0].getFullname());
        image = findViewById(R.id.image);
        loadImageFromUrl(stories[counter].getImageUrl());

        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    private void loadImageFromUrl(String imageUrl) {
        Picasso.get().load(imageUrl).into(image);
    }

    @Override
    public void onNext() {
        if (counter < PROGRESS_COUNT - 1) {
            loadImageFromUrl(stories[++counter].getImageUrl());
            storiesProgressView.setStoryDuration(stories[counter].getDuration());
        } else {
            Intent intent = new Intent(StoryActivity.this, MessageActivity.class);
            // After the progress bar animation is complete, post a message to finish the activity
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        loadImageFromUrl(stories[--counter].getImageUrl());
        storiesProgressView.setStoryDuration(stories[counter].getDuration());
    }

    @Override
    public void onComplete() {
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }
}