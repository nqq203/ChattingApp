package com.main.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group4.matchmingle.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UploadImageType extends Activity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private static final String TAG = "MediaActivity";
    private ImageView image;
    private static final int REQUEST_CODE_SELECT_IMAGES = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_type);

        ImageButton closeBtn = findViewById(R.id.closeBtn);
        View btnChoose = findViewById(R.id.btnChoose);
        View btnCapture = findViewById(R.id.btnCapture);
        image = findViewById(R.id.imageCapture);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng activity và trở về activity trước đó
                finish();
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở gallery để chọn multiple images
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), REQUEST_CODE_SELECT_IMAGES);
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở camera để chụp ảnh
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_IMAGES && data != null) {
                // Xử lý khi chọn ảnh từ gallery
                handleSelectedImages(data);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                // Xử lý khi chụp ảnh từ camera
                handleCapturedImage(data);
            }
        }
    }

    private void handleSelectedImages(Intent data) {
        ArrayList<String> imageUris = new ArrayList<>();
        if (data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String imagePath = getRealPathFromUri(selectedImageUri);
            imageUris.add(imagePath);
        } else if (data.getClipData() != null) {
            ClipData clipData = data.getClipData();
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                String imagePath = getRealPathFromUri(uri);
                imageUris.add(imagePath);
            }
        }

        // Chuyển đổi đường dẫn tệp thành Uri
        ArrayList<String> imageUrisToSend = new ArrayList<>();
        for (String imagePath : imageUris) {
            Uri fileUri = Uri.fromFile(new File(imagePath));
            imageUrisToSend.add(fileUri.toString());
        }

        // Gửi Uri qua Intent
        Intent intent = new Intent(UploadImageType.this, UploadProfileConfirm.class);
        intent.putStringArrayListExtra("imageUris", imageUrisToSend);
        startActivity(intent);
    }

    private void handleCapturedImage(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        if (imageBitmap != null) {
            Uri imageUri = saveBitmapAndGetUri(imageBitmap);

            // Chuyển đổi đường dẫn tệp thành Uri
            Uri fileUri = Uri.fromFile(new File(imageUri.getPath()));

            // Gửi Uri qua Intent
            ArrayList<String> imageUris = new ArrayList<>();
            imageUris.add(fileUri.toString());
            Intent intent = new Intent(UploadImageType.this, UploadProfileConfirm.class);
            intent.putStringArrayListExtra("imageUris", imageUris);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(columnIndex);
        cursor.close();
        return imagePath;
    }

    private Uri saveBitmapAndGetUri(Bitmap bitmap) {
        File imagesDir = new File(getFilesDir(), "images");
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }

        File imageFile = new File(imagesDir, "captured_image.jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return Uri.fromFile(imageFile);
    }
}
