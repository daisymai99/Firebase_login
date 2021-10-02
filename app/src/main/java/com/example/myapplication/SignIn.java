package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    EditText edt_username, edt_password;
    Button login;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FirebaseApp.initializeApp(this);

        edt_username = findViewById(R.id.txtEmail);
        edt_password = findViewById(R.id.txtPass);
        login = findViewById(R.id.btnIn);

        mAuth = FirebaseAuth.getInstance();
        openForgetPassword();

        createNewUser();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void createNewUser() {

        findViewById(R.id.linkUp).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),SignUp.class));
            finish();
        }
    });
    }

    private void openForgetPassword() {

        findViewById(R.id.txtForgetPass).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),Forget_pass.class));
            finish();
        }
    });
    }

    public void loginUser() {
        String email = edt_username.getText().toString().trim();
        String password = edt_password.getText().toString().trim();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!password.isEmpty()) {
                     mAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {


                                Toast.makeText(SignIn.this, "Successfull  !!!", Toast.LENGTH_SHORT).show();
                                 mAuth.getCurrentUser();
//                                 openNewActivity(MainActivity.class);
                                navigateToMainActivity();
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                         @Override
                          public void onFailure(@NonNull Exception e) {
                             Toast.makeText(SignIn.this, "Fail !!!", Toast.LENGTH_SHORT).show();
                              }
                         });
            } else {
                edt_password.setError("Please enter password");
            }
        } else if (email.isEmpty()) {
            edt_username.setError("Please enter email");
        } else {
            edt_username.setError("Email not correct ");
        }
    }

    public void openNewActivity(Class temp){
        Intent intent = new Intent(getApplicationContext(), temp);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //finish();
    }

    public void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



}