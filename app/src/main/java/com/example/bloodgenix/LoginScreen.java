package com.example.bloodgenix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

public class LoginScreen extends AppCompatActivity {

    ImageButton back_btn;
    Button loginBtn;

    TextInputLayout mobLayout;
    String countryWithPlus;
    String phoneNumberPattern="/^(\\+\\d{1,3}[- ]?)?\\d{10}$/";

    //Firebase initialisation
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        back_btn = findViewById(R.id.back_btn);
        mobLayout = findViewById(R.id.mobLayout);
        loginBtn =findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog dialog = new LoadingDialog(LoginScreen.this);
                dialog.startDialog();

                Toast.makeText(LoginScreen.this, "under btn click", Toast.LENGTH_SHORT).show();
//                String _number = mobileNo.getText().toString().trim();
//                String _password = Password.getText().toString().trim();
//                String full_number = countryWithPlus+" "+_number;
//                if (TextUtils.isEmpty(_number) || TextUtils.isEmpty(_password)){
//                    Toast.makeText(LoginScreen.this, "Enter valid data", Toast.LENGTH_SHORT).show();
//                }else if(!_number.matches(phoneNumberPattern)){
//                    mobileNo.setError(" Invalid phone number ");
//                }else if(_password.length()>6){
//                    Password.setError(" Small password ");
//                }else{
//                    Query checkUser = database.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(full_number);
//                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()){
//                                String system_password=snapshot.child(full_number).child("password").getValue(String.class);
//                                if (system_password.equals(_password)){
//                                    dialog.dismissDialog();
//                                    Intent dashBoard = new Intent(LoginScreen.this,DashBoard_Screen.class);
//                                    startActivity(dashBoard);
//                                }else{
//                                    Toast.makeText(LoginScreen.this, " Wrong password ", Toast.LENGTH_SHORT).show();
//                                    Password.setText("");
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Toast.makeText(LoginScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginScreen.this, "This cancel", Toast.LENGTH_SHORT).show();
            }
        });

        mobLayout.setPrefixText(countryWithPlus.toString());

    }

    private void back(View view){
        Intent signUp = new Intent(LoginScreen.this,Welcome.class);
        startActivity(signUp);
    }



}