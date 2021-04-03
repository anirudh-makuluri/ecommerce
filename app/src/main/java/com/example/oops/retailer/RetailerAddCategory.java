package com.example.oops.retailer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oops.R;

public class RetailerAddCategory extends AppCompatActivity {
    private String Categoryname;
    private Button AddProduct;
    private EditText Productname,Productdesc,Productprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_add_category);
        AddProduct=findViewById(R.id.add_product);
        Productname=findViewById(R.id.product_name);
        Productprice=findViewById(R.id.product_price);
        Productdesc=findViewById(R.id.product_desc);

        Categoryname=getIntent().getExtras().get("category").toString();
        Toast.makeText(this, Categoryname, Toast.LENGTH_SHORT).show();
    }
}