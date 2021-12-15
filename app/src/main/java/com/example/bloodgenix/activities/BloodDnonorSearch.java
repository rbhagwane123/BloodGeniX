package com.example.bloodgenix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodgenix.myAdapter;
import com.example.bloodgenix.Models.DonationDetails;
import com.example.bloodgenix.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class BloodDnonorSearch extends AppCompatActivity {

    EditText searchBar;
    RecyclerView searchRecycler;
    String text, myNumber, bloodGroupSearch;
    ArrayList<DonationDetails> donationList;
    ImageButton searchBtn;
    myAdapter adapter;


    FirebaseDatabase database;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_dnonor_search);

        Intent searchGroup = getIntent();
        bloodGroupSearch = searchGroup.getStringExtra("Blood Group");

        searchBar = findViewById(R.id.searchBar);
        searchRecycler = findViewById(R.id.searchRecycler);

        FirebaseRecyclerOptions<DonationDetails> options = new FirebaseRecyclerOptions.Builder<DonationDetails>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("DonationDetails").orderByChild("blGroup").startAt(bloodGroupSearch).endAt(bloodGroupSearch+"\uf8ff"),DonationDetails.class)
                .build();
        adapter = new myAdapter(options);
        adapter.startListening();
        searchRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new myAdapter.OnItemClickListener() {
            @Override
            public String OnItemClick(int position, String number) {
                Toast.makeText(BloodDnonorSearch.this, number, Toast.LENGTH_SHORT).show();
                Intent profileView = new Intent(BloodDnonorSearch.this, ProfileView.class);
                profileView.putExtra("mobile number",number);
                startActivity(profileView);
                return number;
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                FirebaseRecyclerOptions<DonationDetails> options = new FirebaseRecyclerOptions.Builder<DonationDetails>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("DonationDetails").orderByChild("blGroup").startAt(bloodGroupSearch).endAt(bloodGroupSearch+"\uf8ff"),DonationDetails.class)
                        .build();
                adapter = new myAdapter(options);
                adapter.startListening();
                searchRecycler.setAdapter(adapter);
                adapter.setOnItemClickListener(new myAdapter.OnItemClickListener() {
                    @Override
                    public String OnItemClick(int position, String number) {
                        Toast.makeText(BloodDnonorSearch.this, number, Toast.LENGTH_SHORT).show();
                        Intent profileView = new Intent(BloodDnonorSearch.this, ProfileView.class);
                        profileView.putExtra("mobile number",number);
                        startActivity(profileView);
                        return number;
                    }
                });
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });
    }

    private int  checkDonorAvailability() {
        return 0;
    }

    private void filter(String text) {

        Query searchLocationDonor = FirebaseDatabase.getInstance().getReference().child("DonationDetails").orderByChild("donorLocation").startAt(text).endAt(text+"\uf8ff");
        if (searchLocationDonor != null){
            FirebaseRecyclerOptions<DonationDetails> options = new FirebaseRecyclerOptions.Builder<DonationDetails>()
                    .setQuery(searchLocationDonor, DonationDetails.class)
                    .build();

            adapter = new myAdapter(options);
            adapter.startListening();
            searchRecycler.setAdapter(adapter);
            adapter.setOnItemClickListener(new myAdapter.OnItemClickListener() {
                @Override
                public String OnItemClick(int position, String number) {
                    Toast.makeText(BloodDnonorSearch.this, number, Toast.LENGTH_SHORT).show();
                    Intent profileView = new Intent(BloodDnonorSearch.this, ProfileView.class);
                    profileView.putExtra("mobile number",number);
                    startActivity(profileView);
                    return number;
                }
            });
        }else{
            Toast.makeText(BloodDnonorSearch.this, "No Person found", Toast.LENGTH_SHORT).show();
        }

    }
}