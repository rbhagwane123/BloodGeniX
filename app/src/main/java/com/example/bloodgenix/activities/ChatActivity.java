package com.example.bloodgenix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodgenix.Adapters.MessagesAdapter;
import com.example.bloodgenix.Models.Messages;
import com.example.bloodgenix.R;
import com.example.bloodgenix.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String passedData [] = new String[5];
    CircleImageView profileImg;
    TextView receiverName;
    RecyclerView messageAdapter;
    CardView sendBtn;
    EditText textMessage;
    public static String senderImg;
    public static String receiverImg;
    String senderUID;
    String receiverUID;

    String senderRoom, receiverRoom;
    ArrayList <Messages> messagesArrayList;
    MessagesAdapter onlyAdapter;

    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent chat = getIntent();
        passedData = chat.getStringArrayExtra("chatData");

        profileImg = findViewById(R.id.profileImg);
        receiverName = findViewById(R.id.receiverName);
        textMessage = findViewById(R.id.textMessage);
        sendBtn = findViewById(R.id.sendBtn);
        messageAdapter = findViewById(R.id.messageAdapter);

        receiverUID = passedData[0];
        receiverImg = passedData[2];
        Glide.with(ChatActivity.this).load(passedData[2]).into(profileImg);
        receiverName.setText(passedData[1]);
        messagesArrayList = new ArrayList<>();

        SessionManager sessionManager = new SessionManager(ChatActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> senderData =sessionManager.getUserDetailsFromSession();
        detailsFetch("+91 "+senderData.get(SessionManager.KEY_PHONENUMBER));
        senderUID = "+91 "+senderData.get(SessionManager.KEY_PHONENUMBER);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        onlyAdapter = new MessagesAdapter(ChatActivity.this, messagesArrayList, senderUID);
        messageAdapter.setAdapter(onlyAdapter);

        senderRoom = senderUID+receiverUID;
        receiverRoom = receiverUID+senderUID;

        database = FirebaseDatabase.getInstance();
        DatabaseReference chatReference = database.getReference().child("Chats").child(senderRoom).child("messages");
        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                messagesArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                Toast.makeText(ChatActivity.this, "inside chatReference", Toast.LENGTH_SHORT).show();
                onlyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textmessage = textMessage.getText().toString();
                if(textmessage.isEmpty()){
                    Toast.makeText(ChatActivity.this, "Enter text", Toast.LENGTH_SHORT).show();
                }
                textMessage.setText("");
                Date date = new Date();

                Messages messages = new Messages(textmessage, senderUID, date.getTime());
//                database= FirebaseDatabase.getInstance();
                database.getReference().child("Chats")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            database.getReference().child("Chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    DatabaseReference chatReference = database.getReference().child("Chats").child(senderRoom).child("messages");
                                    chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            messagesArrayList.clear();
                                            for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                            {
                                                Messages messages = dataSnapshot.getValue(Messages.class);
                                                messagesArrayList.add(messages);
                                            }
                                            Toast.makeText(ChatActivity.this, "inside chatReference", Toast.LENGTH_SHORT).show();
                                            onlyAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void detailsFetch(String phoneNumb) {

        String _number = phoneNumb.substring(4, phoneNumb.length());
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(phoneNumb);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String _fullName = snapshot.child(_number).child("fullName").getValue(String.class);
                String _emailId = snapshot.child(_number).child("emailId").getValue(String.class);
                String _gender = snapshot.child(_number).child("gender").getValue(String.class);
                String _dob = snapshot.child(_number).child("d_o_b").getValue(String.class);
                String _img = snapshot.child(_number).child("profileImg").getValue(String.class);

                senderImg = _img;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}