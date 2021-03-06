package com.example.bloodgenix.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.bloodgenix.LoadingDialog;
import com.example.bloodgenix.Models.Users;
import com.example.bloodgenix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.TimeUnit;

public class Otp_Screen2 extends AppCompatActivity {

    public String Otp_details[] = new String[15];
    String code_by_system, Number_entered_by_user;
    String imageURI, pdfUri, whatToDo, forgetNumber;
    TextView dispNumber;
    PinView otpVal;
    Button resendBtn, verify;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    //FireBase Initialisation of variables
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen2);

        //Values intake from previous activity
        Intent i3 = getIntent();
        Otp_details = i3.getStringArrayExtra("Otp_value");
        whatToDo = i3.getStringExtra("whatToDo");
        forgetNumber = i3.getStringExtra("phoneNo");


        //SETTING THE PHONE NUMBER
        Number_entered_by_user = forgetNumber.substring(4, forgetNumber.length());
        //Toast.makeText(Otp_Screen2.this, whatToDo, Toast.LENGTH_SHORT).show();

        //firebase values
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://bloodgenix-bb937-default-rtdb.firebaseio.com/");
        storage = FirebaseStorage.getInstance();

        //Otp code
        otpVal = findViewById(R.id.otpVal);
        dispNumber = findViewById(R.id.dispNumber);
//        dispNumber.setText(forgetNumber);
        dispNumber.setText(forgetNumber);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                String code = credential.getSmsCode();
                if (code != null) {
                    otpVal.setText(code);
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(Otp_Screen2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(Otp_Screen2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        startPhoneNumberVerification(Number_entered_by_user);
        resendBtn = findViewById(R.id.resendBtn);
        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(Number_entered_by_user, mResendToken);
            }
        });

        verify = findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpVal.getText().toString();
                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });

    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(Otp_Screen2.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(Otp_Screen2.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            LoadingDialog dialog = new LoadingDialog(Otp_Screen2.this);
                            dialog.startDialog();

                            if (whatToDo.equals("updateData")) {
                                updateUsersData(dialog);
                            } else {
                                storeDataset(task, dialog);
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(Otp_Screen2.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(Otp_Screen2.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void updateUsersData(LoadingDialog dialog) {
        Intent setPass = new Intent(Otp_Screen2.this, SetPassword.class);
        setPass.putExtra("phoneNo", forgetNumber);
        dialog.dismissDialog();
        startActivity(setPass);
        finish();
    }

    private void storeDataset(Task<AuthResult> task, LoadingDialog dialog) {
        FirebaseUser user = task.getResult().getUser();
        DatabaseReference reference = database.getReference().child("Users").child(Number_entered_by_user);
        StorageReference storageReference_image = storage.getReference().child("profilePic").child(Number_entered_by_user);
        StorageReference storageReference_pdf = storage.getReference().child("proof").child(Number_entered_by_user);
        storageReference_image.putFile(Uri.parse(Otp_details[0])).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageReference_pdf.putFile(Uri.parse(Otp_details[7])).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference_image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageURI = uri.toString();
                                        storageReference_pdf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                pdfUri = uri.toString();
                                                Users users = new Users(auth.getUid(), imageURI, Otp_details[1], Otp_details[2], Otp_details[3], Otp_details[4], Otp_details[5], Otp_details[6], pdfUri, forgetNumber);

                                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            dialog.dismissDialog();
                                                            Intent dashBoard = new Intent(Otp_Screen2.this, DashBoard_Screen.class);
                                                            dashBoard.putExtra("profile Values", forgetNumber);
                                                            startActivity(dashBoard);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Otp_Screen2.this, "Error in creating users", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            } else {
                                Toast.makeText(Otp_Screen2.this, "pdf inserting error", Toast.LENGTH_SHORT).show();
                                dialog.dismissDialog();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Otp_Screen2.this, "image inserting error", Toast.LENGTH_SHORT).show();
                    dialog.dismissDialog();
                }
            }
        });
    }

}