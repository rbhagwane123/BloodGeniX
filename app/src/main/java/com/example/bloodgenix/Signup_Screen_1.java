package com.example.bloodgenix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup_Screen_1 extends AppCompatActivity {

    ImageButton nextButton_1, profilePickerBtn;
    CircleImageView profileImage;
    TextInputEditText fullName, userName, emailId, password;
    Uri profile_uri;
    public String details [] = new String[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen1);

        profileImage = findViewById(R.id.profileImage);
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
                        .maxResultSize(144,144)
                        .start();
            }
        });

        nextButton_1 = findViewById(R.id.nextButton_1);
        nextButton_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup_2 = new Intent(Signup_Screen_1.this,Signup_Screen_2.class);
                details[0]=(profile_uri.toString());
                details[1]=(fullName.getText().toString());
                details[2]=(userName.getText().toString());
                details[3]=(emailId.getText().toString());
                details[4]=(password.getText().toString());
                signup_2.putExtra("details_1",details);
                startActivity(signup_2);
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