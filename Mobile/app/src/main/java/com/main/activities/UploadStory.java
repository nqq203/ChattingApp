package com.main.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.group4.matchmingle.R;

public class UploadStory extends Activity {

    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    Button btnChoosePicture;
    LinearLayout btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_story);

        imageView = findViewById(R.id.imageViewStory);
        btnChoosePicture = findViewById(R.id.btnChoosePictureStory);
        btnUpload = findViewById(R.id.btnUploadStory);

        btnChoosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý việc upload ảnh
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                imageView.setVisibility(View.VISIBLE);
                btnUpload.setVisibility(View.VISIBLE);
                btnChoosePicture.setVisibility(View.GONE);
                imageView.setImageURI(data.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
