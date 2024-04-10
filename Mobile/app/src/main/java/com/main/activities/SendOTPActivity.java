package com.main.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group4.matchmingle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.main.entities.User;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {
    private EditText otpEditText;
    private Button sendingOtpButton;
    private TextView resendingOtpTextView;
    private ImageView backVerifyButton;
    private String mPhoneNumber;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.send_otp_activity);

        getDataIntent();

        otpEditText = (EditText) findViewById(R.id.otpEditText);
        sendingOtpButton = (Button) findViewById(R.id.sendingOtpButton);
        resendingOtpTextView = (TextView) findViewById(R.id.resendOtpTextView);
        backVerifyButton = (ImageView) findViewById(R.id.backVerifyButton);

        mAuth = FirebaseAuth.getInstance();


        sendingOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOtp = otpEditText.getText().toString().trim();
                onSendingOtp(strOtp);
            }
        });

        resendingOtpTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onResendingOtp();
            }
        });

        backVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendOTPActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            goToSwipeCardActivity(user.getPhoneNumber());
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SendOTPActivity.this, "Invalid verification id!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToSwipeCardActivity(String phoneNumber) {
        Intent intent = new Intent(this, SwipeCardViewActivity.class);
        intent.putExtra("phone_number", phoneNumber);
//        startActivity(intent);
        checkUserExistsOrCreate(intent, phoneNumber);
    }

    private void checkUserExistsOrCreate(final Intent nextActivityIntent, String phoneNumber) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://matchmingle-3065c-default-rtdb.asia-southeast1.firebasedatabase.app");
            final String uid = firebaseUser.getUid();
            Log.d(TAG, "checkUserExistsOrCreate: " + uid);
            // Lấy tham chiếu tới node 'User' trong database
            DatabaseReference usersRef = database.getReference("User").child(uid);
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "truoc khi kiem tra");
                    if (dataSnapshot.exists()) {
                        Log.d(TAG, "Co user roi");
                        startActivity(nextActivityIntent);
//                        // Người dùng không tồn tại, tạo mới
//                        // Bạn sẽ cần tạo một User object mới tương ứng với cấu trúc của bạn
//                        User newUser = new User(/* thông tin người dùng */);
//                        // Thêm người dùng mới vào database
//                        usersRef.setValue(newUser).addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                Log.d(TAG, "Người dùng mới đã được tạo trong database");
//                            } else {
//                                Log.d(TAG, "Lỗi khi tạo người dùng mới", task.getException());
//                            }
//                        });
                    }
                    else {
                        Intent intent = new Intent(SendOTPActivity.this, SignUpActivity.class);
                        intent.putExtra("phone_number", phoneNumber);
                        Log.d(TAG, "khong co user");
                        startActivity(intent);
                    }
                    Log.d(TAG, "sau khi kiem tra");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi
                    Log.e(TAG, "Lỗi khi kiểm tra người dùng trong database", databaseError.toException());
                }
            });
        }
    }

    private void getDataIntent() {
        mPhoneNumber = getIntent().getStringExtra("phone_number");
        mVerificationId = getIntent().getStringExtra("verification_id");
    }

    private void onSendingOtp(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void onResendingOtp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        .setForceResendingToken(mToken)
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(SendOTPActivity.this, "Verification failed!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                super.onCodeSent(verificationId, token);
                                mVerificationId = verificationId;
                                mToken = token;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}