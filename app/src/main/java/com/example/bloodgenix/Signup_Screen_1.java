package com.example.bloodgenix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.drjacky.imagepicker.ImagePicker;

public class Signup_Screen_1 extends AppCompatActivity {

    ImageButton nextButton_1, profilePickerBtn;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen1);

        profileImage = findViewById(R.id.profileImage);
        profilePickerBtn = findViewById(R.id.profilePickerBtn);
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
                startActivity(signup_2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        profileImage.setImageURI(uri);
    }
}