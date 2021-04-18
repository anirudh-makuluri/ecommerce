package com.example.oops.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.oops.R;
import com.example.oops.retailer.RetailerAddCategory;
import com.example.oops.retailer.RetailerHomeActivity;

public class CustomerCategoryActivity extends AppCompatActivity {

    private ImageView vegetables,fruits,dairy,readymade,milk,beverages,breakfast,foodgrams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_category);
        vegetables=findViewById(R.id.category_vegetables);
        fruits=findViewById(R.id.category_fruits);
        dairy=findViewById(R.id.category_dairy);
        readymade=findViewById(R.id.category_readymade);
        milk=findViewById(R.id.category_milk);
        beverages=findViewById(R.id.category_beverages);
        breakfast=findViewById(R.id.category_breakfast);
        foodgrams=findViewById(R.id.category_foodgrams);

        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CustomerCategorySearchActivity.class);
                intent.putExtra("category","vegetables");
                startActivity(intent);
            }
        });
        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CustomerCategorySearchActivity.class);
                intent.putExtra("category","fruits");
                startActivity(intent);
            }
        });
        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CustomerCategorySearchActivity.class);
                intent.putExtra("category","dairy");
                startActivity(intent);
            }
        });
        readymade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CustomerCategorySearchActivity.class);
                intent.putExtra("category","readymade");
                startActivity(intent);
            }
        });
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CustomerCategorySearchActivity.class);
                intent.putExtra("category","breakfast");
                startActivity(intent);
            }
        });
        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CustomerCategorySearchActivity.class);
                intent.putExtra("category","beverages");
                startActivity(intent);
            }
        });
        foodgrams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CustomerCategorySearchActivity.class);
                intent.putExtra("category","foodgrams");
                startActivity(intent);
            }
        });
        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CustomerCategorySearchActivity.class);
                intent.putExtra("category","milk");
                startActivity(intent);
            }
        });
    }
}