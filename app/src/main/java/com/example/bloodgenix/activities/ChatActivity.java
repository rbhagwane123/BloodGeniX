package com.example.bloodgenix.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodgenix.Adapters.MessagesAdapter;
import com.example.bloodgenix.Models.BlockUser;
import com.example.bloodgenix.Models.Messages;
import com.example.bloodgenix.R;
import com.example.bloodgenix.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class ChatActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    String passedData[] = new String[5];
    CircleImageView profileImg;
    ImageView activeStatus;
    TextView receiverName;
    RecyclerView messageAdapter;
    ImageButton chatBackBtn, dotMenu;
    CardView sendBtn;
    EditText textMessage;
    public static String senderImg;
    public static String receiverImg;
    String senderUID;
    String receiverUID, whatToDo;
    String senderRoom;
    String receiverRoom;
    static ArrayList<Messages> messagesArrayList;
    MessagesAdapter onlyAdapter;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    public String senderName;
    int blockFlag;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent chat = getIntent();
        passedData = chat.getStringArrayExtra("chatData");
        whatToDo = chat.getStringExtra("whatToDo");

        profileImg = findViewById(R.id.profileImg);
        receiverName = findViewById(R.id.receiverName);
        textMessage = findViewById(R.id.textMessage);
        sendBtn = findViewById(R.id.sendBtn);
        messageAdapter = findViewById(R.id.messageAdapter);
        chatBackBtn = findViewById(R.id.chatBackBtn);
        dotMenu = findViewById(R.id.dotMenuBtn);
        activeStatus = findViewById(R.id.activeStatus);

        auth = FirebaseAuth.getInstance();
        receiverImg = passedData[2];
        Glide.with(ChatActivity.this).load(passedData[2]).into(profileImg);
        receiverName.setText(passedData[1]);
        messagesArrayList = new ArrayList<>();


        chatBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whatToDo.equals("notificationChat")) {
                    Intent previous = new Intent(ChatActivity.this, DashBoard_Screen.class);
                    previous.putExtra("profile Values", senderUID);
                    startActivity(previous);
                } else {
                    Intent previousProfile = new Intent(ChatActivity.this, ProfileView.class);
                    previousProfile.putExtra("mobile number", passedData[0]);
                    startActivity(previousProfile);
                }
            }
        });

        dotMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ChatActivity.this, v);
                popupMenu.setOnMenuItemClickListener(ChatActivity.this);
                popupMenu.inflate(R.menu.chat_menu);
                popupMenu.show();

            }
        });

        SessionManager sessionManager = new SessionManager(ChatActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> senderData = sessionManager.getUserDetailsFromSession();

        String senderNumber = "+91 " + senderData.get(SessionManager.KEY_PHONENUMBER);
        String _number = senderNumber.substring(4, senderNumber.length());
        senderUID = senderNumber;
        senderName = senderData.get(SessionManager.KEY_FULLNAME);
        receiverUID = passedData[0];
        checkStatus(receiverUID);
        Query checkUserSender = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(senderNumber);
        checkUserSender.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String _fullName = snapshot.child(_number).child("fullName").getValue(String.class);
                String _emailId = snapshot.child(_number).child("emailId").getValue(String.class);
                String _gender = snapshot.child(_number).child("gender").getValue(String.class);
                String _dob = snapshot.child(_number).child("d_o_b").getValue(String.class);
                String _img = snapshot.child(_number).child("profileImg").getValue(String.class);
                String _uid = snapshot.child(_number).child("uid").getValue(String.class);

                senderImg = _img;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        onlyAdapter = new MessagesAdapter(ChatActivity.this, messagesArrayList, senderUID);
        messageAdapter.setAdapter(onlyAdapter);

        senderRoom = senderUID + receiverUID;
        receiverRoom = receiverUID + senderUID;

        chatReferenceCall();


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textmessage = textMessage.getText().toString();
                dialogCreationMore();

                DatabaseReference keReference = FirebaseDatabase.getInstance().getReference("BlockUser");
                keReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String keyValue = dataSnapshot.getKey();

                                if (keyValue.substring(0, 14).toString().equals(senderUID)) {
                                    dialog.show();
                                    //BLOCK DIALOG CREATION INITIALISATION
                                    Button CANCEL = dialog.findViewById(R.id.CANCEL);
                                    Button UNBLOCK = dialog.findViewById(R.id.UNBLOCK);

                                    CANCEL.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    UNBLOCK.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            FirebaseDatabase.getInstance().getReference("BlockUser").child(keyValue).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                        }
                                    });

                                } else {
                                    Toast.makeText(ChatActivity.this, "You have been blocked", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        } else {
                            if (textmessage.isEmpty()) {
                                Toast.makeText(ChatActivity.this, "Enter text", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            textMessage.setText("");
                            Date date = new Date();

                            Messages messages = new Messages(textmessage, senderUID, date.getTime(), receiverUID, senderName, senderImg, passedData[1], passedData[2]);
                            database = FirebaseDatabase.getInstance();
                            database.getReference().child("Chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        database.getReference().child("Chats")
                                                .child(receiverRoom)
                                                .child("messages")
                                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
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
        });
    }

    public int checkBlock() {
        Query chckBlock = FirebaseDatabase.getInstance().getReference("BlockUser").orderByChild("receiver").equalTo(receiverUID);
        chckBlock.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
//                    Toast.makeText(ChatActivity.this, "inside block", Toast.LENGTH_SHORT).show();
                    blockFlag = 1;
                }
//                }else{
//                    Toast.makeText(ChatActivity.this, "outside block", Toast.LENGTH_SHORT).show();
//                    blockFlag = 0;
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return blockFlag;
    }

    public void dialogCreationMore() {
        dialog = new Dialog(ChatActivity.this);
        dialog.setContentView(R.layout.block_layout_design);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.info_layout_background_style));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.setCanceledOnTouchOutside(true);

    }

    public void checkStatus(String receiverUID) {

        Query checkStatus = FirebaseDatabase.getInstance().getReference("PersonStatus").orderByChild("phoneNumber").equalTo(receiverUID);
        checkStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child(receiverUID).child("status").getValue(String.class);
                    if (status.equals("true")) {
                        activeStatus.setVisibility(View.VISIBLE);
                    } else {
                        activeStatus.setVisibility(View.INVISIBLE);
                    }
                } else {
                    activeStatus.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void chatReferenceCall() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference chatReference = database.getReference().child("Chats").child(senderRoom).child("messages");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                onlyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.BlockUser:
                reference = database.getReference().child("BlockUser").child(senderUID + receiverUID);
                BlockUser block = new BlockUser(receiverUID, senderUID);
                reference.setValue(block).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChatActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;
            default:
                return false;
        }
    }
}
