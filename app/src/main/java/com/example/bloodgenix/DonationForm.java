package com.example.bloodgenix;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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


public class DonationForm extends AppCompatActivity {

    TextInputLayout diabeticCountLayout, PeriodLayout;
    RadioGroup radioGroup, radioGroup2;
    RadioButton radioButton, radioButton2;
    ImageButton cancelBtn;
    Button applyDonation;
    TextInputEditText otherSpecify, locationDonation;
    CheckBox authorise;
    Dialog dialog;

    AutoCompleteTextView bloodGroup, diabeticCount, DonationPeriod;
    ArrayAdapter<String> arrayAdapterBGroup, arrayAdapterBCount, arrayAdapterDPeriod;
    String phoneNumber;
    String bGroup[], DiabeticCount[], Weight[], DPeriod[];
    public String formFillData [];
    NumberPicker weight;
    FusedLocationProviderClient fusedLocationProviderClientDon;

    //FireBase variables
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_form);

        //getting values from previous activities
        Intent i8 = getIntent();
        formFillData = i8.getStringArrayExtra("Donation");
        phoneNumber = formFillData[1];

        //giving id to the respective views
        diabeticCountLayout = findViewById(R.id.diabeticCountLayout);
        locationDonation = findViewById(R.id.locationDonation);
        PeriodLayout = findViewById(R.id.PeriodLayout);
        bloodGroup = findViewById(R.id.bloodGroup);
        diabeticCount = findViewById(R.id.diabeticCount);
        radioGroup = findViewById(R.id.radioGroup);
        cancelBtn = findViewById(R.id.cancelBtn);
        DonationPeriod = findViewById(R.id.DonationPeriod);
        radioGroup2 = findViewById(R.id.radioGroup2);
        applyDonation = findViewById(R.id.applyDonation);
        otherSpecify = findViewById(R.id.otherSpecify);
        authorise = findViewById(R.id.authorise);
        weight = findViewById(R.id.weight);


        // Setting weight max and min values
        weight.setMinValue(50);
        weight.setMaxValue(100);

        //SETTING CURRENT LOCATION FOR PERSON
        try{
            fusedLocationProviderClientDon = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(DonationForm.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClientDon.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(DonationForm.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses.size()>0){
                                    for (Address adr: addresses){
                                        if (adr.getLocality() != null && adr.getLocality().length()>0){
                                            locationDonation.setText(adr.getLocality());
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
                ActivityCompat.requestPermissions(DonationForm.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
        catch (Exception e){
            Toast.makeText(DonationForm.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dash = new Intent(DonationForm.this, DashBoard_Screen.class);
                dash.putExtra("profile Values", phoneNumber);
                startActivity(dash);
            }
        });

        //BLOOD GROUP DROP DOWN
        bGroup = getResources().getStringArray(R.array.bloodgroup);
        arrayAdapterBGroup = new ArrayAdapter<String>(this, R.layout.items_list_blood, bGroup);
        bloodGroup.setAdapter(arrayAdapterBGroup);

        //DIABETIC COUNT DROP DOWN
        DiabeticCount = getResources().getStringArray(R.array.diabeticCount);
        arrayAdapterBCount = new ArrayAdapter<String>(this, R.layout.diabetic_cout_dropdown, DiabeticCount);
        diabeticCount.setAdapter(arrayAdapterBCount);
        diabeticCount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if (value.equals("More than 8")){
                    Toast.makeText(DonationForm.this, "You are much diabetic", Toast.LENGTH_SHORT).show();
                    diabeticCount.setText(null);
                }
            }
        });

        //DONATION PERIOD DROP DOWN
        DPeriod = getResources().getStringArray(R.array.periodMonth);
        arrayAdapterDPeriod = new ArrayAdapter<String>(this, R.layout.donation_period_dropdown, DPeriod);
        DonationPeriod.setAdapter(arrayAdapterDPeriod);
        DonationPeriod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dValue= parent.getItemAtPosition(position).toString();
                if (dValue.equals("Less than 3 month")){
                    Toast.makeText(DonationForm.this, "Your donation period not completed", Toast.LENGTH_SHORT).show();
                    DonationPeriod.setText(null);
                }
            }
        });

        //SUCCESS LAYOUT VIEWING
        dialogInitialise();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                if (radioButton.getText().toString().equals("yes")) {
                    diabeticCountLayout.setEnabled(true);
                }
                else {
                    diabeticCountLayout.setEnabled(false);
                    diabeticCount.setText(null);
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton2 = findViewById(checkedId);
                if (radioButton2.getText().toString().equals("yes")) {
                    PeriodLayout.setEnabled(true);
                } else {
                    PeriodLayout.setEnabled(false);
                    DonationPeriod.setText(null);
                }
            }
        });

        applyDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = checkValidations();
                if (result == 1){
                    bloodGroup.setError(null);
                    diabeticCount.setError(null);
                    otherSpecify.setError(null);
                    DonationPeriod.setError(null);

                    //CHECKING FOR THE USER IS APPLIED FOR DONATION OR NOT
                    Query recipientDetails = FirebaseDatabase.getInstance().getReference("RecipientDetails").orderByChild("phoneNumber").equalTo(phoneNumber);
                    recipientDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Toast.makeText(DonationForm.this, "You have applied for recipient before", Toast.LENGTH_SHORT).show();
                            }else{
                                String wight = String.valueOf(weight.getValue());
                                database = FirebaseDatabase.getInstance();
                                auth = FirebaseAuth.getInstance();
                                reference = database.getReference().child("DonationDetails").child(phoneNumber);
                                DonationDetails donationDetails = new DonationDetails(auth.getUid(),phoneNumber,bloodGroup.getText().toString(),radioButton.getText().toString(),diabeticCount.getText().toString(),otherSpecify.getText().toString(),radioButton2.getText().toString(),DonationPeriod.getText().toString(),wight,locationDonation.getText().toString(),formFillData[0], formFillData[2] );
                                reference.setValue(donationDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dialog.show();
                                        }
                                        else{
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(DonationForm.this, "Any field left empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //DIALOG BOX INITIALISATION
    public void dialogInitialise(){
        dialog = new Dialog(DonationForm.this);
        dialog.setContentView(R.layout.custom_success_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_success_animation));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button Okay, Cancel;
        TextView dialogType, dialogDesc;
        Okay = dialog.findViewById(R.id.Okay);
        Cancel = dialog.findViewById(R.id.Cancel);
        dialogType = dialog.findViewById(R.id.dialogType);
        dialogDesc = dialog.findViewById(R.id.dialogDesc);

        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(DonationForm.this,BloodDnonorSearch.class);
                search.putExtra("Blood Group",bloodGroup.getText().toString());
                startActivity(search);
                dialog.dismiss();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    //VALIDATION CHECKING FOR EMPTY FIELDS
    public int checkValidations(){

        int flag=0;
        if (bloodGroup.getText().toString().isEmpty()) {
            bloodGroup.setError("Please select");
            bloodGroup.setFocusable(true);
            flag=0;
        }
        else if (otherSpecify.getText().toString().isEmpty()) {
            otherSpecify.setError("Please Enter");
            otherSpecify.setFocusable(true);
            flag=0;
        }
        else if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(DonationForm.this, "Please select", Toast.LENGTH_SHORT).show();
            flag=0;
        }
        else if (radioButton.getText().toString().equals("yes")) {
            if (diabeticCount.getText().toString().isEmpty()) {
                diabeticCount.setError("Please select");
                flag=0;
            }
            else{
                flag=1;
            }
        }
        else if (radioGroup2.getCheckedRadioButtonId() == -1) {
            Toast.makeText(DonationForm.this, "Please select", Toast.LENGTH_SHORT).show();
            flag=0;
        }
        else if (radioButton2.getText().toString().equals("yes")) {
            if (DonationPeriod.getText().toString().isEmpty()) {
                DonationPeriod.setError("Please select");
                flag=0;
            }
            else{
                flag=1;
            }
        }
        else{
            bloodGroup.setError(null);
            diabeticCount.setError(null);
            otherSpecify.setError(null);
            DonationPeriod.setError(null);
            flag=1;
        }
        return flag;

    }


    // function for applying enable and disable over apply button
    public void checkAuthorise(View view) {
        if (authorise.isChecked() == true) {
            applyDonation.setEnabled(true);
        } else {
            applyDonation.setEnabled(false);
        }
    }
}