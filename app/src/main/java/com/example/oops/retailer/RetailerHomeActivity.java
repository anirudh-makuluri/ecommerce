package com.example.oops.retailer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.oops.R;

public class RetailerHomeActivity extends AppCompatActivity {
    private ImageView milk,vegetables,fruits,dairy,beverages,breakfast,foodgrams,readymade;

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