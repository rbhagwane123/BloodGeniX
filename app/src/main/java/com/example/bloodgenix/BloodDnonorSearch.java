package com.example.bloodgenix;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class BloodDnonorSearch extends AppCompatActivity {

    EditText searchBar;
    RecyclerView searchRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_dnonor_search);

        searchBar = findViewById(R.id.searchBar);
        searchRecycler = findViewById(R.id.searchRecycler);

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BloodDnonorSearch.this, searchBar.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
    }
}