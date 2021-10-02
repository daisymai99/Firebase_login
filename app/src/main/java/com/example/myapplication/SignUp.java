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

import com.example.myapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText mPhone, mUsername, mEmail, mPassword, mConfirmPass;
    private Button btnSignUp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUsername = findViewById(R.id.edtName);
        mPhone = findViewById(R.id.edtPhone);
        mEmail = findViewById(R.id.edtEmail);
        mPassword = findViewById(R.id.edtPass);
        mConfirmPass = findViewById(R.id.edtConfirm);
        btnSignUp = findViewById(R.id.btnCreate);

        findViewById(R.id.linkSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignIn.class));
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),SignIn.class));
                finish();
            }
        });
    }

    public void createUser() {

        String name = mUsername.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        // Firstly, we check username is exits or not

        if(name.isEmpty() ){
            mUsername.setError("Please enter phone number");
            mUsername.requestFocus();
            return;
        }




        if(phone.isEmpty()){
            mPhone.setError("Please enter phone number");
            mPhone.requestFocus();
            return;
        }



        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!password.isEmpty()) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isComplete()) {

                                    User user = new User(email,name,phone,password);


                                    FirebaseDatabase.getInstance().getReference("User")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(SignUp.this,"Successful !!!",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),SignIn.class));
                                                finish();
                                            }

                                        }
                                    });


                                } else {
                                    Toast.makeText(SignUp.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SignUp.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (password.length() < 6) {
                mPassword.setError("Password must be longer than 6 character"); }
            else {
                mPassword.setError("Enter correct password"); }

        } else if (email.isEmpty()) {
            mEmail.setError("Please enter email");
        } else {
            mEmail.setError("Email not correct "); }
    }
}