//package com.main.activities;
//
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.renderscript.Allocation;
//import android.renderscript.Element;
//import android.renderscript.RenderScript;
//import android.renderscript.ScriptIntrinsicBlur;
//import android.view.View;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.myapplication.R;
//import com.main.callbacks.FilterCallback;
//
//public class FilterActivity extends AppCompatActivity implements FilterCallback {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.filter_activity);
//
//        Bitmap screenshot = getIntent().getParcelableExtra("screenshot");
//        if (screenshot != null) {
//            Bitmap blurredScreenshot = blurBitmap(screenshot);
//            getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(), blurredScreenshot));
//        }
//    }
//
//    public void blurBackground() {
//        Bitmap screenshot = takeScreenshot();
//    }
//
//    public void finishFilterLayout() {
//        finish();
//    }
//
//    public Bitmap takeScreenshot() {
//        View rootView = getWindow().getDecorView().getRootView();
//        rootView.setDrawingCacheEnabled(true);
//        Bitmap screenshot = Bitmap.createBitmap(rootView.getDrawingCache());
//        rootView.setDrawingCacheEnabled(false);
//        return screenshot;
//    }
//
//    public Bitmap blurBitmap(Bitmap bitmap) {
//        RenderScript rs = RenderScript.create(this);
//        Allocation input = Allocation.createFromBitmap(rs, bitmap);
//        Allocation output = Allocation.createTyped(rs, input.getType());
//        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//        script.setRadius(25f); // Adjust the blur intensity as needed
//        script.setInput(input);
//        script.forEach(output);
//        output.copyTo(bitmap);
//        return bitmap;
//    }
//}
