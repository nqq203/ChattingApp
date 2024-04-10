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

import com.group4.matchmingle.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadImageType extends Activity {
    private static final String TAG = "MediaActivity";
    private ImageView image;
    private static final int REQUEST_CODE_SELECT_IMAGES = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;

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
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
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
        try {
            Log.v(TAG, String.valueOf(requestCode));
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
                // Xử lý khi chọn ảnh từ gallery

                imagesEncodedList = new ArrayList<>();

                if (data.getData() != null) {
                    Uri mImageUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri, null, null, null, null);

                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            imagesEncodedList.add(imagePath);
                        }
                        cursor.close();
                    }
                } else if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();

                        // Get the cursor
                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

                        if (cursor != null) {
                            while (cursor.moveToNext()) {
                                String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                                imagesEncodedList.add(imagePath);
                            }
                            cursor.close();
                        }
                    }
                }

                // Gửi danh sách hình ảnh sang hoạt động UploadProfileConfirm

                ArrayList<String> arrayList = new ArrayList<>(imagesEncodedList);
                Intent intent = new Intent(UploadImageType.this, UploadProfileConfirm.class);
                intent.putStringArrayListExtra("imageUris", arrayList);
                startActivity(intent);



            } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && null != data) {
                if (data != null && data.getExtras() != null) {
                    // Trích xuất ảnh từ dữ liệu Intent
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                    // Lưu ảnh vào bộ nhớ hoặc thư mục tạm thời (nếu cần)
                    //Log.e("UploadImageType",bitmap.toString());
                    // Chuyển đường dẫn của ảnh thành Uri
                    Uri imageUri = saveBitmapAndGetUri(bitmap);
                    Picasso.get().load(imageUri).into(image);
                    Log.v(TAG, imageUri.toString());
                    // Gửi đường dẫn của ảnh qua Intent

                    if (imageUri != null) {
                        Log.e("UploadImageType",imageUri.toString());
                        Intent intent = new Intent(UploadImageType.this, UploadProfileConfirm.class);
                        intent.putExtra("imageUri", imageUri.toString());
                        startActivity(intent);
                    }



                }
                else{
                    Log.v(TAG, "Picture canceled! :(");
                }
            }
            else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
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
