package com.example.bloodgenix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoard_Screen extends AppCompatActivity {

    TextView salutationText;
    CircleImageView active_person;
    Button donationExpand, RecipientExpand, Logout;
    View layout_1;

    Bitmap bitmap;

    //slider layout initialisation
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CircleImageView sliderProfile;
    TextView sliderName, sliderEmail;

    //Bottom layout initialisation
    ChipNavigationBar bottom_nav_menu;

    //FireBase initialisation
    StorageReference storageReference;

    public String full_number;
    public String fetch_details [] = new String[15];
    public String donationAdding [] = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_screen);

        // Setting values to components
        salutationText = findViewById(R.id.salutationText);
        active_person = findViewById(R.id.active_person);
        donationExpand = findViewById(R.id.donationExpand);
        RecipientExpand = findViewById(R.id.RecipientExpand);
        Logout = findViewById(R.id.Logout);
        layout_1 = findViewById(R.id.layout_1);

        //Extracting values
        Intent val = getIntent();
        full_number = val.getStringExtra("profile Values");
        profileDetailsFetch(full_number);

        //drawer layout coding
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(DashBoard_Screen.this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View hView =  navigationView.getHeaderView(0);
        sliderProfile = (CircleImageView) hView.findViewById(R.id.sliderProfile);
        sliderEmail = (TextView) hView.findViewById(R.id.sliderEmail);
        sliderName = (TextView) hView.findViewById(R.id.sliderName);;


        active_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(DashBoard_Screen.this,ProfileView.class);
                profile.putExtra("mobile number",full_number);
                startActivity(profile);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(DashBoard_Screen.this,Welcome.class);
                startActivity(logout);
                finish();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_notification:
                        Toast.makeText(DashBoard_Screen.this, "Notification area", Toast.LENGTH_SHORT).show();

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_settings:
                        Toast.makeText(DashBoard_Screen.this, "Settings area", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_about:
                        Toast.makeText(DashBoard_Screen.this, "About area", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_sharing:
                        Toast.makeText(DashBoard_Screen.this, "Sharing area", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        //setting click listener over buttons
        donationExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donationIntent = new Intent(DashBoard_Screen.this,DonationForm.class);
                donationAdding [0] = fetch_details[0];
                donationAdding [1] = full_number;
                donationAdding [2] = fetch_details[5];
                donationIntent.putExtra("Donation",donationAdding);
                startActivity(donationIntent);
            }
        });

        RecipientExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RecipientIntent = new Intent(DashBoard_Screen.this,RecipientForm.class);
                RecipientIntent.putExtra("Recipient",full_number);
                startActivity(RecipientIntent);
            }
        });

    }


    //Fetching Details from FireBase
    public void profileDetailsFetch(String full_number){
        String _number = full_number.substring(4,full_number.length());
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(full_number);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String _fullName = snapshot.child(_number).child("fullName").getValue(String.class);
                    String _emailId = snapshot.child(_number).child("emailId").getValue(String.class);
                    String _gender = snapshot.child(_number).child("gender").getValue(String.class);
                    String _dob = snapshot.child(_number).child("d_o_b").getValue(String.class);
                    String _img = snapshot.child(_number).child("profileImg").getValue(String.class);

                    fetch_details [0] = _fullName;
                    fetch_details [1] = _emailId;
                    fetch_details [2] =_dob;
                    fetch_details [3] =_gender;
                    fetch_details [4] = _number;
                    fetch_details [5] = _img;

                    salutationText.setText("Hey there,\n"+fetch_details[0]);
                    Glide.with(DashBoard_Screen.this).load(fetch_details[5]).into(active_person);
                    Glide.with(layout_1).load(fetch_details[5]).into(sliderProfile);
                    String[] userName=_fullName.split("\\s");
                    sliderName.setText(_fullName);
                    sliderEmail.setText(_emailId);

                }
                else{
                    Toast.makeText(DashBoard_Screen.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashBoard_Screen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();        }
    }
}