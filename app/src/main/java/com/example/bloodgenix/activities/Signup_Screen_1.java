package com.example.bloodgenix.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodgenix.R;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup_Screen_1 extends AppCompatActivity {

    ImageButton nextButton_1, profilePickerBtn;
    CircleImageView profileImage, profileImage2;
    TextInputEditText fullName, userName, emailId, password;
    Uri profile_uri;
    Context context;

    StorageReference storageReference;

    public String details [] = new String[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen1);

        profileImage = findViewById(R.id.profileImage);
        profileImage2 = findViewById(R.id.profileImage2);
        profilePickerBtn = findViewById(R.id.profilePickerBtn);
        fullName = findViewById(R.id.fullName);
        userName = findViewById(R.id.userName);
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);


        profilePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Signup_Screen_1.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .cropOval()	    		//Allow dimmed layer to have a circle inside
                        .maxResultSize(1080,1080)
                        .start();
            }
        });

        nextButton_1 = findViewById(R.id.nextButton_1);
        nextButton_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullName.getText().toString().isEmpty() &&
                    userName.getText().toString().isEmpty() &&
                emailId.getText().toString().isEmpty() &&
                password.getText().toString().isEmpty()){
                    Toast.makeText(Signup_Screen_1.this, "Many fields left empty", Toast.LENGTH_SHORT).show();
                }else{

                    if (profileImage.getDrawable() == null){
//                        storageReference = FirebaseStorage
                        profile_uri = Uri.parse("android.resource://com.example.bloodgenix/drawable/ic_male_avatar");
                        profileImage2.setImageURI(profile_uri);
                        Toast.makeText(Signup_Screen_1.this, profile_uri.toString(), Toast.LENGTH_SHORT).show();
                    }
                    if (fullName.getText().toString().isEmpty()){
                        fullName.setError("Enter name");
                    }
                    if (userName.getText().toString().isEmpty()){
                        userName.setError("Enter user name");
                    }if (emailId.getText().toString().isEmpty()){
                        emailId.setError("Enter email");
                    }if (password.getText().toString().isEmpty()){
                        password.setError("Enter password");
                    }
                    try {
                        Intent signup_2 = new Intent(Signup_Screen_1.this,Signup_Screen_2.class);

                        details[0]=profile_uri.toString();
                        details[1]=(fullName.getText().toString());
                        details[2]=(userName.getText().toString());
                        details[3]=(emailId.getText().toString());
                        details[4]=(password.getText().toString());

                        signup_2.putExtra("details_1",details);
                        Toast.makeText(Signup_Screen_1.this, details[0], Toast.LENGTH_SHORT).show();
                        startActivity(signup_2);
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profile_uri = data.getData();
        profileImage.setImageURI(profile_uri);
    }
}