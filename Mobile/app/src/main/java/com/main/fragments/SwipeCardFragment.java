package com.main.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.main.activities.FilterActivity;
import com.main.activities.NotificationActivity;
import com.main.callbacks.OnSwipeTouchListener;
import com.main.entities.User;

import java.util.ArrayList;
import java.util.List;

public class SwipeCardFragment extends Fragment {
    private CardView cardView;
    private TextView userName;
    private TextView userAge;
    private TextView distance;
    private ImageView userProfileImage;
    private ImageView filterIcon;
    private TextView notiIcon;
    private RelativeLayout cardWrapper;
    private List<User> users = new ArrayList<>();
    private int currentUserIndex = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_cardview_fragment, container, false);
        cardView = view.findViewById(R.id.card_view);
        cardWrapper = view.findViewById(R.id.card_wrapper);
        userName = cardView.findViewById(R.id.card_name);
        userAge = cardView.findViewById(R.id.card_age);
        distance = cardView.findViewById(R.id.card_distance);
        userProfileImage = cardView.findViewById(R.id.card_image);
        filterIcon = cardWrapper.findViewById(R.id.icon_filter);
        notiIcon = cardWrapper.findViewById(R.id.icon_noti);

        filterIcon.setOnClickListener(v -> {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
            }
        });

        notiIcon.setOnClickListener(v -> {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

//        Initialize list of users here
        users.add( new User("Lisa", "female", "23/12/2000", "0912345678", "abc1234", "https://i.pinimg.com/originals/87/e3/68/87e3680db94fa0baa744017596863653.jpg"));
        users.add( new User("Jisoo", "female", "23/12/1999", "0912345678", "abc1234", "https://th.bing.com/th/id/OIP.xNcchNwI9Vtv66fF0dGHbwHaLH?rs=1&pid=ImgDetMain"));
        users.add( new User("Jennie", "female", "23/12/1998", "0912345678", "abc1234", "https://data.boomsbeat.com/data/images/full/304085/jennie.jpg?w=802&l=50&t=40"));
        users.add( new User("Rose", "female", "23/12/1997", "0912345678", "abc1234", "https://th.bing.com/th/id/OIP.faNRNq3OP8EP-1O2yTlNhwHaNK?rs=1&pid=ImgDetMain"));

        displayUser(users.get(currentUserIndex));
        setupCardSwipe();
        return view;
    }

    private void displayUser(User user) {
        userName.setText(user.getFullname());
        userAge.setText(user.getDate());
        distance.setText("345 miles");
        if (user.getImageUrl() != null) {
            Glide.with(getContext())
                    .load(user.getImageUrl())
                    .into(userProfileImage);
        } else {
            userProfileImage.setImageResource(R.drawable.lisa);
        }
    }

    private void setupCardSwipe() {
        cardView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                simulateSwipeLeft();
            }
            @Override
            public void onSwipeRight() {
                simulateSwipeRight();
            }
        });
    }
    public void simulateSwipeRight() {
        // Example animation to simulate swipe right
        cardView.animate()
                .translationX(1000)
                .rotation(40)
                .alpha(0)
                .setDuration(500)
                .withEndAction(() -> {
                    // Reset card position and visibility here
                    cardView.setTranslationX(0);
                    cardView.setAlpha(1);
                    cardView.setRotation(0);
                    showNextUser(true);
                });
    }


    public void simulateSwipeLeft() {
        // Similar to simulateSwipeRight but animate to the left
        cardView.animate()
                .translationX(-1000)
                .rotation(-40)
                .alpha(0)
                .setDuration(500)
                .withEndAction(() -> {
                    // Reset card position and visibility here
                    cardView.setTranslationX(0);
                    cardView.setAlpha(1);
                    cardView.setRotation(0);
                    showNextUser(false);
                });
    }

    private void showNextUser(boolean isRightSwipe) {
        currentUserIndex++;
        if (currentUserIndex >= users.size()) {
            currentUserIndex = 0; // Hoặc có thể bạn muốn xử lý khi danh sách kết thúc
        }
        // Đặt card về vị trí ban đầu và hiển thị thông tin user mới
        cardView.setTranslationX(0);
        cardView.setRotation(0);
        cardView.setAlpha(1);

        // Hiển thị thông tin của user mới
        displayUser(users.get(currentUserIndex));
    }
}
