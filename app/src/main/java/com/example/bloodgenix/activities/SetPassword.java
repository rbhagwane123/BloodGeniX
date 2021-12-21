package com.example.bloodgenix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodgenix.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetPassword extends AppCompatActivity {

    TextInputEditText newPassword, newPasswordConfirm;
    Button updatePassword;
    ImageButton setPasswordBack;

    Boolean chckpass;
    String phoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        Intent i9 = getIntent();
        phoneNo = i9.getStringExtra("phoneNo");

        newPassword = findViewById(R.id.newPassword);
        newPasswordConfirm = findViewById(R.id.newPasswordConfirm);
        updatePassword = findViewById(R.id.updatePassword);
        setPasswordBack = findViewById(R.id.setPasswordBack);

        setPasswordBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
                finish();
            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passValidate(newPassword.getText().toString(), newPasswordConfirm.getText().toString())){
                    newPassword.setError(null);
                    newPasswordConfirm.setError(null);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(phoneNo.substring(4,phoneNo.length())).child("password").setValue(newPassword.getText().toString());
                    Toast.makeText(SetPassword.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Welcome.class));
                    finish();
                }
            }
        });
    }

    private boolean passValidate(String newPass, String confirmNewPass) {

        if (newPass.equals(confirmNewPass)){
            if (newPass.equals("12345678") || newPass.equals("123456789")){
                newPassword.setError("weak password");
                chckpass = false;
            }
            else if(newPass.length() <=6 || confirmNewPass.length() <=6){
                newPassword.setError("To small pass");
                chckpass = false;
            }
            else{
                chckpass = true;
            }
        }else{
            newPasswordConfirm.setError("Not matches");
            chckpass = false;
        }
        return chckpass;
    }
}