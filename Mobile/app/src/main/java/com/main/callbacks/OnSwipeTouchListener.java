package com.main.callbacks;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public abstract class OnSwipeTouchListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;
    private float downRawX, downRawY;
    private boolean isDragging;
    private float touchSlop;
    private float THRESHOLD = 150;

    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float eventRawX = event.getRawX();
        float eventRawY = event.getRawY();
        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downRawX = eventRawX;
                downRawY = eventRawY;
                isDragging = false;
                return true;

            case MotionEvent.ACTION_MOVE:
                if (!isDragging) {
                    float deltaX = Math.abs(eventRawX - downRawX);
                    float deltaY = Math.abs(eventRawY - downRawY);
                    if (deltaX > touchSlop || deltaY > touchSlop) {
                        isDragging = true;
                    }
                }

                if (isDragging) {
                    float offsetX = eventRawX - downRawX;
                    float offsetY = eventRawY - downRawY;
                    view.setTranslationX(offsetX);
                    view.setTranslationY(offsetY);
                    float rotationDegrees = offsetX * 0.05f;
                    view.setRotation(rotationDegrees);
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
                float finalX = eventRawX;
                float distanceX = finalX - downRawX;
                view.animate().translationX(0).translationY(0).rotation(0).setDuration(300).start();
                isDragging = false;

                if (Math.abs(distanceX) > THRESHOLD) {
                    if (distanceX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                }
                return true;
        }
        return false;
    }

    public abstract void onSwipeLeft();

    public abstract void onSwipeRight();

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
