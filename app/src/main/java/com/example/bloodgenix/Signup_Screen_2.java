package com.example.bloodgenix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

public class Signup_Screen_2 extends AppCompatActivity {

    private AutoCompleteTextView gender;
    ArrayAdapter <String> arrayAdapter;
    DatePicker datePicker;
    ImageButton nextButton_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen2);

        gender = findViewById(R.id.Gender);
        String [] gender_val= getResources().getStringArray(R.array.gender);
        arrayAdapter= new ArrayAdapter<String>(this,R.layout.dropdown_item_gender,gender_val);
        gender.setAdapter(arrayAdapter);
        gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(Signup_Screen_2.this, item, Toast.LENGTH_SHORT).show();
            }
        });

        datePicker = findViewById(R.id.Dob);
        nextButton_2 = findViewById(R.id.nextButton_2);
        nextButton_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Toast.makeText(Signup_Screen_2.this, day+" "+month+" "+year, Toast.LENGTH_SHORT).show();
            }
        });
    }
}