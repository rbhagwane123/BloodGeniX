package com.example.bloodgenix.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.bloodgenix.R;
import com.example.bloodgenix.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoard_Screen extends AppCompatActivity {

    TextView salutationText;
    CircleImageView active_person;
    Button donationExpand, RecipientExpand, Logout;
    View layout_1, homeLayout;

    //slider layout initialisation
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    Toolbar toolbar;
    CircleImageView sliderProfile;
    TextView sliderName;
    Dialog dialog;
    ImageView fullProfile;
    String personImage;

    BottomNavigationView bottomNavigation;
    NavController navController;


    //FireBase initialisation
    StorageReference storageReference;

    public String full_number;
    public String fetch_details[] = new String[15];
    public String donationAdding[] = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_screen);


        //BOTTOM NAVIGATION IMPLEMENTATION
        bottomNavigation = findViewById(R.id.bottom_navigation);
        navController = Navigation.findNavController(this, R.id.main_fragment);
        NavigationUI.setupWithNavController(bottomNavigation, navController);



        //DRAWER LAYOUT IMPLEMENTATION
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toggle = new ActionBarDrawerToggle(DashBoard_Screen.this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        layout_1 = findViewById(R.id.layout_1);
        Logout = findViewById(R.id.Logout);


        View hView = navigationView.getHeaderView(0);
        sliderProfile = (CircleImageView) hView.findViewById(R.id.sliderProfile);
        sliderName = (TextView) hView.findViewById(R.id.sliderName);

        sliderProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCreation();
                dialog.show();
            }
        });

        //Extracting values
        Intent val = getIntent();
        full_number = val.getStringExtra("profile Values");
        profileDetailsFetch(full_number);

        SessionManager sessionManager = new SessionManager(this,"userLoginSession");
        HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();

        String fullName = userDetails.get(SessionManager.KEY_FULLNAME);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_notification:
                        Toast.makeText(DashBoard_Screen.this, "Notification selected", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_settings:
                        Toast.makeText(DashBoard_Screen.this, "Setting selected", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_about:
                        Toast.makeText(DashBoard_Screen.this, "About selected", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_sharing:
                        Toast.makeText(DashBoard_Screen.this, "Sharing selected", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    default:
                        return false;
                }

                return false;
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(DashBoard_Screen.this, Welcome.class);
                startActivity(logout);
                finish();
            }
        });



    }

    public String myData() {
        return full_number;
    }

    //Fetching Details from FireBase
    public void profileDetailsFetch(String full_number) {
        String _number = full_number.substring(4, full_number.length());
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(full_number);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String _fullName = snapshot.child(_number).child("fullName").getValue(String.class);
                    String _emailId = snapshot.child(_number).child("emailId").getValue(String.class);
                    String _gender = snapshot.child(_number).child("gender").getValue(String.class);
                    String _dob = snapshot.child(_number).child("d_o_b").getValue(String.class);
                    String _img = snapshot.child(_number).child("profileImg").getValue(String.class);

                    fetch_details[0] = _fullName;
                    fetch_details[1] = _emailId;
                    fetch_details[2] = _dob;
                    fetch_details[3] = _gender;
                    fetch_details[4] = _number;
                    fetch_details[5] = _img;

                    personImage = fetch_details[5];
                    Glide.with(layout_1).load(fetch_details[5]).into(sliderProfile);
                    sliderName.setText(_fullName);

                } else {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void dialogCreation() {
//        ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        dialog = new Dialog(DashBoard_Screen.this);
        dialog.setContentView(R.layout.image_click_zoom_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.info_layout_background_style));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.setCanceledOnTouchOutside(true);
        fullProfile = dialog.findViewById(R.id.fullProfile);
        Glide.with(getApplicationContext()).load(personImage).into(fullProfile);
    }
}