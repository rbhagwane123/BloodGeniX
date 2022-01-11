package com.example.bloodgenix.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.bloodgenix.R;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Profile_View extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, View.OnClickListener {

    CircleImageView userProfileImage;
    ImageButton backBtn, dotMenuBtn;
    TextView userProfileName, ProfileUserName, toolbarName;
    TextView userProfileBlood, userPersonBleed, userProfileLocation;
    TextView userProfileContact, userProfileEmail, userProfileDOB;

    //EDIT VIEWS DECLARATION
    ImageButton BloodBtn, BleedBtn, ContactBtn;
    ImageButton EmailBtn, DOBBtn, cancel_button, locationBtn;
    ImageButton editBtn, picUpdate;

    LinearLayout bleedLayout;
    String mobileNumber;
    DatePicker Dob;
    int donorFlag =0, recipientFlag = 0;
    boolean updatePic = false;
    Uri profile_uri;
    CardView deleteChoice;

    //BOTTOM SHEET DECLARATION
    BottomSheetDialog sheetDialog2;
    TextInputEditText editView;
    TextInputLayout textInputLayout, textInputLayout2;
    AutoCompleteTextView autoView;


    //FireBase declaration
    FirebaseDatabase database;
    DatabaseReference reference;

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);

        viewInitialisation();
        btnviewInitialisation();
        Intent profile = getIntent();
        mobileNumber = profile.getStringExtra("mobile number");
        reference = FirebaseDatabase.getInstance().getReference();
        bleedLayout = findViewById(R.id.bleedLayout);
        deleteChoice = findViewById(R.id.deleteChoice);

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent i_again = new Intent(getApplicationContext(), User_Profile_View.class);
                i_again.putExtra("mobile number", mobileNumber);

                HashMap hashMapImg = new HashMap();
                if(updatePic == true){
                    hashMapImg.put("profileImg", profile_uri.toString());
                    FirebaseDatabase.getInstance().getReference().child("Users").child(mobileNumber.substring(4,mobileNumber.length())).updateChildren(hashMapImg).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(User_Profile_View.this, "Image added", Toast.LENGTH_SHORT).show();
                            updatePic = false;
                        }
                    });
                }
                startActivity(i_again);
            }
        });

        dotMenuBtn = findViewById(R.id.dotMenuBtn);
        dotMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(User_Profile_View.this, v);
                popupMenu.setOnMenuItemClickListener(User_Profile_View.this);
                popupMenu.inflate(R.menu.dot_menu);
                popupMenu.show();
            }
        });

        deleteChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chckDonor() == 1){
                    FirebaseDatabase.getInstance().getReference("DonationDetails").child(mobileNumber).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(User_Profile_View.this, "Donation deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if (chckRecipient() == 1){
                    FirebaseDatabase.getInstance().getReference("RecipientDetails").child(mobileNumber).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(User_Profile_View.this, "Recipient deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        backBtn = findViewById(R.id.bacBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(User_Profile_View.this, DashBoard_Screen.class);
                back.putExtra("profile Values", mobileNumber);
                startActivity(back);
                finish();
            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullView = new Intent(User_Profile_View.this,fullProfileView.class);
                fullView.putExtra("phone Number", mobileNumber);
                startActivity(fullView);
            }
        });

        String _number = mobileNumber.substring(4, mobileNumber.length());
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(mobileNumber);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String _fullName = snapshot.child(_number).child("fullName").getValue(String.class);
                    String _userName = snapshot.child(_number).child("userName").getValue(String.class);
                    String _emailId = snapshot.child(_number).child("emailId").getValue(String.class);
                    String _gender = snapshot.child(_number).child("gender").getValue(String.class);
                    String _dob = snapshot.child(_number).child("d_o_b").getValue(String.class);
                    String _img = snapshot.child(_number).child("profileImg").getValue(String.class);

                    userProfileName.setText(_fullName);
                    ProfileUserName.setText(_userName);
                    userProfileContact.setText(mobileNumber);
                    userProfileEmail.setText(_emailId);
                    userProfileDOB.setText(_dob);
                    Glide.with(User_Profile_View.this).load(_img).into(userProfileImage);
                    if (chckDonor() == 0) {
                        if (chckRecipient() == 0) {
                            userProfileBlood.setText("Not applied");
                            userPersonBleed.setText("Not applied");
                            userProfileLocation.setText("Not known");
                            deleteChoice.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                else {
                    Toast.makeText(User_Profile_View.this, "no person found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonListener();
    }

    //BUTTON LISTENERS CREATED
    private void buttonListener() {
        BloodBtn.setOnClickListener(this);
        BloodBtn.setOnClickListener(this);
        BleedBtn.setOnClickListener(this);
        ContactBtn.setOnClickListener(this);
        EmailBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        DOBBtn.setOnClickListener(this);
        picUpdate.setOnClickListener(this);

    }

    //BOTTOM SHEET DIALOG CREATION FOR EDITTEXT VALUES
    private BottomSheetDialog BottomSheet() {
        BottomSheetDialog sheet = new BottomSheetDialog(User_Profile_View.this, R.style.BottomSheetStyle);
        View view = LayoutInflater.from(User_Profile_View.this).inflate(R.layout.edit_bottom_layout, (LinearLayout) findViewById(R.id.sheet2));
        sheet.setContentView(view);

        //Initialising the views
        editView = sheet.findViewById(R.id.editView);
        editBtn = sheet.findViewById(R.id.editBtn);
        cancel_button = sheet.findViewById(R.id.cancel_button);
        textInputLayout = sheet.findViewById(R.id.textInputLayout);
        textInputLayout2 = sheet.findViewById(R.id.textInputLayout2);
        Dob = sheet.findViewById(R.id.Dob);
        textInputLayout.setVisibility(View.VISIBLE);

        sheet.show();
        cancel_button.setOnClickListener(v1 -> sheet.dismiss());
        return sheet;
    }

    //BOTTOM SHEET DIALOG CREATION FOR DROPDOWN
    private BottomSheetDialog BottomSheetDrop() {

        BottomSheetDialog sheet = new BottomSheetDialog(User_Profile_View.this, R.style.BottomSheetStyle);
        View view = LayoutInflater.from(User_Profile_View.this).inflate(R.layout.edit_bottom_layout, (LinearLayout) findViewById(R.id.sheet2));
        sheet.setContentView(view);

        //Initialising the views
        editView = sheet.findViewById(R.id.editView);
        editBtn = sheet.findViewById(R.id.editBtn);
        cancel_button = sheet.findViewById(R.id.cancel_button);
        autoView = sheet.findViewById(R.id.autoView);
        textInputLayout = sheet.findViewById(R.id.textInputLayout);
        textInputLayout2 = sheet.findViewById(R.id.textInputLayout2);
        textInputLayout2.setVisibility(View.VISIBLE);

        sheet.show();
        cancel_button.setOnClickListener(v1 -> sheet.dismiss());
        return sheet;
    }

    //INITIALISING BUTTONS VIEWS
    private void btnviewInitialisation() {
        BloodBtn = findViewById(R.id.BloodBtn);
        BleedBtn = findViewById(R.id.BleedBtn);
        ContactBtn = findViewById(R.id.ContactBtn);
        EmailBtn = findViewById(R.id.EmailBtn);
        locationBtn = findViewById(R.id.locationBtn);
        DOBBtn = findViewById(R.id.DOBBtn);
        picUpdate = findViewById(R.id.picUpdate);
    }

    //ON CLICK DEFINITION
    @Override
    public void onClick(View v) {
        ArrayAdapter<String> arrayAdapter;
        String auto[];
        switch (v.getId()) {

            case R.id.picUpdate:
                ImagePicker.Companion.with(User_Profile_View.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .cropOval()	    		//Allow dimmed layer to have a circle inside
                        .maxResultSize(1080,1080)
                        .start();
                break;

            case R.id.BloodBtn:
                sheetDialog2 = BottomSheetDrop();
                textInputLayout2.setHint("Blood Group");
                auto = getResources().getStringArray(R.array.bloodgroup);
                arrayAdapter = new ArrayAdapter<String>(this, R.layout.items_list_blood, auto);
                autoView.setAdapter(arrayAdapter);

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap hashMap = new HashMap();

                        if (chckDonor() == 1) {
                            hashMap.put("blGroup", autoView.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("DonationDetails").child(mobileNumber).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(User_Profile_View.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                                    textInputLayout2.setVisibility(View.INVISIBLE);
                                    sheetDialog2.dismiss();
                                }
                            });
                        } else if (chckRecipient() == 1) {
                            hashMap.put("bGroupRecipient", autoView.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("RecipientDetails").child(mobileNumber).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(User_Profile_View.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                                    textInputLayout2.setVisibility(View.INVISIBLE);
                                    sheetDialog2.dismiss();
                                }
                            });
                        }

                    }
                });
                break;
            case R.id.BleedBtn:
                sheetDialog2 = BottomSheetDrop();
                textInputLayout2.setHint("Last Donation Period");
                auto = getResources().getStringArray(R.array.periodMonth);
                arrayAdapter = new ArrayAdapter<String>(this, R.layout.donation_period_dropdown, auto);
                autoView.setAdapter(arrayAdapter);

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap hashMap = new HashMap();

                        if (chckDonor() == 1) {
                            hashMap.put("donateMonth", autoView.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("DonationDetails").child(mobileNumber).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(User_Profile_View.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                                    textInputLayout2.setVisibility(View.INVISIBLE);

                                    sheetDialog2.dismiss();
                                }
                            });
                        }else if (chckRecipient() == 1) {
                            Toast.makeText(User_Profile_View.this, "You are applied for blood", Toast.LENGTH_SHORT).show();
                            sheetDialog2.dismiss();
                        }
                        textInputLayout2.setEnabled(true);
                    }

                });
                break;
            case R.id.ContactBtn:
                Toast.makeText(User_Profile_View.this, "Cannot update contact", Toast.LENGTH_SHORT).show();
//                sheetDialog2 = BottomSheet();
//                textInputLayout.setHint("Contact");
                break;
            case R.id.locationBtn:
                sheetDialog2 = BottomSheet();
                textInputLayout.setHint("Location");

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap hashMap = new HashMap();

                        if (chckDonor() == 1) {
                            hashMap.put("donorLocation", editView.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("DonationDetails").child(mobileNumber).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(User_Profile_View.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                                    textInputLayout.setVisibility(View.INVISIBLE);

                                    sheetDialog2.dismiss();
                                }
                            });
                        }else if (chckRecipient() == 1) {
                            hashMap.put("locationRecipient", editView.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("RecipientDetails").child(mobileNumber).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(User_Profile_View.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                                    textInputLayout2.setVisibility(View.INVISIBLE);

                                    sheetDialog2.dismiss();
                                }
                            });
                        }
                    }

                });
                break;
            case R.id.EmailBtn:
                sheetDialog2 = BottomSheet();
                textInputLayout.setHint("Email");

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("emailId", editView.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("Users").child(mobileNumber.substring(4, mobileNumber.length())).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(User_Profile_View.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                                textInputLayout.setVisibility(View.INVISIBLE);

                                sheetDialog2.dismiss();
                            }
                        });
                    }
                });
                break;
            case R.id.DOBBtn:
                sheetDialog2 = BottomSheet();
                textInputLayout.setVisibility(View.INVISIBLE);
                Dob.setVisibility(View.VISIBLE);

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap hashMap = new HashMap();
                        int day = Dob.getDayOfMonth();
                        int month = Dob.getMonth()+1;
                        int year = Dob.getYear();
                        String dateOfBirth = day+"/"+month+"/"+year;
                        hashMap.put("d_o_b", dateOfBirth);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(mobileNumber.substring(4, mobileNumber.length())).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(User_Profile_View.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                                Dob.setVisibility(View.INVISIBLE);
                                sheetDialog2.dismiss();
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
    }


    //FUNCTION TO CHECK LOGON PERSON IS RECIPIENT
    private int chckRecipient() {
        Query recipientDetails = FirebaseDatabase.getInstance().getReference("RecipientDetails").orderByChild("phoneNumber").equalTo(mobileNumber);
        recipientDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot3) {
                if (snapshot3.exists()) {
                    bleedLayout.setVisibility(View.INVISIBLE);
                    String _bloodGroup = snapshot3.child(mobileNumber).child("bGroupRecipient").getValue(String.class);
                    String _location = snapshot3.child(mobileNumber).child("locationRecipient").getValue(String.class);
                    userProfileBlood.setText(_bloodGroup);

                    userProfileLocation.setText(_location);
                    recipientFlag = 1;
                } else {
                    recipientFlag = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                recipientFlag = 0;
            }
        });
        return recipientFlag;
    }

    //FUNCTION TO CHECK LOGON PERSON IS DONOR
    private int chckDonor() {

        Query donorDetails = FirebaseDatabase.getInstance().getReference("DonationDetails").orderByChild("phoneNumber").equalTo(mobileNumber);
        donorDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                if (snapshot2.exists()) {
                    bleedLayout.setVisibility(View.VISIBLE);
                    String _bloodGroup = snapshot2.child(mobileNumber).child("blGroup").getValue(String.class);
                    String _bleed = snapshot2.child(mobileNumber).child("donateMonth").getValue(String.class);
                    String _location = snapshot2.child(mobileNumber).child("donorLocation").getValue(String.class);
                    userProfileBlood.setText(_bloodGroup);
                    userProfileLocation.setText(_location);
                    userPersonBleed.setText(_bleed);

                    if (_bleed.length() <= 0) {
                        userPersonBleed.setText("No donation");
                    }
                    else {
                        userPersonBleed.setText(_bleed);
                    }
                    donorFlag = 1;
                }
                else {
                    donorFlag = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                donorFlag = 0;
            }
        });
        return donorFlag;
    }

    private void viewInitialisation() {
        toolbarName = findViewById(R.id.toolbarName);
        userProfileImage = findViewById(R.id.userProfileImage);
        userProfileName = findViewById(R.id.userProfileName);
        ProfileUserName = findViewById(R.id.ProfileUserName);
        userProfileBlood = findViewById(R.id.userProfileBlood);
        userPersonBleed = findViewById(R.id.userPersonBleed);
        userProfileLocation = findViewById(R.id.userProfileLocation);
        userProfileContact = findViewById(R.id.userProfileContact);
        userProfileEmail = findViewById(R.id.userProfileEmail);
        userProfileDOB = findViewById(R.id.userProfileDOB);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.forgotPassword:
                Intent forgotPage = new Intent(getApplicationContext(), ForgetPassword.class);
                forgotPage.putExtra("whatToDo","UserProfile");
                forgotPage.putExtra("mobile Number", mobileNumber);
                startActivity(forgotPage);
                finish();
                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profile_uri = data.getData();
        userProfileImage.setImageURI(profile_uri);
        updatePic = true;


    }
}