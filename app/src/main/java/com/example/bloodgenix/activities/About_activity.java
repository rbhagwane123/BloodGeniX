package com.example.bloodgenix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodgenix.R;
import com.example.bloodgenix.SessionManager;

import java.util.HashMap;

public class About_activity extends AppCompatActivity {

    ImageButton aboutBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        SessionManager sessionManager = new SessionManager(this,"userLoginSession");
        HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();
        String fullNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);


        aboutBack = findViewById(R.id.aboutBack);
        aboutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backDash = new Intent(About_activity.this, DashBoard_Screen.class);
                backDash.putExtra("profile Values","+91 "+fullNumber);
                startActivity(backDash);
                finish();
            }
        });
    }
}