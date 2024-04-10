package com.main.activities;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.Manifest;
import com.group4.matchmingle.R;
import com.main.adapters.SignUpAdapter;
import com.main.entities.User;

public class SignUpActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private Uri imageUri;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        EditText editFullname = (EditText) findViewById(R.id.editFullname);
        EditText editGender = (EditText) findViewById(R.id.editGender);
        EditText editBirthDate = (EditText) findViewById(R.id.editDate);
        EditText editPassword = (EditText) findViewById(R.id.editPassword);
        Button buttonChooseImage = (Button) findViewById(R.id.buttonChooseImage);
        Button buttonSignUp = (Button) findViewById(R.id.signUpButton);
        ImageView backSignUp = (ImageView) findViewById(R.id.back_signup);

        String phoneNumber = getIntent().getStringExtra("phone_number");

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        backSignUp.setOnClickListener(view -> {
            Intent intentSignIn = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intentSignIn);
            finish();
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = editFullname.getText().toString();
                String gender = editGender.getText().toString();
                String date = editBirthDate.getText().toString();
                String password = editPassword.getText().toString();
                if (fullname == "" || gender == "" || date == "" || password == "" || imageUri == null) {
                    Toast.makeText(SignUpActivity.this, "Required all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                User newUser = new User(fullname, gender, date, phoneNumber, password, null);
                uploadImageToFirebaseStorage();
                saveUserToDatabase(newUser);
            }
        });
    }
    private void saveUserToDatabase(User user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance()
                .getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Log.d(TAG, "Toi duoc save user");
        usersRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete");
                    // Chuyển đến MainActivity sau khi đăng ký thành công
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Xử lý lỗi
                    Toast.makeText(SignUpActivity.this, "User register failed! Let try again" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openFileChooser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Quyền không được cấp, yêu cầu quyền
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        } else {
            // Quyền đã được cấp, mở file chooser
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView imageView = findViewById(R.id.imageViewPreview);
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, mở file chooser
                openFileChooser();
            } else {
                // Quyền bị từ chối, hiển thị thông báo
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        if (imageUri != null) {
            StorageReference fileReference = FirebaseStorage.getInstance().getReference("uploads").child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            Log.d(TAG, "uploadImageToFirebaseStorage:  " + imageUri);
            fileReference.putFile(imageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();
                } else {
                    Toast.makeText(SignUpActivity.this, "Upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
