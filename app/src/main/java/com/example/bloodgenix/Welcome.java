package com.example.bloodgenix;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class Welcome extends AppCompatActivity {

    Button loginView;
    BottomSheetDialog sheetDialog;
    Button signUpBtn;
    String profile_details[] = new String[15];
    Bitmap bitmap;


    //Bottom Sheet Dialog
    TextInputLayout passwordLayout;
    LoadingDialog dialog;
    Button loginBtn;
    ImageButton back_btn;
    TextInputEditText mobileNo, Password;
    CountryCodePicker picker;
    TextInputLayout mobLayout;
    String countryWithPlus;
    String phoneNumberPattern = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";

    //FireBase variable initialise
    FirebaseAuth mAuth;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginView = findViewById(R.id.loginView);
        mAuth = FirebaseAuth.getInstance();

        dialog = new LoadingDialog(Welcome.this);

        loginView.setOnClickListener(v -> {

            sheetDialog = new BottomSheetDialog(Welcome.this, R.style.BottomSheetStyle);
            View view = LayoutInflater.from(Welcome.this).inflate(R.layout.activity_login_screen, (LinearLayout) findViewById(R.id.sheet));
            sheetDialog.setContentView(view);

            mobileNo = sheetDialog.findViewById(R.id.mobileNo);
            Password = sheetDialog.findViewById(R.id.Password);
            picker = sheetDialog.findViewById(R.id.codePicker);
            back_btn = sheetDialog.findViewById(R.id.back_btn);
            passwordLayout = sheetDialog.findViewById(R.id.passwordLayout);
            sheetDialog.show();

            listners(sheetDialog);
            back_btn.setOnClickListener(v12 -> {
                sheetDialog.dismiss();
            });

            loginBtn = sheetDialog.findViewById(R.id.loginBtn);
            assert loginBtn != null;
            loginBtn.setOnClickListener(v1 -> {


                String _number = Objects.requireNonNull(mobileNo.getText()).toString().trim();
                String _password = Objects.requireNonNull(Password.getText()).toString();
                String full_number = countryWithPlus + " " + _number;

                passwordLayout.setEndIconActivated(false);

                if (TextUtils.isEmpty(_number) && TextUtils.isEmpty(_password)) {
                    Toast.makeText(sheetDialog.getContext(), " Enter valid data ", Toast.LENGTH_SHORT).show();
                    mobileNo.setError("Empty field");
                    passwordLayout.setEndIconDrawable(0);
                    Password.setError("Empty field");

                } else if (TextUtils.isEmpty(_password)) {
                    passwordLayout.setEndIconDrawable(0);
                    Password.setError("Empty field");
                } else if (full_number.matches(phoneNumberPattern) == false) {
                    mobileNo.setError(" Invalid number ");

                } else if (_password.length() <= 6) {
                    passwordLayout.setEndIconDrawable(0);
                    Password.setError(" Small password ");
                } else {
                    mobileNo.setError(null);
                    Password.setError(null);

                    dialog.startDialog();
                    Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(full_number);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String system_password = snapshot.child(_number).child("password").getValue(String.class);

                                if (system_password.equals(_password)) {

//                                    String _fullName = snapshot.child(_number).child("fullName").getValue(String.class);
//                                    String _emailId = snapshot.child(_number).child("emailId").getValue(String.class);
//                                    String _gender = snapshot.child(_number).child("gender").getValue(String.class);
//                                    String _dob = snapshot.child(_number).child("d_o_b").getValue(String.class);
//                                    String _img = snapshot.child(_number).child("profileImg").getValue(String.class);
//
//                                    profile_details [0] = _fullName;
//                                    profile_details [1] = _emailId;
//                                    profile_details [2] = system_password;
//                                    profile_details [3] =_dob;
//                                    profile_details [4] =_gender;
//                                    profile_details [5] = _number;
//                                    profile_details [6] = _img;
                                    dialog.dismissDialog();
                                    Intent dashBoard = new Intent(sheetDialog.getContext(), DashBoard_Screen.class);
                                    dashBoard.putExtra("profile Values", full_number);
                                    startActivity(dashBoard);
                                } else {
                                    dialog.dismissDialog();
                                    Toast.makeText(Welcome.this, "Incorrect password", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                dialog.dismissDialog();
                                Toast.makeText(Welcome.this, "User does not exists", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            dialog.dismissDialog();
                            Toast.makeText(sheetDialog.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        });
        signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(v13 -> {
            Intent signUp = new Intent(Welcome.this, Signup_Screen_1.class);
            startActivity(signUp);
        });

    }

    private void listners(BottomSheetDialog sheetDialog) {
        String code = picker.getSelectedCountryCode();
        countryWithPlus = picker.getSelectedCountryCodeWithPlus();

    }

}