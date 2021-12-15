package com.example.bloodgenix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bloodgenix.R;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class User_Profile_View extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, View.OnClickListener {

    ImageView userProfileImage;
    ImageButton backBtn, dotMenuBtn;
    TextView userProfileName, ProfileUserName, toolbarName;
    TextView userProfileBlood, userPersonBleed, userProfileLocation;
    TextView userProfileContact, userProfileEmail, userProfileDOB;

    //EDIT VIEWS DECLARATION
    ImageButton BloodBtn, BleedBtn, ContactBtn;
    ImageButton EmailBtn, DOBBtn, cancel_button, locationBtn;
    Button editBtn;
    String mobileNumber;
    int donorFlag = 0, recipientFlag = 0;

    //BOTTOM SHEET DECLARATION
    BottomSheetDialog sheetDialog2;
    TextInputEditText editView;
    TextInputLayout textInputLayout, textInputLayout2;
    AutoCompleteTextView autoView;

    //FireBase declaration
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);

        viewInitialisation();
        btnviewInitialisation();
        Intent profile = getIntent();
        mobileNumber = profile.getStringExtra("mobile number");
        reference = FirebaseDatabase.getInstance().getReference();

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
        backBtn = findViewById(R.id.bacBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(User_Profile_View.this, DashBoard_Screen.class);
                back.putExtra("profile Values", mobileNumber);
                startActivity(back);
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
                    toolbarName.setText(_fullName);
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
                        }
                    }
                } else {
                    Toast.makeText(User_Profile_View.this, "no person found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonListener();

    }

    private void buttonListener() {
        BloodBtn.setOnClickListener(this);
        BloodBtn.setOnClickListener(this);
        BleedBtn.setOnClickListener(this);
        ContactBtn.setOnClickListener(this);
        EmailBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        DOBBtn.setOnClickListener(this);
    }

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
        textInputLayout.setVisibility(View.VISIBLE);

        sheet.show();
        cancel_button.setOnClickListener(v1 -> sheet.dismiss());
        return sheet;
    }

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

    private void btnviewInitialisation() {
        BloodBtn = findViewById(R.id.BloodBtn);
        BleedBtn = findViewById(R.id.BleedBtn);
        ContactBtn = findViewById(R.id.ContactBtn);
        EmailBtn = findViewById(R.id.EmailBtn);
        locationBtn = findViewById(R.id.locationBtn);
        DOBBtn = findViewById(R.id.DOBBtn);
    }

    @Override
    public void onClick(View v) {
        ArrayAdapter<String> arrayAdapter;
        String auto[];
        switch (v.getId()) {

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
                sheetDialog2 = BottomSheet();
                textInputLayout.setHint("Contact");

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
                textInputLayout.setHint("Birth Date");

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("d_o_b", editView.getText().toString());
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
            default:
                break;
        }
    }


    private int chckRecipient() {
        Query recipientDetails = FirebaseDatabase.getInstance().getReference("RecipientDetails").orderByChild("phoneNumber").equalTo(mobileNumber);
        recipientDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot3) {
                if (snapshot3.exists()) {
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

            }
        });
        return recipientFlag;
    }

    private int chckDonor() {

        Query donorDetails = FirebaseDatabase.getInstance().getReference("DonationDetails").orderByChild("phoneNumber").equalTo(mobileNumber);
        donorDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                if (snapshot2.exists()) {
                    String _bloodGroup = snapshot2.child(mobileNumber).child("blGroup").getValue(String.class);
                    String _bleed = snapshot2.child(mobileNumber).child("donateMonth").getValue(String.class);
                    String _location = snapshot2.child(mobileNumber).child("donorLocation").getValue(String.class);
                    userProfileBlood.setText(_bloodGroup);
                    userProfileLocation.setText(_location);
                    if (_bleed.length() <= 0) {
                        userPersonBleed.setText("No donation");

                    } else {
                        userPersonBleed.setText(_bleed);
                    }
                    donorFlag = 1;
                } else {
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
                Toast.makeText(User_Profile_View.this, "forgot Password", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;
        }
    }


}