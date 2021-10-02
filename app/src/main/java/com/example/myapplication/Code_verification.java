package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class Code_verification extends AppCompatActivity {

    private String number, verificationID, code;

    OtpView otpView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        otpView = findViewById(R.id.otpView);
        mAuth = FirebaseAuth.getInstance();

        number = getIntent().getStringExtra("number");

        sendVerificationCode(number);


        // click button Verify
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String data = otpView.getText().toString();
                if (data.equals(code)) {

                    verifyCode(data, number);
                } else {
                    Toast.makeText(Code_verification.this, "Mã xác nhận không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // click button back
        findViewById(R.id.Exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        //click button RESEND

        findViewById(R.id.resendOTP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(number);
                String data = otpView.getText().toString();
                if (data.equals(code)) {
                    verifyCode(data, number);
                    Toast.makeText(Code_verification.this, "Đã gửi lại mã ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Code_verification.this, "Mã xác nhận không đúng", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void sendVerificationCode(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;

            Log.d("xxxxxxxxxxxxxx", "send success");
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("xxxxxxxxxxxxxx", e.toString());

            Toast.makeText(Code_verification.this, "Đã xảy ra lỗi với mã xác nhận của bạn" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code, String phoneNumber) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);

        signInWithPhoneAuthCredential(credential, phoneNumber);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, String phone) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(getApplicationContext(), Update_password.class).putExtra("number", number));

                            finish();

                        } else {

                            Log.w(TAG, "signInWithCredential:failure", task.getException());


                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                Toast.makeText(Code_verification.this, "Mã xác nhận không đúng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}