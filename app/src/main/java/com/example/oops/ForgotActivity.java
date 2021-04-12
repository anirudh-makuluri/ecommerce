package com.example.oops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.customer.CustomerHomeActivity;
import com.example.oops.customer.Settings;
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

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ForgotActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String verificationCodeBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        mAuth = FirebaseAuth.getInstance();
        EditText otpinput=findViewById(R.id.forgot_input);
        Button login=findViewById(R.id.forgot_submit);
        EditText password=findViewById(R.id.forgot_password_input);
        EditText confirmpasswword=findViewById(R.id.forgot_confirm_password_input);
        Button confirm=findViewById(R.id.forgot_confirmed);
        String name=getIntent().getStringExtra("name");
        String forgot=getIntent().getStringExtra("phone");
        String phonenumber= forgot;
        sendVerificationToUser(phonenumber);

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
            Toast.makeText(ForgotActivity.this, "error", Toast.LENGTH_SHORT).show();
        }


    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeBySystem,code);
        SignInTheUser(credential);

    }

    private void SignInTheUser(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).
                addOnCompleteListener(ForgotActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotActivity.this, "noice", Toast.LENGTH_SHORT).show();
                            ConfirmPassword();
                        }
                        else
                        {
                            Toast.makeText(ForgotActivity.this, "wrong OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void ConfirmPassword() {
        EditText otpinput=findViewById(R.id.forgot_input);
        Button login=findViewById(R.id.forgot_submit);
        EditText password=findViewById(R.id.forgot_password_input);
        EditText confirmpasswword=findViewById(R.id.forgot_confirm_password_input);
        Button confirm=findViewById(R.id.forgot_confirmed);
        otpinput.setVisibility(View.INVISIBLE);
        login.setVisibility(View.INVISIBLE);
        password.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
        confirmpasswword.setVisibility(View.VISIBLE);
        String password1=password.getText().toString();
        String confirmpassword1=confirmpasswword.getText().toString();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(password1) || password.length()<6)
                {
                    Toast.makeText(ForgotActivity.this, "enter password", Toast.LENGTH_SHORT).show();
                }
                else  if(TextUtils.isEmpty(confirmpassword1)|| confirmpassword1.length()<6)
                {
                    Toast.makeText(ForgotActivity.this, "confirm password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(confirmpassword1.equals(password))
                    {
                        String name=getIntent().getStringExtra("name");
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Accounts");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap. put("password", password1);
                        ref.child(name).updateChildren(userMap);
                        Toast.makeText(ForgotActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(ForgotActivity.this,"Confirm your password properly",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



    }


}