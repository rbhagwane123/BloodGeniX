package com.example.bloodgenix.activities;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bloodgenix.R;

public class Signup_Screen_2 extends AppCompatActivity {

    private AutoCompleteTextView gender;
    ArrayAdapter <String> arrayAdapter;
    DatePicker datePicker;
    ImageButton docSelection, nextButton_2,docDeSelect;
    TextView docText;
    CardView docLayout;
    public String details [] = new String[15];
    String gender_type;
    Uri docUri;
    int Requestcode =1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen2);

//        Getting Profile 1 Values
        Intent i = getIntent();
        details = i.getStringArrayExtra("details_1");


//        for gender selection code
        gender = findViewById(R.id.Gender);
        String [] gender_val= getResources().getStringArray(R.array.gender);
        arrayAdapter= new ArrayAdapter<String>(this,R.layout.dropdown_item_gender,gender_val);
        gender.setAdapter(arrayAdapter);
        gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gender_type = parent.getItemAtPosition(position).toString();
            }
        });
        Context context = getApplicationContext();
        datePicker = findViewById(R.id.Dob);

//        file Uploading code
        docText = findViewById(R.id.docText);
        docLayout = findViewById(R.id.docLayout);
        docDeSelect = findViewById(R.id.docDeSelect);

        docSelection = findViewById(R.id.docSelection);
        docSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,Requestcode);
            }
        });

        docDeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docLayout.setVisibility(View.INVISIBLE);
                docSelection.setVisibility(View.VISIBLE);
                docDeSelect.setVisibility(View.INVISIBLE);
            }
        });

        nextButton_2 = findViewById(R.id.nextButton_2);
        nextButton_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth()+1;
                int year = datePicker.getYear();
                String dateOfBirth = day+"/"+month+"/"+year;

                details[5] = dateOfBirth;
                details[6] = gender_type;
                details[7] = String.valueOf(docUri);
                Intent Otp_1 = new Intent(Signup_Screen_2.this,Otp_Screen_1.class);
                Otp_1.putExtra("details_2",details);
                startActivity(Otp_1);
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
            docUri = data.getData();
            docText.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1)+".pdf");
            docSelection.setVisibility(View.INVISIBLE);
            docDeSelect.setVisibility(View.VISIBLE);

        }
        else {
            Toast.makeText(Signup_Screen_2.this, "Please try again ", Toast.LENGTH_SHORT).show();
        }
    }

}