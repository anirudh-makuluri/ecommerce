package com.example.oops.retailer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.oops.MainActivity;
import com.example.oops.QueriesActivity;
import com.example.oops.R;
import com.example.oops.customer.CustomerHomeActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.google.firebase.auth.FirebaseAuth;

public class RetailerHomeActivity extends AppCompatActivity {
    private ImageView milk,vegetables,fruits,dairy,beverages,breakfast,foodgrams,readymade;
    private Button logoutbtn,checkorderbtn,maintainproductsbtn,productsbtn,querybtn;
    private AccessTokenTracker accessTokenTracker;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_home);
        vegetables=findViewById(R.id.vegetables);
        milk=findViewById(R.id.milk);
        fruits=findViewById(R.id.fruits);
        dairy=findViewById(R.id.dairy);
        beverages=findViewById(R.id.beverages);
        breakfast=findViewById(R.id.breakfast);
        foodgrams=findViewById(R.id.foodgrams);
        readymade=findViewById(R.id.readymade);
        querybtn=findViewById(R.id.retailer_query);
        logoutbtn=findViewById(R.id.retailer_logout);
        checkorderbtn=findViewById(R.id.check_order_btn);
        maintainproductsbtn=findViewById(R.id.maintain_btn);
        productsbtn=findViewById(R.id.retailer_products);
        querybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), QueriesActivity.class);
                startActivity(intent);
            }
        });
        productsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RetailerProductsActivity.class);
                startActivity(intent);
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                FirebaseAuth.getInstance().signOut();
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        if (currentAccessToken == null) {
                            mFirebaseAuth.signOut();
                        }
                    }
                };
                startActivity(intent);
                finish();
            }
        });

        maintainproductsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CustomerHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Admin","Admin");
                startActivity(intent);


            }
        });
        checkorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RetailerNewOrderActivity.class);
                startActivity(intent);


            }
        });


        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RetailerHomeActivity.this,RetailerAddCategory.class);
                intent.putExtra("category","vegetables");
                startActivity(intent);
            }
        });

        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RetailerHomeActivity.this,RetailerAddCategory.class);
                intent.putExtra("category","fruits");
                startActivity(intent);
            }
        });

        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RetailerHomeActivity.this,RetailerAddCategory.class);
                intent.putExtra("category","dairy");
                startActivity(intent);
            }
        });

        readymade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RetailerHomeActivity.this,RetailerAddCategory.class);
                intent.putExtra("category","readymade");
                startActivity(intent);
            }
        });

        foodgrams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RetailerHomeActivity.this,RetailerAddCategory.class);
                intent.putExtra("category","foodgrams");
                startActivity(intent);
            }
        });

        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RetailerHomeActivity.this,RetailerAddCategory.class);
                intent.putExtra("category","beverages");
                startActivity(intent);
            }
        });

        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RetailerHomeActivity.this,RetailerAddCategory.class);
                intent.putExtra("category","milk");
                startActivity(intent);
            }
        });

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RetailerHomeActivity.this,RetailerAddCategory.class);
                intent.putExtra("category","breakfast");
                startActivity(intent);
            }
        });



    }
}