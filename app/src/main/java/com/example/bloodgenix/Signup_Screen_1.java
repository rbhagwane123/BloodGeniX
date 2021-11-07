package com.example.bloodgenix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Signup_Screen_1 extends AppCompatActivity {

    ImageButton nextButton_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen1);

        nextButton_1 = findViewById(R.id.nextButton_1);
        nextButton_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup_2 = new Intent(Signup_Screen_1.this,Signup_Screen_2.class);
                startActivity(signup_2);
            }
        });
    }
}