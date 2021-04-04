package com.example.oops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oops.customer.CustomerHomeActivity;
import com.example.oops.retailer.RetailerHomeActivity;
import com.example.oops.wholesaler.WholesalerHomeActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private Button logout,submit;
    private Spinner spinner;
    private TextView Displayname;
    private FirebaseAuth mFirebaseAuth;
    private AccessTokenTracker accessTokenTracker;
    String l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logout = (Button) findViewById(R.id.logout);
        Displayname=(TextView) findViewById(R.id.name);
        spinner=(Spinner) findViewById(R.id.spinner1);
        submit=findViewById(R.id.btnSubmit);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, String.valueOf(spinner.getSelectedItem()), Toast.LENGTH_SHORT).show();
            }
        });

        GoogleSignInAccount googleSignInAccount= GoogleSignIn.getLastSignedInAccount(this);
        if(googleSignInAccount!=null){
            Displayname.setText("hello "+googleSignInAccount.getDisplayName()+". Select the type of user below");


        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        if (currentAccessToken == null) {
                            mFirebaseAuth.signOut();
                        }
                    }
                };
                Paper.book().destroy();
                Intent intent = new Intent(HomeActivity.this,MainActivity.class
                );
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= googleSignInAccount.getDisplayName();
                String typeuser=String.valueOf(spinner.getSelectedItem());
                final DatabaseReference RootRef;
                RootRef= FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> userdatamap= new HashMap<>();
                        userdatamap.put("name",name);
                        userdatamap.put("type",typeuser);
                        RootRef.child("Accounts").child(name).updateChildren(userdatamap).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(HomeActivity.this,"Type of user added successfully",Toast.LENGTH_SHORT).show();
                                        if(typeuser.equals("Wholesaler"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), WholesalerHomeActivity.class
                                            );
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            Toast.makeText(HomeActivity.this, "you are " + typeuser, Toast.LENGTH_SHORT).show();
                                        }
                                        else if(typeuser.equals("Customer"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), CustomerHomeActivity.class
                                            );
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            Toast.makeText(HomeActivity.this, "you are " + typeuser, Toast.LENGTH_SHORT).show();
                                        }
                                        else if(typeuser.equals("Retailer"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), RetailerHomeActivity.class
                                            );
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            Toast.makeText(HomeActivity.this, "you are " + typeuser, Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(HomeActivity.this, "some error occured", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}