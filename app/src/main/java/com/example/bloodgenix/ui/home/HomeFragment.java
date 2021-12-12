package com.example.bloodgenix.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bloodgenix.activities.DonationForm;
import com.example.bloodgenix.R;
import com.example.bloodgenix.activities.RecipientForm;
import com.example.bloodgenix.SessionManager;
import com.example.bloodgenix.activities.User_Profile_View;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {


    TextView salutationText;
    CircleImageView active_person;
    Button donationExpand, RecipientExpand, Logout;
    String phoneNo;
    private SessionManager sessionManager;
    String sendingData [] = new String [3];
    private int chckFlag=0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        salutationText = view.findViewById(R.id.salutationText);
        active_person = view.findViewById(R.id.active_person);
        donationExpand = view.findViewById(R.id.donationExpand);
        RecipientExpand = view.findViewById(R.id.RecipientExpand);


        //FETCHING DETAILS FROM SESSION
        SessionManager sessionManager = new SessionManager(getContext(),"userLoginSession");
        HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();

        salutationText.setText("Hey there\n"+userDetails.get(SessionManager.KEY_FULLNAME));
        String _number = userDetails.get(SessionManager.KEY_PHONENUMBER);
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo("+91 "+_number);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String _img = snapshot.child(_number).child("profileImg").getValue(String.class);
                    String _fullName = snapshot.child(_number).child("fullName").getValue(String.class);

                    Glide.with(getContext()).load(_img).into(active_person);
                    sendingData [0] = _fullName;
                    sendingData [1] = "+91 "+_number;
                    sendingData [2] = _img;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            });

        active_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(getContext(), User_Profile_View.class);
                profile.putExtra("mobile number", "+91 "+_number);
                startActivity(profile);
            }
        });

        donationExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBeforeApply("+91 "+_number) == 1){
                    Intent donationIntent = new Intent(getContext(), DonationForm.class);
                    donationIntent.putExtra("Donation", sendingData);
                    startActivity(donationIntent);
                }

            }
        });

        RecipientExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RecipientIntent = new Intent(getContext(), RecipientForm.class);
                RecipientIntent.putExtra("Recipient", "+91 "+_number);
                startActivity(RecipientIntent);
            }
        });

        return view;

    }

    private int checkBeforeApply(String number) {

        Query donationDetails = FirebaseDatabase.getInstance().getReference("DonationDetails").orderByChild("phoneNumber").equalTo(number);
        donationDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(getContext(), "You already applied", Toast.LENGTH_SHORT).show();
                    chckFlag = 0;
                }else{
                    chckFlag = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                chckFlag = 0;
            }
        });
        return chckFlag;
    }

}

