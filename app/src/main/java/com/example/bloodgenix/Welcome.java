package com.example.bloodgenix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Welcome extends AppCompatActivity {

    Button loginView;
    BottomSheetDialog sheetDialog;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginView = findViewById(R.id.loginView);

        loginView.setOnClickListener(v -> {

            sheetDialog = new BottomSheetDialog(Welcome.this,R.style.BottomSheetStyle);
            View view = LayoutInflater.from(Welcome.this).inflate(R.layout.activity_login_screen,(LinearLayout)findViewById(R.id.sheet));
            sheetDialog.setContentView(view);
            sheetDialog.show();
        });
        signUpBtn= findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(Welcome.this,Signup_Screen_1.class);
                startActivity(signUp);
            }
        });
    }


}