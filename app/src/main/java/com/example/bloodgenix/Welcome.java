package com.example.bloodgenix;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class Welcome extends AppCompatActivity {

    Button loginView;
    BottomSheetDialog sheetDialog;
    Button signUpBtn;

    //Bottom Sheet Dialog
    TextInputLayout passwordLayout;
    LoadingDialog dialog;
    Button loginBtn;
    ImageButton back_btn;
    TextInputEditText mobileNo,Password;
    CountryCodePicker picker;
    TextInputLayout mobLayout;
    String countryWithPlus;
    String phoneNumberPattern="^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";

    //FireBase variable initialise
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginView = findViewById(R.id.loginView);

        loginView.setOnClickListener(v -> {

            sheetDialog = new BottomSheetDialog(Welcome.this,R.style.BottomSheetStyle);
            View view = LayoutInflater.from(Welcome.this).inflate(R.layout.activity_login_screen,(LinearLayout)findViewById(R.id.sheet));
            sheetDialog.setContentView(view);

            mobileNo = sheetDialog.findViewById(R.id.mobileNo);
            Password = sheetDialog.findViewById(R.id.Password);
            picker = sheetDialog.findViewById(R.id.codePicker);
            back_btn= sheetDialog.findViewById(R.id.back_btn);
            passwordLayout = sheetDialog.findViewById(R.id.passwordLayout);
            sheetDialog.show();

            listners(sheetDialog);
            back_btn.setOnClickListener(v12 -> {
                sheetDialog.dismiss();
            });

            loginBtn = sheetDialog.findViewById(R.id.loginBtn);
            loginBtn.setOnClickListener(v1 -> {

                dialog = new LoadingDialog(Welcome.this);
                dialog.startDialog();
                String _number = mobileNo.getText().toString().trim();
                String _password = Password.getText().toString();
                String full_number = countryWithPlus+" "+_number;
                Toast.makeText(sheetDialog.getContext(), _password, Toast.LENGTH_SHORT).show();
                passwordLayout.setEndIconActivated(false);

                if (TextUtils.isEmpty(_number) || TextUtils.isEmpty(_password)){
                    Toast.makeText(sheetDialog.getContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
                }else if(full_number.matches(phoneNumberPattern)){
                    mobileNo.setError(" Invalid number ");
                }
                else if(_password.length()<=6){
                    passwordLayout.setEndIconDrawable(0);
                    Password.setError(" Small password ");

                }else{

                    Query checkUser = database.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(full_number);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                String system_password=snapshot.child(_number).child("password").getValue(String.class);
                                if (system_password.equals(_password)){
                                    dialog.dismissDialog();
                                    Intent dashBoard = new Intent(sheetDialog.getContext(),DashBoard_Screen.class);
                                    startActivity(dashBoard);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(sheetDialog.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            signUpBtn= findViewById(R.id.signUpBtn);
            signUpBtn.setOnClickListener(v13 -> {
                Intent signUp = new Intent(Welcome.this,Signup_Screen_1.class);
                startActivity(signUp);
            });
        });

    }

    private void listners(BottomSheetDialog sheetDialog) {
        String code = picker.getSelectedCountryCode();
        countryWithPlus = picker.getSelectedCountryCodeWithPlus();

    }

}