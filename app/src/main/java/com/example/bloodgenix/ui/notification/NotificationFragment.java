package com.example.bloodgenix.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bloodgenix.Adapters.NotificationAdapter;
import com.example.bloodgenix.Models.Messages;
import com.example.bloodgenix.R;
import com.example.bloodgenix.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class NotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView chatsBlockView;
    NotificationAdapter noteAdapter;
    SwipeRefreshLayout refreshLayout;;
    public String ReceiverNumber;
    String senderName;
    String senderImg;
    int flag=0;
    public ArrayList<Messages> messagesArrayList;
    public ArrayList<Long> timeArrayList;
    public Map<String, Long> timeValues;

    FirebaseDatabase database;
    DatabaseReference messageReference;
    Toolbar toolbarLayout;
    TextView toolBarHeader;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                messagesArrayList.clear();
                noteAdapter.notifyDataSetChanged();
                Fragment someFragment = new NotificationFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, someFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
                refreshLayout.setRefreshing(false);
            }
        });

        SessionManager sessionManager = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> ReceiverData =sessionManager.getUserDetailsFromSession();
        ReceiverNumber = "+91 "+ReceiverData.get(SessionManager.KEY_PHONENUMBER);

        database = FirebaseDatabase.getInstance();
        messagesArrayList = new ArrayList<>();

        timeArrayList = new ArrayList<>();
        timeValues = new HashMap<>();
        chatCall();

        chatsBlockView = view.findViewById(R.id.chatsBlockView);
        chatsBlockView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatsBlockView.setLayoutManager(new LinearLayoutManager(getContext()));


        noteAdapter = new NotificationAdapter(getContext(), messagesArrayList);
        chatsBlockView.setAdapter(noteAdapter);

        return view;
    }

    //FETCHING THE VALUES FROM CHAT OF SENDER
    private void chatCall() {
        final String[] chckPerson = new String[1];
        Date date = new Date();

        DatabaseReference keyReference = database.getReference("Chats");
        keyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    chckPerson[0] = dataSnapshot.getKey();
                    if (chckPerson[0].substring(0,14).equals(ReceiverNumber))
                    {
                        messageReference = dataSnapshot.getRef().child("messages");
                        messageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                {
                                    String senderID = dataSnapshot1.child("senderID").getValue(String.class);
                                    String receiverID = dataSnapshot1.child("receiverID").getValue(String.class);

                                    long currentTime = date.getTime();

                                    if (receiverID.equals(ReceiverNumber))
                                    {
                                        long time = dataSnapshot1.child("timeStamp").getValue(long.class);
                                        timeArrayList.add(time);
                                    }
                                }

                                for (DataSnapshot dataSnapshot2 : snapshot.getChildren()){
                                    String receiverID = dataSnapshot2.child("receiverID").getValue(String.class);
                                    if (receiverID.equals(ReceiverNumber))
                                    {
                                        Long extractedTime = dataSnapshot2.child("timeStamp").getValue(long.class);
                                        long value = timeArrayList.get(timeArrayList.size()-1);
                                        if (extractedTime == value){
                                            String senderID = dataSnapshot2.child("senderID").getValue(String.class);
                                            String message = dataSnapshot2.child("message").getValue(String.class);
                                            String _senderName = dataSnapshot2.child("senderName").getValue(String.class);
                                            String _senderImg = dataSnapshot2.child("senderImg").getValue(String.class);
                                            String _receiverName = dataSnapshot2.child("receiverName").getValue(String.class);
                                            String _receiverImg = dataSnapshot2.child("receiverImg").getValue(String.class);

                                        Messages messages = new Messages(message, senderID, extractedTime,receiverID,_senderName, _senderImg, _receiverName, _receiverImg);
                                        messagesArrayList.add(messages);
                                        }
                                    }
                                }
                                noteAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void keySelection(Map<String, Long> timeValues) {

    }

    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }
}