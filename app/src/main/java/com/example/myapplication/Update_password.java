package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class Update_password extends AppCompatActivity  {

    EditText txtEmail, txtPass;
    Button update;



    FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);


        txtEmail = findViewById(R.id.txtEmail);

        update = findViewById(R.id.btnUpdate);

        String email = txtEmail.getText().toString();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(update.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    startActivity(new Intent(getApplicationContext(),SignIn.class));
                                    finish();
                                }
                            }
                        });


            }
        });

    }
}