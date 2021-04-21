package com.example.oops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.oops.Prevalent.Prevalent;
import com.example.oops.customer.CartActivity;
import com.example.oops.customer.CustomerHomeActivity;
import com.example.oops.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private FloatingActionButton addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDesc,productName;
    private String productID="",state="normal";
    private String retailername="Retailer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID=getIntent().getStringExtra("pid");
        addToCartBtn=findViewById(R.id.add_product_to_cart);
        productImage=findViewById(R.id.product_image_details);
        numberButton=findViewById(R.id.number_btn);
        productPrice=findViewById(R.id.product_price_details);
        productDesc=findViewById(R.id.product_description_details);
        productName=findViewById(R.id.product_name_details);

        getProductDetails(productID);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("Order Placed")||state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "purchase more after your current order is shipped", Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void addingToCartList() {

        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentDate.format(calForDate.getTime());
        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final DatabaseReference productRef=FirebaseDatabase.getInstance().getReference("Products");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{retailername=snapshot.child(productID).child("retailername").getValue().toString();}
                catch (NullPointerException e)
                {
                    Toast.makeText(ProductDetailsActivity.this, "theres no retailer for this product", Toast.LENGTH_SHORT).show();
                }

                System.out.println(retailername);
                final HashMap<String,Object> cartMap=new HashMap<>();
                System.out.println("hashmap "+retailername);
                cartMap.put("pid",productID);
                cartMap.put("pname",productName.getText().toString());
                cartMap.put("price",productPrice.getText().toString());
                cartMap.put("date",saveCurrentDate);
                cartMap.put("time",saveCurrentTime);
                cartMap.put("quantity",numberButton.getNumber());
                cartMap.put("discount","");
                cartMap.put("retailername",retailername);
                cartMap.put("state","not shipped");
                cartListRef.child("User View").child(Prevalent.currentonlineUser.getName()).child("Products").child(productID).updateChildren(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    cartListRef.child("Retailer View").child(Prevalent.currentonlineUser.getName()).child("Products").child(productID).updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), CustomerHomeActivity.class);
                                                    startActivity(intent);

                                                }
                                            });

                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Products products= snapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productDesc.setText(products.getDesc());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckOrderState()
    {
        DatabaseReference orderRef;
        orderRef=FirebaseDatabase.getInstance().getReference().child("Order").child(Prevalent.currentonlineUser.getName());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String shippingstate=snapshot.child("state").getValue().toString();

                    if(shippingstate.equals("shipped"))
                    {
                        state="Order Shipped";
                    }
                    else if(shippingstate.equals("shipped"))
                    {   state="Order Placed";

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}