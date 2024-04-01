package com.main.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumberActivity extends AppCompatActivity {
    private EditText phoneEditText;
    private Button verifyPhoneNumberButton;
    private ImageView backSignInButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_phone_number_activity);

        phoneEditText = findViewById(R.id.phoneEditText);
        backSignInButton = findViewById(R.id.backSignInButton);
        verifyPhoneNumberButton = findViewById(R.id.verifyPhoneNumberButton);

        mAuth = FirebaseAuth.getInstance();

        verifyPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneEditText.getText().toString().trim();
                onVerifyPhoneNumber(phoneNumber);
            }
        });

        backSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifyPhoneNumberActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void onVerifyPhoneNumber(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.e(TAG, "onVerificationFailed", e);
                                Toast.makeText(VerifyPhoneNumberActivity.this, "Verification failed!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                super.onCodeSent(verificationId, token);
                                goToSendOtpActivity(phoneNumber, verificationId);
                            }
                        })        // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
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
                                Toast.makeText(VerifyPhoneNumberActivity.this, "Invalid verification id!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToSwipeCardActivity(String phoneNumber) {
        Intent intent = new Intent(this, SwipeCardViewActivity.class);
        intent.putExtra("phone_number", phoneNumber);
        startActivity(intent);
    }

    private void goToSendOtpActivity(String phoneNumber, String verificationId) {
        Intent intent = new Intent(this, SendOTPActivity.class);
        intent.putExtra("phone_number", phoneNumber);
        intent.putExtra("verification_id", verificationId);
        startActivity(intent);
    }
}
