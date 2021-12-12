package com.example.bloodgenix.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bloodgenix.Models.RecipientDetails;
import com.example.bloodgenix.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RecipientForm extends AppCompatActivity {

    AutoCompleteTextView bloodGroupRecipient, reasonBlood;
    TextInputEditText locationRecipient, requirementRecipient;
    ArrayAdapter<String> AdapterBGroupRecipient, AdapterReasonBlood;
    String bGroup[], reason[];
    CheckBox authoriseRecipient;
    Button applyRecipient;
    ImageButton cancelBtnRecipient;
    String phoneNumb;
    Dialog dialog;

    FusedLocationProviderClient fusedLocationProviderClient;

    //FireBase variable declaration
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecipientDetails recipientDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_form);

        Intent i5 =  getIntent();
        phoneNumb = i5.getStringExtra("Recipient");


        bloodGroupRecipient = findViewById(R.id.bloodGroupRecipient);
        locationRecipient = findViewById(R.id.locationRecipient);
        authoriseRecipient = findViewById(R.id.authoriseRecipient);
        applyRecipient = findViewById(R.id.applyRecipient);
        cancelBtnRecipient = findViewById(R.id.cancelBtnRecipient);
        requirementRecipient = findViewById(R.id.requirementRecipient);
        reasonBlood = findViewById(R.id.reasonBlood);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Blood group drop down code
        bGroup = getResources().getStringArray(R.array.bloodgroup);
        AdapterBGroupRecipient = new ArrayAdapter<String>(this, R.layout.items_list_blood, bGroup);
        bloodGroupRecipient.setAdapter(AdapterBGroupRecipient);

        //Reason Drop down coding
        reason = getResources().getStringArray(R.array.resonBlood);
        AdapterReasonBlood = new ArrayAdapter<String>(this,R.layout.item_list_reason,reason);
        reasonBlood.setAdapter(AdapterReasonBlood);

        //CURRENT LOCATION FETCHING
        if (ActivityCompat.checkSelfPermission(RecipientForm.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(RecipientForm.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses.size()>0){
                                for (Address adr: addresses){
                                    if (adr.getLocality() != null && adr.getLocality().length()>0){
                                        locationRecipient.setText(adr.getLocality());
                                        break;
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else {
            ActivityCompat.requestPermissions(RecipientForm.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        cancelBtnRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donation = new Intent(RecipientForm.this,DashBoard_Screen.class);
                donation.putExtra("mobile number",phoneNumb);
                startActivity(donation);
            }
        });


        //SUCCESS LAYOUT VIEWING
        dialog = new Dialog(RecipientForm.this);
        dialog.setContentView(R.layout.custom_success_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_success_animation));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button Okay, Cancel;
        Okay = dialog.findViewById(R.id.Okay);
        Cancel = dialog.findViewById(R.id.Cancel);

        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                auth = FirebaseAuth.getInstance();
                reference = database.getReference().child("RecipientDetails").child(phoneNumb);
                recipientDetails = new RecipientDetails(auth.getUid(), bloodGroupRecipient.getText().toString(), locationRecipient.getText().toString(), requirementRecipient.getText().toString(),reasonBlood.getText().toString(),phoneNumb);
                reference.setValue(recipientDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent searchPage = new Intent(RecipientForm.this, BloodDnonorSearch.class);
                            searchPage.putExtra("Blood Group",bloodGroupRecipient.getText().toString());
                            startActivity(searchPage);
                            dialog.dismiss();
                        }else{
                            dialog.dismiss();
                            Toast.makeText(RecipientForm.this, "Try Again !..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        applyRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bloodGroupRecipient.getText().toString().isEmpty()){
                    bloodGroupRecipient.setError("Please Select");
                }
                else if(requirementRecipient.getText().toString().isEmpty()){
                    requirementRecipient.setError("Field left empty");
                }
                else if(reasonBlood.getText().toString().isEmpty()){
                    reasonBlood.setError("Please Select");
                }
                else{
                    bloodGroupRecipient.setError(null);
                    requirementRecipient.setError(null);
                    reasonBlood.setError(null);

                    Query donationDetails = FirebaseDatabase.getInstance().getReference("DonationDetails").orderByChild("phoneNumber").equalTo(phoneNumb);
                    donationDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Toast.makeText(RecipientForm.this, "You have applied for Donation", Toast.LENGTH_SHORT).show();
                            }else{
                                dialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    public void authorise(View view){
        if (authoriseRecipient.isChecked() == true) {
            applyRecipient.setEnabled(true);
        } else {
            applyRecipient.setEnabled(false);
        }
    }

}