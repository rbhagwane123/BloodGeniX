package com.example.bloodgenix.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.bloodgenix.Models.PersonStatus;
import com.example.bloodgenix.R;
import com.example.bloodgenix.SessionManager;
import com.example.bloodgenix.Settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
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
    ImageButton moreInfo;
    ImageView fullProfile;
    String personImage;


    BottomNavigationView bottomNavigation;
    NavController navController;


    //FireBase initialisation
    FirebaseDatabase database;
    DatabaseReference reference;
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
//        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance("https://bloodgenix-bb937-default-rtdb.firebaseio.com/");

        toggle = new ActionBarDrawerToggle(DashBoard_Screen.this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        layout_1 = findViewById(R.id.layout_1);
        Logout = findViewById(R.id.Logout);
        moreInfo = findViewById(R.id.moreInfo);

        //Extracting values
        Intent val = getIntent();
        full_number = val.getStringExtra("profile Values");
        profileDetailsFetch(full_number);

        dialogCreationMore();
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

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

        //Creating Status of person
        createStatus(full_number);


        SessionManager sessionManager = new SessionManager(this,"userLoginSession");
        HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();

        String fullName = userDetails.get(SessionManager.KEY_FULLNAME);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.profile_view:
                        Intent profileIntent = new Intent(DashBoard_Screen.this, User_Profile_View.class);
                        profileIntent.putExtra("mobile number", full_number);
                        startActivity(profileIntent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(DashBoard_Screen.this, Settings.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_about:
                        startActivity(new Intent(DashBoard_Screen.this, About_activity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_sharing:

                        ApplicationInfo api = getApplicationContext().getApplicationInfo();
                        String apkpath = api.sourceDir;
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("application/vnd.android.package-archive");
                        shareIntent.putExtra("AppSharing", Uri.fromFile(new File(apkpath)));
                        startActivity(Intent.createChooser(shareIntent, "ShareVia"));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    default:
                        return false;
                }

                return false;
            }
        });
//
//
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("status", "false");
                FirebaseDatabase.getInstance().getReference().child("PersonStatus").child(full_number).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
//                            logoutUserFromSession
                            SessionManager sessionManager = new SessionManager(DashBoard_Screen.this,"userLoginSession");
                            HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();

                            Intent logout = new Intent(DashBoard_Screen.this, Welcome.class);
                            sessionManager.logoutUserFromSession();
                            startActivity(logout);
                            finish();
                        }
                    }
                });
            }
        });
    }

    private void createStatus(String full_number) {
        if (isNetworkConnected()){
            reference = database.getReference().child("PersonStatus").child(full_number);
            PersonStatus status = new PersonStatus(full_number, "true");
            reference.setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                    }else{
                        Toast.makeText(DashBoard_Screen.this, "doesn't Added Status", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            reference = database.getReference().child("PersonStatus").child(full_number);
            PersonStatus status = new PersonStatus(full_number, "false");
            reference.setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                    }else{
                        Toast.makeText(DashBoard_Screen.this, "doesn't Added Status", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void dialogCreationMore() {
//        ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        dialog = new Dialog(DashBoard_Screen.this);
        dialog.setContentView(R.layout.info_layout);
        dialog.getWindow().setLayout(1000,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.info_layout_background_style));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.setCanceledOnTouchOutside(true);

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