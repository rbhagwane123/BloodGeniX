package com.example.bloodgenix.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodgenix.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class ForgetPassword extends AppCompatActivity {

    CountryCodePicker forgetCountryPicker;
    TextInputEditText forgetMobileNo;
    String countryWithPlus;
    Button forgetNextBtn;
    ImageButton forgotBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forgetCountryPicker = findViewById(R.id.forgetCountryPicker);
        forgetMobileNo = findViewById(R.id.forgetMobileNo);
        forgetNextBtn = findViewById(R.id.forgetNextBtn);
        forgotBackBtn = findViewById(R.id.forgotBackBtn);
        listners(ForgetPassword.this);

        forgotBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Welcome.class));
                finish();
            }
        });

        forgetNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _number = Objects.requireNonNull(forgetMobileNo.getText()).toString().trim();
                String full_number = countryWithPlus + " " + _number;
                Toast.makeText(ForgetPassword.this, full_number, Toast.LENGTH_SHORT).show();
                Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(full_number);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            forgetMobileNo.setError(null);
                            Intent i0 = new Intent(ForgetPassword.this, Otp_Screen2.class);
                            i0.putExtra("whatToDo","updateData");
                            i0.putExtra("phoneNo",full_number);
                            startActivity(i0);
                        }else{
                            forgetMobileNo.setError("No such user");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    private void listners(Activity forget) {
        String code = forgetCountryPicker.getSelectedCountryCode();
        countryWithPlus = forgetCountryPicker.getSelectedCountryCodeWithPlus();

    }
}