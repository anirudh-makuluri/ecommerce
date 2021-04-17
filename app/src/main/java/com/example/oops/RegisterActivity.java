package com.example.oops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.M;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton,LocationButton;
    private EditText InputName,InputPhoneNumber,InputPassword,InputConfirmPassword,LocationText,CityText;
    private Spinner TypeUser;
    private ProgressDialog loadingBar;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;


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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationText=findViewById(R.id.register_location_text);
        CityText=findViewById(R.id.register_city_text);
        LocationButton=findViewById(R.id.register_location_btn);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
                                            try {
                                                GetLocation(lat,lon);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
//                                            LocationText.setText(lat+ " " + lon);
                                            //Toast.makeText(RegisterActivity.this, "worked", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {   Double lat=17.5449;
                                            Double lon=78.5718;
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

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
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

    private void CreateAccount() {
        TypeUser.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        String location=LocationText.getText().toString();
        String name= InputName.getText().toString();
        String phone= InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();
        String confirmpassword=InputConfirmPassword.getText().toString();
        String typeuser=String.valueOf(TypeUser.getSelectedItem());
        String city=CityText.getText().toString();

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
        else if(TextUtils.isEmpty(location))
        {
            Toast.makeText(this,"Enter location",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(city))
        {
            Toast.makeText(this,"Enter city",Toast.LENGTH_SHORT).show();
        }

        else
        {
            if(confirmpassword.equals(password))
            {
                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("We are creating your account. Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                ValidatePhoneNumber(name,phone,password,typeuser,location,city);
            }
            else
            {
                Toast.makeText(this,"Confirm your password properly",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void ValidatePhoneNumber(String name, String phone, String password,String typeuser,String location,String city) {
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
                    userdatamap.put("address",location);
                    userdatamap.put("city",city);
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