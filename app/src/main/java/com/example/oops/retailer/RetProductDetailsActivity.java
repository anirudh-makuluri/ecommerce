package com.example.oops.retailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.oops.Prevalent.Prevalent;
import com.example.oops.ProductDetailsActivity;
import com.example.oops.R;
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
import java.util.Random;

public class RetProductDetailsActivity extends AppCompatActivity {
    private FloatingActionButton addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDesc, productName;
    private String productID = "", state = "normal";
    private String retailername = "Retailer";
    private RatingBar productrating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ret_product_details);
        productID = getIntent().getStringExtra("pid");
        addToCartBtn = findViewById(R.id.retailer_add_product_to_cart);
        productImage = findViewById(R.id.retailer_product_image_details);
        numberButton = findViewById(R.id.retailer_number_btn);
        productrating = findViewById(R.id.retailer_products_rating_bar);
        productPrice = findViewById(R.id.retailer_product_price_details);
        productDesc = findViewById(R.id.retailer_product_description_details);
        productName = findViewById(R.id.retailer_product_name_details);
        getProductDetails(productID);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state.equals("Order Placed") || state.equals("Order Shipped")) {
                    Toast.makeText(RetProductDetailsActivity.this, "purchase more after your current order is shipped", Toast.LENGTH_LONG).show();
                } else {
                    addingToCartList();
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    private void addingToCartList() {

        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        cartListRef.keepSynced(true);

        String type = "";
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        DatabaseReference productRef;
        productRef = FirebaseDatabase.getInstance().getReference("Wholesaler Products");


        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    retailername = snapshot.child(productID).child("retailername").getValue().toString();
                } catch (NullPointerException e) {
                    //Toast.makeText(ProductDetailsActivity.this, "theres no retailer for this product", Toast.LENGTH_SHORT).show();
                }
                Boolean net = isNetworkAvailable();
                if (!net) {
                    Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI);
                    insertCalendarIntent.putExtra(CalendarContract.Events.TITLE, "Order from Krazy Kart"); // Simple title
                    insertCalendarIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                    insertCalendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Hyderabad");
                    insertCalendarIntent.putExtra(CalendarContract.Events.DESCRIPTION,
                            Prevalent.currentonlineUser.getName() + " ordered:" + productName.getText().toString() +
                                    "\nprice:" + productPrice.getText().toString() +
                                    "\nquantity:" + numberButton.getNumber() +
                                    "\n at date:" + saveCurrentDate +
                                    "\n at time:" + saveCurrentTime);// Description
                    insertCalendarIntent.putExtra(Intent.EXTRA_EMAIL, "anirudh.makuluri@gmail.com,durgakrishnavamsi@gmail.com");
                    insertCalendarIntent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
                    insertCalendarIntent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
                    startActivity(insertCalendarIntent);
                }

                System.out.println(retailername);
                final HashMap<String, Object> cartMap = new HashMap<>();
                System.out.println("hashmap " + retailername);
                cartMap.put("pid", productID);
                cartMap.put("pname", productName.getText().toString());
                cartMap.put("price", productPrice.getText().toString());
                cartMap.put("date", saveCurrentDate);
                cartMap.put("time", saveCurrentTime);
                cartMap.put("quantity", numberButton.getNumber());
                cartMap.put("discount", "");
                cartMap.put("retailername", retailername);
                cartMap.put("state", "not shipped");
                cartListRef.child("User View").child(Prevalent.currentonlineUser.getName()).child("Products").child(productID).updateChildren(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    cartListRef.child("Retailer View").child(Prevalent.currentonlineUser.getName()).child("Products").child(productID).updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(RetProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                    Intent intent;
                                                    intent = new Intent(getApplicationContext(), RetailerProductsActivity.class);
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
        System.out.println(productID);
        DatabaseReference productsRef;

            productsRef= FirebaseDatabase.getInstance().getReference().child("Wholesaler Products");


        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Products products= snapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productDesc.setText(products.getDesc());
                    productPrice.setText(products.getPrice());
                    Random rd= new Random();
                    productrating.setRating((rd.nextFloat())*5);
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }






}