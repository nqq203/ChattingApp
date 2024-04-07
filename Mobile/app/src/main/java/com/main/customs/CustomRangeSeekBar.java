package com.main.customs;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.group4.matchmingle.R;

public class CustomRangeSeekBar extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float minThumbX;
    private float maxThumbX;
    private int thumbRadius = 20;
    private float thumbMax = 100; // Giới hạn trên của seekbar
    private float thumbMin = 0;   // Giới hạn dưới của seekbar
    private float minValue = 20;  // Giá trị bắt đầu
    private float maxValue = 80;  // Giá trị kết thúc
    private int activePointerId = MotionEvent.INVALID_POINTER_ID;
    private boolean isMinThumbPressed = false;
    private boolean isMaxThumbPressed = false;
    private Rect textBounds = new Rect();
    public CustomRangeSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomRangeSeekBar,
                0, 0);

        try {
            thumbMax = a.getFloat(R.styleable.CustomRangeSeekBar_thumbMax, 100); // Giá trị mặc định là 100
            thumbMin = a.getFloat(R.styleable.CustomRangeSeekBar_thumbMin, 0);   // Giá trị mặc định là 0
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        int color = ContextCompat.getColor(getContext(), R.color.purple_2);

        paint.setColor(color); // Màu của thumb và đường
        paint.setStrokeWidth(20);
        paint.setTextSize(35); // Thêm dòng này
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Xác định kích thước của view
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = thumbRadius * 2 + 100; // Chiều cao phụ thuộc vào kích thước của thumb và padding
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int activeRangeColor = ContextCompat.getColor(getContext(), R.color.purple_2);

        // Cập nhật vị trí của thumb dựa trên giá trị
        minThumbX = (minValue / thumbMax) * (getWidth() - getPaddingRight() - getPaddingLeft()) + getPaddingLeft();
        maxThumbX = (maxValue / thumbMax) * (getWidth() - getPaddingRight() - getPaddingLeft()) + getPaddingLeft();

        // Vẽ đường trước thumb min
        paint.setColor(Color.GRAY); // Đặt màu cho phần đường không hoạt động
        canvas.drawLine(getPaddingLeft(), getHeight() / 2, minThumbX, getHeight() / 2, paint);

        // Vẽ đường giữa min và max
        paint.setColor(activeRangeColor); // Màu của phần đường hoạt động
        canvas.drawLine(minThumbX, getHeight() / 2, maxThumbX, getHeight() / 2, paint);

        // Vẽ đường sau thumb max
        paint.setColor(Color.GRAY); // Đặt màu cho phần đường không hoạt động
        canvas.drawLine(maxThumbX, getHeight() / 2, getWidth() - getPaddingRight(), getHeight() / 2, paint);

        // Vẽ các thumb
        paint.setColor(activeRangeColor); // Đặt lại màu cho thumb
        canvas.drawCircle(minThumbX, getHeight() / 2, thumbRadius, paint);
        canvas.drawCircle(maxThumbX, getHeight() / 2, thumbRadius, paint);

        // Hiển thị giá trị trên thumb
        paint.setColor(Color.BLACK);
        paint.setTextSize(35); // Cỡ chữ cho giá trị min và max
        canvas.drawText(String.valueOf((int)minValue), minThumbX, getHeight() / 2 - thumbRadius - 10, paint);
        canvas.drawText(String.valueOf((int)maxValue), maxThumbX, getHeight() / 2 - thumbRadius - 10, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activePointerId = event.getPointerId(0);
                // Kiểm tra xem người dùng chạm gần thumb nào
                if (Math.abs(x - minThumbX) < thumbRadius) {
                    isMinThumbPressed = true;
                } else if (Math.abs(x - maxThumbX) < thumbRadius) {
                    isMaxThumbPressed = true;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                // Chỉ di chuyển thumb nếu nó đang được giữ
                if (isMinThumbPressed) {
                    float proposedMinValue = Math.max(thumbMin, Math.min(maxValue - 1, (x / getWidth()) * thumbMax));
                    // Giới hạn minValue không thể lớn hơn hoặc bằng maxValue
                    if (proposedMinValue <= maxValue - 1) {
                        minValue = proposedMinValue;
                        minThumbX = x;
                        invalidate(); // Yêu cầu vẽ lại view
                    }
                } else if (isMaxThumbPressed) {
                    float proposedMaxValue = Math.max(minValue + 1, Math.min(thumbMax, (x / getWidth()) * thumbMax));
                    // Giới hạn maxValue không thể nhỏ hơn hoặc bằng minValue
                    if (proposedMaxValue >= minValue + 1) {
                        maxValue = proposedMaxValue;
                        maxThumbX = x;
                        invalidate(); // Yêu cầu vẽ lại view
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Nhả tất cả các thumb và reset trạng thái
                isMinThumbPressed = false;
                isMaxThumbPressed = false;
                activePointerId = MotionEvent.INVALID_POINTER_ID;
                invalidate(); // Có thể bạn muốn cập nhật view sau khi thả thumb
                return true;
        }
        return super.onTouchEvent(event);
    }
}
