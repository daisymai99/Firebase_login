package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.hbb20.CountryCodePicker;

public class Forget_pass extends AppCompatActivity {

    private Button btnNext;
    private EditText edt;
    private CountryCodePicker countryCodePicker;

    String code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        FirebaseApp.initializeApp(this);

        edt = findViewById(R.id.txtPhone);
        btnNext = findViewById(R.id.btnNext);
        countryCodePicker = findViewById(R.id.countryCode);


        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edt.getText().toString();
                String country = countryCodePicker.getFullNumber().toString().trim();

                String completePhone = country+phone;

                if(phone.isEmpty()){
                    edt.setError("Please enter number ");
                    edt.requestFocus();

                } else {
                    startActivity(new Intent(getApplicationContext(),Code_verification.class).putExtra("number",phone));
                    finish();
                }


            }
        });


    }




}


