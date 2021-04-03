package com.example.oops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName,InputPhoneNumber,InputPassword,InputConfirmPassword;
    private Spinner TypeUser;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CreateAccountButton=(Button)findViewById(R.id.register_btn);
        InputName=(EditText)findViewById(R.id.register_username_input);
        InputPhoneNumber=(EditText)findViewById(R.id.register_phone_number_input);
        InputPassword=(EditText)findViewById(R.id.register_password_input);
        InputConfirmPassword=(EditText)findViewById(R.id.register_confirm_password_input);
        TypeUser=findViewById(R.id.register_type_user_input);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }


        });
    }

    private void CreateAccount() {
        TypeUser.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        String name= InputName.getText().toString();
        String phone= InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();
        String confirmpassword=InputConfirmPassword.getText().toString();
        String typeuser=String.valueOf(TypeUser.getSelectedItem());

        if(TextUtils.isEmpty(name) || name.length()<6)
        {
            Toast.makeText(this,"Enter a name having more than 6 characters",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone) || phone.length()!=10)
        {
            Toast.makeText(this,"Enter a valid phone number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password) || password.length()<6)
        {
            Toast.makeText(this,"Enter a password having more than 6 characters",Toast.LENGTH_SHORT).show();
        }
       else  if(TextUtils.isEmpty(confirmpassword)|| confirmpassword.length()<6)
        {
            Toast.makeText(this,"Ensure that you have confirmed the password properly",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(typeuser)|| typeuser.equals("Choose the type of user"))
        {
            Toast.makeText(this,"Enter a valid type of user",Toast.LENGTH_SHORT).show();
        }

        else
        {
            if(confirmpassword.equals(password))
            {
                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("We are creating your account. Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                ValidatePhoneNumber(name,phone,password,typeuser);
            }
            else
            {
                Toast.makeText(this,"Confirm your password properly",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void ValidatePhoneNumber(String name, String phone, String password,String typeuser) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Accounts").child(name).exists()))
                {
                    HashMap<String,Object> userdatamap= new HashMap<>();
                    userdatamap.put("phone",phone);
                    userdatamap.put("password",password);
                    userdatamap.put("name",name);
                    userdatamap.put("type",typeuser);
                    RootRef.child("Accounts").child(name).updateChildren(userdatamap).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this,"Account created successfully",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                        intent.putExtra("phonenumber",phone);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this,"Try again later plz",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"This "+name + "already exists",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}