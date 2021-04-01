package com.example.oops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class otppage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otppage);
        EditText otpinput=findViewById(R.id.otp_input);
        Button login=findViewById(R.id.otp_login_btn);
        String ver = "abc";
        String otp= otpinput.getText().toString();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(otp.equals(ver)){
                    Toast.makeText(otppage.this, "Entering", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(otppage.this,HomeActivity.class
                    );
                    startActivity(intent);}
                    else
                    {
                        Toast.makeText(otppage.this, "OTP wrong", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
}