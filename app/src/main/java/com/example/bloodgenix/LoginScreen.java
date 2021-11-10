package com.example.bloodgenix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class LoginScreen extends AppCompatActivity {

    ImageButton back_btn;
    Button loginBtn;
    TextInputEditText mobile,Password;
    CountryCodePicker picker;
    TextInputLayout mobLayout;
    String countryWithPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        back_btn = findViewById(R.id.back_btn);
        mobLayout = findViewById(R.id.mobLayout);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginScreen.this, "This cancxel", Toast.LENGTH_SHORT).show();
            }
        });
        initializeViews();
        listners();
        mobLayout.setPrefixText(countryWithPlus.toString());
        loginBtn = findViewById(R.id.loginBtn);
    }

    private void back(View view){
        Intent signUp = new Intent(LoginScreen.this,Welcome.class);
        startActivity(signUp);
    }

    private void listners() {
        String code = picker.getSelectedCountryCode();
        countryWithPlus = picker.getSelectedCountryCodeWithPlus();
        Toast.makeText(LoginScreen.this, countryWithPlus, Toast.LENGTH_SHORT).show();

    }

    private void initializeViews() {
        picker = (CountryCodePicker) findViewById(R.id.codePicker);
        mobile = (TextInputEditText) findViewById(R.id.mobileNo);
    }

}