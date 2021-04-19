package com.example.oops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.customer.CustomerHomeActivity;
import com.example.oops.model.Users;
import com.example.oops.retailer.RetailerHomeActivity;
import com.example.oops.wholesaler.WholesalerHomeActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private Button logout,submit,LocationButton;
    private Spinner spinner;
    private TextView Displayname;
    private EditText LocationText,CityText,PhoneText;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;

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
        PhoneText=findViewById(R.id.home_phone_number_input);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationText=findViewById(R.id.register_location_text);
        CityText=findViewById(R.id.register_city_text);
        LocationButton=findViewById(R.id.register_location_btn);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GoogleSignInAccount googleSignInAccount= GoogleSignIn.getLastSignedInAccount(this);
        DatabaseReference rootref=FirebaseDatabase.getInstance().getReference("Accounts");
        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(googleSignInAccount.getDisplayName()).exists())
                {
                    String phone=snapshot.child(googleSignInAccount.getDisplayName()).child("phone").getValue().toString();
                    Intent intent = new Intent(getApplicationContext(),otppage.class);
                    intent.putExtra("phone",phone);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        LocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Btn pressed");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                { System.out.println("In build");
                    if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(new OnSuccessListener<Location>(){
                                    @Override
                                    public void onSuccess(Location location)
                                    {
                                        if(location!=null)
                                        {
                                            Double lat=location.getLatitude();
                                            Double lon=location.getLongitude();
                                            Intent intent=new Intent(getApplicationContext(),GoogleMapActivity.class);
                                            startActivity(intent);
                                            try {
                                                GetLocation(lat,lon);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            //LocationText.setText(lat+ " " + lon);
                                            //Toast.makeText(RegisterActivity.this, "worked", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {   Double lat=17.544841;
                                            Double lon=78.571687;
                                            Intent intent=new Intent(getApplicationContext(),GoogleMapActivity.class);
                                            startActivity(intent);
                                            // Toast.makeText(RegisterActivity.this, lat+" "+lon, Toast.LENGTH_SHORT).show();

                                            try {

                                                GetLocation(lat,lon);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            //Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    }
                    else
                    {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
                    }
                }
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, String.valueOf(spinner.getSelectedItem()), Toast.LENGTH_SHORT).show();
            }
        });

        if(googleSignInAccount!=null){
            Displayname.setText("Hello "+googleSignInAccount.getDisplayName());
            Users usersdata= new Users();
            usersdata.setName(googleSignInAccount.getDisplayName());
            Prevalent.currentonlineUser=usersdata;



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
                String city=CityText.getText().toString();
                String location=LocationText.getText().toString();
                String email=googleSignInAccount.getEmail();
                String phone= PhoneText.getText().toString();
                if(TextUtils.isEmpty(phone))
                    Toast.makeText(HomeActivity.this, "enter phone number", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(city))
                    Toast.makeText(HomeActivity.this, "enter city", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(location))
                    Toast.makeText(HomeActivity.this, "enter location", Toast.LENGTH_SHORT).show();
                else{
                final DatabaseReference RootRef;
                RootRef= FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> userdatamap= new HashMap<>();
                        userdatamap.put("name",name);
                        userdatamap.put("type",typeuser);
                        userdatamap.put("address",location);
                        userdatamap.put("city",city);
                        userdatamap.put("email",email);
                        userdatamap.put("phone",phone);
                        RootRef.child("Accounts").child(name).updateChildren(userdatamap).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(getApplicationContext(),otppage.class);
                                        intent.putExtra("phone",phone);
                                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);


                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });}
            }
        });

    }

    private void GetLocation(Double latitude, Double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        LocationText.setText(address);
        CityText.setText(city);
        //Toast.makeText(this, address+" "+ city, Toast.LENGTH_SHORT).show();
    }
}