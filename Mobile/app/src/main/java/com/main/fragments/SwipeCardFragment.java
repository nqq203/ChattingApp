package com.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.main.callbacks.OnSwipeTouchListener;

public class SwipeCardFragment extends Fragment {

    private CardView cardView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof CardSwipeListener) {
//            swipeListener = (CardSwipeListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement SwipeActionListener");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_cardview_fragment, container, false);
        cardView = view.findViewById(R.id.card_view);
        setupCardSwipe();
        return view;
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
                });
    }
}
