
package com.main.activities;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.main.entities.Story;

import jp.shts.android.storiesprogressview.StoriesProgressView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener  {

    private StoriesProgressView storiesProgressView;
    private DatabaseReference databaseReference; // Sử dụng kết nối Firebase đã có
    private ImageView image;
    private TextView textViewName;

    private int counter = 0;
    private static Story[] stories;
    private static int PROGRESS_COUNT = 0;

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
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/"); // Sử dụng kết nối Firebase đã có

        storiesProgressView = findViewById(R.id.stories);
        textViewName = findViewById(R.id.nameStory);
        image = findViewById(R.id.image);

        // Lấy danh sách story từ Firebase và cập nhật mảng stories
        fetchStories(userID);

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

    private void fetchStories(String userID) {
        // Sử dụng kết nối Firebase đã có
        Query query = databaseReference.child("Story").child(userID).orderByChild("timeCreated");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Story> storyList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    // Lặp qua tất cả các story trong danh sách
                    for (DataSnapshot storySnapshot : dataSnapshot.getChildren()) {
                        // Lấy thông tin của story
                        long duration = storySnapshot.child("duration").getValue(long.class);
                        String image = storySnapshot.child("imageUrl").getValue(String.class);
                        Date date = storySnapshot.child("timeCreated").getValue(Date.class);
                        String name = storySnapshot.child("fullname").getValue(String.class);
                        Story story = new Story(duration, image, date, name);
                        // Thêm story vào danh sách
                        storyList.add(story);
                    }
                } else {
                    // Không có story nào
                }

                // Chuyển danh sách story sang mảng stories
                stories = storyList.toArray(new Story[0]);
                PROGRESS_COUNT = stories.length;
                Log.d("abc", String.valueOf(PROGRESS_COUNT));
                // Bắt đầu hiển thị câu chuyện đầu tiên
                startStory(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    private void startStory(int index) {
        counter = index;
        textViewName.setText(stories[counter].getFullname());
        loadImageFromUrl(stories[counter].getImageUrl());
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoryDuration(stories[counter].getDuration()*1000);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories();
    }

    private void loadImageFromUrl(String imageUrl) {
        Picasso.get().load(imageUrl).into(image);
    }

    @Override
    public void onNext() {
        if (counter < PROGRESS_COUNT) {
            loadImageFromUrl(stories[++counter].getImageUrl());
            storiesProgressView.setStoryDuration(stories[counter].getDuration()*1000);}
        //} else {
          //  // Nếu đang ở story cuối cùng, kết thúc activity
            //finish();
        //}
    }

    @Override
    public void onComplete() {
        // Khi câu chuyện kết thúc (hoàn thành), kết thúc activity
        finish();
    }

    @Override
    public void onPrev() {
        if ((counter) > 0) return;
        loadImageFromUrl(stories[--counter].getImageUrl());
        storiesProgressView.setStoryDuration(stories[counter].getDuration()*1000);
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
        finish();
    }
}
