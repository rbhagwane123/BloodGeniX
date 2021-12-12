package com.example.bloodgenix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodgenix.R;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=5000;
    //Defining Attributes
    TextView appTitle;
    ImageView appIcon;
    Animation topAnim, bottomAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        appTitle = findViewById(R.id.appTitle);
        appIcon = findViewById(R.id.appIcon);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        appIcon.setAnimation(topAnim);
        appTitle.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, OnBoardingScreen.class);
                startActivity(intent);
            }
        },SPLASH_SCREEN);

    }
}