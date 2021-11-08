package com.example.bloodgenix;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Otp_Screen_1 extends AppCompatActivity {

    public String details_2 [] = new String[15];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen1);

        //Storing Values from details_2
        Intent i2 = getIntent();
        details_2= i2.getStringArrayExtra("details_2");

    }
}