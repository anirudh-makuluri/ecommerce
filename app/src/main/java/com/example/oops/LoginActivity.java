package com.example.oops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputName,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private String parentDbname="Accounts";
    private CheckBox chkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton=(Button)findViewById(R.id.main_login_btn);
        InputName=(EditText)findViewById(R.id.login_user_name_input);
        InputPassword=(EditText)findViewById(R.id.login_password_input );
        loadingBar = new ProgressDialog(this);
        chkBox=(CheckBox)findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }


        });


    }

    private void LoginUser()
    {
        String name= InputName.getText().toString();
        String password=InputPassword.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(LoginActivity.this,"Enter your name",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this,"Write ur password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking for creds");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(name,password);
        }
    }

    private void AllowAccessToAccount(final String name,final String password) {
        if(chkBox.isChecked())
        {
            Paper.book().write(Prevalent.UserNameKey,name);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }
        //System.out.println("In access");
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbname).child(name).exists())
                {
                    Users usersdata=snapshot.child(parentDbname).child(name).getValue(Users.class);
                    if(usersdata.getName().equals(name) )
                    {
                        if(usersdata.getPassword().equals(password))
                        {
                            Toast.makeText(LoginActivity.this,"Getting you in",Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(LoginActivity.this,otppage.class
                            );
                            startActivity(intent);
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Wrong password",Toast.LENGTH_LONG).show();

                        }

                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"No account with this phone number",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}