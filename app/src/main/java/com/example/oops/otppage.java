package com.example.oops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oops.customer.CustomerHomeActivity;
import com.example.oops.model.Users;
import com.example.oops.retailer.RetailerHomeActivity;
import com.example.oops.wholesaler.WholesalerHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.Spinner;

import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class otppage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String verificationCodeBySystem;
    private Button logoutbtn,submitbtn;
    private EditText otpuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otppage);
        mAuth = FirebaseAuth.getInstance();
        EditText otpinput=findViewById(R.id.otp_input);
        Button login=findViewById(R.id.otp_login_btn);
        String phonenumber=getIntent().getStringExtra("phonenumber");
        Toast.makeText(this, phonenumber, Toast.LENGTH_SHORT).show();
        String name=getIntent().getStringExtra("name");
        sendVerificationToUser(phonenumber);
        logoutbtn=findViewById(R.id.otppage_logout);
        submitbtn=findViewById(R.id.otppage_emer_submit);
        otpuser=findViewById(R.id.otp_typeuser);



        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(otppage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code =otpinput.getText().toString();
                if(code.isEmpty() || code.length()<6)
                {
                    otpinput.setError("Wrong OTP");
                    otpinput.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });
    }

    private void sendVerificationToUser(String phonenumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phonenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                verifyCode(code);
            }
        }



        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(otppage.this, "error retrieving otp via phone", Toast.LENGTH_SHORT).show();
            int i=0;

            if(i==1)
            {
                Toast.makeText(otppage.this, "But do this", Toast.LENGTH_SHORT).show();
                otpuser.setVisibility(View.VISIBLE);
                submitbtn.setVisibility(View.VISIBLE);
                submitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String typeuser=otpuser.getText().toString();

                        if(typeuser.equals("Wholesaler"))
                        {
                            Intent intent = new Intent(getApplicationContext(), WholesalerHomeActivity.class
                            );
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(otppage.this, "you are " + typeuser, Toast.LENGTH_SHORT).show();
                        }
                        else if(typeuser.equals("Customer"))
                        {
                            Intent intent = new Intent(getApplicationContext(), CustomerHomeActivity.class
                            );
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(otppage.this, "you are " + typeuser, Toast.LENGTH_SHORT).show();
                        }
                        else if(typeuser.equals("Retailer"))
                        {
                            Intent intent = new Intent(getApplicationContext(), RetailerHomeActivity.class
                            );
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(otppage.this, "you are " + typeuser, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(otppage.this, "some error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }


    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeBySystem,code);
        SignInTheUser(credential);

    }

    private void SignInTheUser(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).
                addOnCompleteListener(otppage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {   String name=getIntent().getStringExtra("name");
                                DatabaseReference RootRef;
                                RootRef= FirebaseDatabase.getInstance().getReference();
                                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Users usersdata=snapshot.child("Accounts").child(name).getValue(Users.class);
                                        String typeuser=usersdata.getType();
                                        String nextLoc;
                                        if(typeuser.equals("Wholesaler"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), WholesalerHomeActivity.class
                                            );
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            Toast.makeText(otppage.this, "you are " + typeuser, Toast.LENGTH_SHORT).show();
                                        }
                                        else if(typeuser.equals("Customer"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), CustomerHomeActivity.class
                                            );
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            Toast.makeText(otppage.this, "you are " + typeuser, Toast.LENGTH_SHORT).show();
                                        }
                                        else if(typeuser.equals("Retailer"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), RetailerHomeActivity.class
                                            );
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            Toast.makeText(otppage.this, "you are " + typeuser, Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(otppage.this, "some error occured", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



                            }
                            else
                            {
                                Toast.makeText(otppage.this, "wrong OTP", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }
}