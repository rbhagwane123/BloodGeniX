package com.example.bloodgenix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodgenix.R;
import com.example.bloodgenix.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=5000;
    //Defining Attributes
    TextView appTitle;
    ImageView appIcon;
    Animation topAnim, bottomAnim;
    String phone="";
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
                SessionManager sessionManager = new SessionManager(MainActivity.this,"userLoginSession");
                HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();
                phone = userDetails.get(SessionManager.KEY_PHONENUMBER);
                String IS_login = userDetails.get(SessionManager.IS_LOGIN);
                if (phone == null){
                    Intent intent = new Intent(MainActivity.this, OnBoardingScreen.class);
                    startActivity(intent);
                }
                else{
                    Intent dashBoard = new Intent(MainActivity.this, DashBoard_Screen.class);
                    dashBoard.putExtra("profile Values","+91 "+phone);
                    startActivity(dashBoard);
                }
//                else if (sessionManager.checkLogin()){
//                    //                    Toast.makeText(MainActivity.this, IS_login, Toast.LENGTH_SHORT).show();
//
//                }
            }
        },SPLASH_SCREEN);

    }
}