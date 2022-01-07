package com.example.bloodgenix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bloodgenix.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class fullProfileView extends AppCompatActivity {

    ImageView fullProfile;
    TextView fullProfileNumber;
    ImageButton back_button;

    String Img, Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_profile_view);

        Intent previousExtract = getIntent();
        Number = previousExtract.getStringExtra("phone Number");
//        Number = previousExtract.getStringExtra();

        fullProfile = findViewById(R.id.fullProfile);
        fullProfileNumber = findViewById(R.id.fullProfileNumber);
        back_button = findViewById(R.id.back_button);

        fetchImageValue(Number);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBack = new Intent(fullProfileView.this, User_Profile_View.class);
                toBack.putExtra("mobile number",Number);
                startActivity(toBack);
                finish();
            }
        });
    }

    private void fetchImageValue(String number) {
        String _number = number.substring(4, number.length());
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(number);
        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String _img = snapshot.child(_number).child("profileImg").getValue(String.class);
                Glide.with(fullProfileView.this).load(_img).into(fullProfile);
                fullProfileNumber.setText(number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}