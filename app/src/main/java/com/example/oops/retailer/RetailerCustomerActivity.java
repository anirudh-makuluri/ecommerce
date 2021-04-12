package com.example.oops.retailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oops.R;
import com.example.oops.customer.ViewHolder.CartViewHolder;
import com.example.oops.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RetailerCustomerActivity extends AppCompatActivity {

    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_customer);
        userID=getIntent().getStringExtra("uid");
        Toast.makeText(this, userID+" in customerhomeactivity", Toast.LENGTH_SHORT).show();
        productList=findViewById(R.id.products_list);
        productList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);
        cartListRef= FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Retailer View").child(userID).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart>options= new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                       // Toast.makeText(RetailerCustomerActivity.this, cart.getQuantity(), Toast.LENGTH_SHORT).show();
                        cartViewHolder.txtProductQuantity.setText("Quantity:"+cart.getQuantity());
                        cartViewHolder.txtProductPrice.setText("Price:"+cart.getPrice());
                        cartViewHolder.txtProductName.setText("Name:"+cart.getPname());

                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };

        productList.setAdapter(adapter);
        adapter.startListening();
    }
}