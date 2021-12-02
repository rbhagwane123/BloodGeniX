package com.example.bloodgenix;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BloodDnonorSearch extends AppCompatActivity {

    EditText searchBar;
    RecyclerView searchRecycler;
    String text;
    ArrayList<DonationDetails> donationList;
    ImageButton searchBtn;
    myAdapter adapter;


    FirebaseDatabase database;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_dnonor_search);

        searchBar = findViewById(R.id.searchBar);
        searchRecycler = findViewById(R.id.searchRecycler);


        FirebaseRecyclerOptions<DonationDetails> options = new FirebaseRecyclerOptions.Builder<DonationDetails>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("DonationDetails"),DonationDetails.class)
                .build();
        adapter = new myAdapter(options);
        adapter.startListening();
        searchRecycler.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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

    private void filter(String text) {

        FirebaseRecyclerOptions<DonationDetails> options = new FirebaseRecyclerOptions.Builder<DonationDetails>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("DonationDetails").orderByChild("donorLocation").startAt(text).endAt(text+"\uf8ff"),DonationDetails.class)
                .build();

        adapter = new myAdapter(options);
        adapter.startListening();
        searchRecycler.setAdapter(adapter);
    }
}