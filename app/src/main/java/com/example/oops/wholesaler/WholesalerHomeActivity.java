package com.example.oops.wholesaler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.oops.LoginActivity;
import com.example.oops.MainActivity;
import com.example.oops.R;
import com.example.oops.customer.CustomerHomeActivity;
import com.example.oops.retailer.RetailerAddCategory;
import com.example.oops.retailer.RetailerHomeActivity;
import com.example.oops.retailer.RetailerNewOrderActivity;
import com.example.oops.retailer.RetailerProductsActivity;
import com.example.oops.wholesaler.subcats.WholesalerDairy;
import com.example.oops.wholesaler.subcats.WholesalerFruits;
import com.example.oops.wholesaler.subcats.WholesalerReadymade;
import com.example.oops.wholesaler.subcats.WholesalerVegetables;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.google.firebase.auth.FirebaseAuth;

public class WholesalerHomeActivity extends AppCompatActivity {
    private ImageView milk,vegetables,fruits,dairy,beverages,breakfast,foodgrams,readymade;
    private Button logoutbtn,checkorderbtn,maintainproductsbtn;
    private AccessTokenTracker accessTokenTracker;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaler_home);
        vegetables=findViewById(R.id.wholesaler_vegetables);
        milk=findViewById(R.id.wholesaler_milk);
        fruits=findViewById(R.id.wholesaler_fruits);
        dairy=findViewById(R.id.wholesaler_dairy);
        beverages=findViewById(R.id.wholesaler_beverages);
        breakfast=findViewById(R.id.wholesaler_breakfast);
        foodgrams=findViewById(R.id.wholesaler_foodgrams);
        readymade=findViewById(R.id.wholesaler_readymade);
        logoutbtn=findViewById(R.id.wholesaler_logout);
        checkorderbtn=findViewById(R.id.wholesaler_check_order_btn);
        maintainproductsbtn=findViewById(R.id.wholesaler_maintain_btn);
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
                Intent intent = new Intent(getApplicationContext(), RetailerProductsActivity.class);
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
                Intent intent= new Intent(WholesalerHomeActivity.this, WholesalerAddCategory.class);
                intent.putExtra("category","vegetables");
                startActivity(intent);
            }
        });

        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WholesalerHomeActivity.this,WholesalerAddCategory.class);
                intent.putExtra("category","fruits");
                startActivity(intent);
            }
        });

        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WholesalerHomeActivity.this,WholesalerAddCategory.class);
                intent.putExtra("category","dairy");
                startActivity(intent);
            }
        });

        readymade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WholesalerHomeActivity.this,WholesalerAddCategory.class);
                intent.putExtra("category","readymade");
                startActivity(intent);
            }
        });

        foodgrams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WholesalerHomeActivity.this,WholesalerAddCategory.class);
                intent.putExtra("category","foodgrams");
                startActivity(intent);
            }
        });

        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WholesalerHomeActivity.this,WholesalerAddCategory.class);
                intent.putExtra("category","beverages");
                startActivity(intent);
            }
        });

        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WholesalerHomeActivity.this,WholesalerAddCategory.class);
                intent.putExtra("category","milk");
                startActivity(intent);
            }
        });

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WholesalerHomeActivity.this,WholesalerAddCategory.class);
                intent.putExtra("category","breakfast");
                startActivity(intent);
            }
        });







    }
}