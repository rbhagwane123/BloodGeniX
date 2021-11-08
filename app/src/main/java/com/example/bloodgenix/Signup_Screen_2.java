package com.example.bloodgenix;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Signup_Screen_2 extends AppCompatActivity {

    private AutoCompleteTextView gender;
    ArrayAdapter <String> arrayAdapter;
    DatePicker datePicker;
    ImageButton docSelection;
    ImageButton nextButton_2;
    TextInputEditText docText;
    TextInputLayout docLayout;
    int Requestcode =1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen2);

//        for gender selection code
        gender = findViewById(R.id.Gender);
        String [] gender_val= getResources().getStringArray(R.array.gender);
        arrayAdapter= new ArrayAdapter<String>(this,R.layout.dropdown_item_gender,gender_val);
        gender.setAdapter(arrayAdapter);
        gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(Signup_Screen_2.this, item, Toast.LENGTH_SHORT).show();
            }
        });
        Context context = getApplicationContext();
        datePicker = findViewById(R.id.Dob);

//        file Uploading code
        docText = findViewById(R.id.docText);
        docLayout = findViewById(R.id.docLayout);

        docSelection = findViewById(R.id.docSelection);
        docSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,Requestcode);
            }
        });

        nextButton_2 = findViewById(R.id.nextButton_2);
        nextButton_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
//                Toast.makeText(Signup_Screen_2.this, day+" "+month+" "+year, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(Signup_Screen_2.this, requestCode+" "+data.getData(), Toast.LENGTH_SHORT).show();
        if (requestCode ==1){
            if (data == null){
                return;
            }
            docLayout.setVisibility(View.VISIBLE);
            Uri docUri = data.getData();
            docText.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1)+".pdf");
        }
        else {
            Toast.makeText(Signup_Screen_2.this, "Please try again ", Toast.LENGTH_SHORT).show();
        }
    }

}