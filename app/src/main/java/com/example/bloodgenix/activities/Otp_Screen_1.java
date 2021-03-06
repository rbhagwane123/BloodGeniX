package com.example.bloodgenix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodgenix.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class Otp_Screen_1 extends AppCompatActivity {

    public String details_2 [] = new String[15];
    Button getOtpBtn;
    CountryCodePicker codePicker_2;
    EditText mobileNumb;
    String phoneNumberPattern="^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";
    String countryWithPlus, _full_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen1);

        //Storing Values from details_2
        Intent i2 = getIntent();
        details_2= i2.getStringArrayExtra("details_2");

        //Country Code Picker Code
        codePicker_2 = findViewById(R.id.codePicker_2);
        codePicker_2.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryWithPlus = codePicker_2.getSelectedCountryCodeWithPlus();
            }
        });

        countryWithPlus = codePicker_2.getSelectedCountryCodeWithPlus();

        mobileNumb = findViewById(R.id.mobileNumb);

        getOtpBtn = findViewById(R.id.getOtpBtn);
        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobileNumb.getText().toString().trim().isEmpty() == false){
                    if(mobileNumb.getText().length() == 10 && !mobileNumb.getText().toString().equals("1234567890")){
                        Query personCheck = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo("+91 "+mobileNumb.getText().toString());
                        personCheck.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Toast.makeText(Otp_Screen_1.this, "Already registered user", Toast.LENGTH_SHORT).show();
                                }else{
                                    Intent Otp2 = new Intent(Otp_Screen_1.this,Otp_Screen2.class);
                                    String _full_number = countryWithPlus+" "+mobileNumb.getText().toString();
                                    Otp2.putExtra("Otp_value",details_2);
                                    Otp2.putExtra("phoneNo", _full_number);
                                    startActivity(Otp2);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else{
                        Toast.makeText(Otp_Screen_1.this, "Invalid number", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Otp_Screen_1.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}