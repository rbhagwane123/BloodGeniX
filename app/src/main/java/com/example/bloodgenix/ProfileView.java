package com.example.bloodgenix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileView extends AppCompatActivity {

    CircleImageView profileImageView;
    EditText personName, personBlood, personGender;
    String phoneNumb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        Intent p = getIntent();
        phoneNumb = p.getStringExtra("mobile number");

        personBlood = findViewById(R.id.personBlood);
        personGender = findViewById(R.id.personGender);
        personName = findViewById(R.id.personName);
        profileImageView = findViewById(R.id.profileImageView);

        String _number = phoneNumb.substring(4, phoneNumb.length());
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(phoneNumb);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Query donorDetails = FirebaseDatabase.getInstance().getReference("DonationDetails").orderByChild("phoneNumber").equalTo(phoneNumb);
                    donorDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            if (snapshot2.exists()){
                                String _fullName = snapshot.child(_number).child("fullName").getValue(String.class);
                                String _emailId = snapshot.child(_number).child("emailId").getValue(String.class);
                                String _gender = snapshot.child(_number).child("gender").getValue(String.class);
                                String _dob = snapshot.child(_number).child("d_o_b").getValue(String.class);
                                String _img = snapshot.child(_number).child("profileImg").getValue(String.class);

                                String _bloodGroup = snapshot2.child(phoneNumb).child("blGroup").getValue(String.class);
                                personName.setText(_fullName);
                                personGender.setText(_gender);
                                personBlood.setText(_bloodGroup);
                                Glide.with(ProfileView.this).load(_img).into(profileImageView);
                            }
                            else{
                                Query recipientDetails = FirebaseDatabase.getInstance().getReference("RecipientDetails").orderByChild("phoneNumber").equalTo(phoneNumb);
                                recipientDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot3) {
                                        if (snapshot3.exists()){
                                            String _fullName = snapshot.child(_number).child("fullName").getValue(String.class);
                                            String _emailId = snapshot.child(_number).child("emailId").getValue(String.class);
                                            String _gender = snapshot.child(_number).child("gender").getValue(String.class);
                                            String _dob = snapshot.child(_number).child("d_o_b").getValue(String.class);
                                            String _img = snapshot.child(_number).child("profileImg").getValue(String.class);

                                            String _bloodGroup = snapshot3.child(phoneNumb).child("bGroupRecipient").getValue(String.class);
                                            personName.setText(_fullName);
                                            personGender.setText(_gender);
                                            personBlood.setText(_bloodGroup);
                                            Glide.with(ProfileView.this).load(_img).into(profileImageView);
                                        }
                                        else {
                                            Toast.makeText(ProfileView.this, "recipient not found", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    Toast.makeText(ProfileView.this,  "no such user ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileView.this, "Something issue in extracting", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
