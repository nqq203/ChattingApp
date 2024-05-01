package com.main.activities;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.group4.matchmingle.R;
import com.group4.matchmingle.databinding.ImageViewerFragmentBinding;
import com.main.MemoryData;
import com.main.adapters.ChatAdapter;
import com.main.entities.ChatList;
import com.main.fragments.ImageViewerFragment;
import com.main.fragments.InfoDialogFragment;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.OnImageClickListener {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private final List<ChatList> chatLists = new ArrayList<>();
    private UserSessionManager sessionManager;
    private String chatKey;
    private String myPhone;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;
    private MediaRecorder mediaRecorder;
    private String audioPath = null;
    private final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        sessionManager = new UserSessionManager(getApplicationContext());

        final ImageView backBtn = findViewById(R.id.back_chat);
        final TextView senderName = findViewById(R.id.user_chat_name);
        final EditText msgEditText = findViewById(R.id.edit_text_message);
        final TextView sendBtn = findViewById(R.id.button_send);
        final TextView sendImgBtn = findViewById(R.id.send_image);
        final TextView sendVoiceBtn = findViewById(R.id.send_voice);
        final ImageView profileImage = findViewById(R.id.user_chat_image);
        final TextView infoCatalog = findViewById(R.id.button_info);

        chatRecyclerView = findViewById(R.id.chat_container);

        // scroll to bottom when open keyboard
        final View activityRootView = findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(ChatActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // Scroll to bottom immediately
                    if (chatAdapter.getItemCount() > 0) {
                        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                    }
                }
            }
        });

        //get data from message adapter class
        final String senderFullname = getIntent().getStringExtra("fullname");
        final String senderProfilePic = getIntent().getStringExtra("imageUrl");
        chatKey = getIntent().getStringExtra("chatKey");
        final String senderMobile = getIntent().getStringExtra("mobile");

        //get my phone
        myPhone = sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);

        senderName.setText(senderFullname);
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(this).load(senderProfilePic).apply(requestOptions).into(profileImage);

        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        chatAdapter = new ChatAdapter(chatLists, ChatActivity.this);
        chatAdapter.setOnImageClickListener(this);
        chatRecyclerView.setAdapter(chatAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (chatKey.isEmpty()) {
                    chatKey = "1";
                    if (snapshot.hasChild("Chat")) {
                        chatKey = String.valueOf(snapshot.child("Chat").getChildrenCount() + 1);
                    }
                }
                if (snapshot.hasChild("Chat")) {
                    if (snapshot.child("Chat").child(chatKey).hasChild("messages")) {
                        chatLists.clear();
                        for (DataSnapshot messagesSnapshot : snapshot.child("Chat").child(chatKey).child("messages").getChildren()) {
                            if (messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("phoneNumber")) {
                                final String messageTimestamps = messagesSnapshot.getKey();
                                final String getPhone = messagesSnapshot.child("phoneNumber").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);
                                final String getType = messagesSnapshot.child("type").getValue(String.class);
                                final String getName = snapshot.child("Message").child(myPhone).child(getPhone).child("fullname").getValue(String.class);

                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                ChatList chatList = new ChatList(getPhone, getName, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date), getType);
                                chatLists.add(chatList);

                                if (loadingFirstTime || Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastMsg(ChatActivity.this, chatKey))) {
                                    loadingFirstTime = false;
                                    MemoryData.saveLastMsg(messageTimestamps, chatKey, ChatActivity.this);

                                    chatAdapter.updateChatLists(chatLists);
                                    chatRecyclerView.scrollToPosition(chatLists.size() - 1);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        infoCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog(); // Display the info dialog on click
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getTextMsg = msgEditText.getText().toString();
                // get current timestamps
                final String currentTimestanp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                databaseReference.child("Chat").child(chatKey).child("user1").setValue(myPhone);
                databaseReference.child("Chat").child(chatKey).child("user2").setValue(senderMobile);
//                databaseReference.child("Chat").child(chatKey).child("unseenmessage").setValue(0);

                sendMessage(getTextMsg, "text");
                msgEditText.setText("");
            }
        });

        LinearLayout recordPanel = findViewById(R.id.record_panel);
        sendVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordPanel.setVisibility(View.VISIBLE);
                startRecording();
            }
        });

        ImageView stopRecord = findViewById(R.id.record_icon);
        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordPanel.setVisibility(View.GONE);
                stopRecording();
            }
        });

        sendImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public boolean CheckPermissions() {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(ChatActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    private void startRecording() {
        if (!CheckPermissions()) {
            RequestPermissions();
        } else {
            audioPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio_" + System.currentTimeMillis() + ".3gp";
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioPath);
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();  // Stop first before releasing
            mediaRecorder.release();
            uploadAudio();
        }
    }

    private void uploadAudio() {
        File audioFile = new File(audioPath);
        Uri file = Uri.fromFile(audioFile);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("audio").child(file.getLastPathSegment());
        storageReference.putFile(file).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            // Lấy URL và gửi tin nhắn
            String audioUrl = uri.toString();
            sendMessage(audioUrl, "audio");
            mediaRecorder = null;
        })).addOnFailureListener(e -> {
            Log.e("Audio", "Failed to upload audio.");
            mediaRecorder = null;
        });
    }

    private void sendMessage(String content, String type) {
        final String timestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
        HashMap<String, Object> message = new HashMap<>();
        message.put("msg", content);
        message.put("type", type);
        message.put("phoneNumber", myPhone);

        databaseReference.child("Chat").child(chatKey).child("messages").child(timestamp).setValue(message);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("images/" + System.currentTimeMillis() + ".jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                sendMessage(uri.toString(), "image");
            })).addOnFailureListener(e -> {
                Toast.makeText(ChatActivity.this, "Upload image failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public void showImage(String imageUrl) {
        ImageViewerFragment imageViewerFragment = ImageViewerFragment.newInstance(imageUrl);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.image_viewer_container, imageViewerFragment)
                .addToBackStack(null)
                .commit();
        findViewById(R.id.image_viewer_container).setVisibility(View.VISIBLE);
    }

    public void showInfoDialog() {
        InfoDialogFragment dialog = new InfoDialogFragment();
        dialog.show(getSupportFragmentManager(), "InfoDialogFragment");
    }
}
