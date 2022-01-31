package com.example.bloodgenix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodgenix.activities.DashBoard_Screen;

import java.util.HashMap;

public class Settings extends AppCompatActivity {

    private ImageView backBtn;
    LinearLayout FAQLayout, helpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backBtn = findViewById(R.id.backButtons);
        FAQLayout = findViewById(R.id.FAQLayout);
        helpLayout = findViewById(R.id.helpLayout);
        SessionManager sessionManager = new SessionManager(this,"userLoginSession");
        HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();

        String phoneNumb = userDetails.get(SessionManager.KEY_PHONENUMBER);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashBoard = new Intent(Settings.this, DashBoard_Screen.class);
                dashBoard.putExtra("profile Values", "+91 "+phoneNumb);
                startActivity(dashBoard);
            }
        });

        FAQLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "FAQ's clicked", Toast.LENGTH_SHORT).show();
            }
        });

        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "Help clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}