package com.example.oops.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.R;
import com.example.oops.customer.ViewHolder.CartViewHolder;
import com.example.oops.customer.ViewHolder.OrderViewHolder;
import com.example.oops.model.Cart;
import com.example.oops.model.Orders;
import com.example.oops.retailer.RetailerCustomerActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerOrderActivity extends AppCompatActivity {
    private RecyclerView productList;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        productList=findViewById(R.id.customer_order_products_list);
        productList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Orders> options;
        try
        {
            options= new FirebaseRecyclerOptions.Builder<Orders>()
                    .setQuery(cartListRef.child("Retailer View").child(Prevalent.currentonlineUser.getName()).child("Products"), Orders.class)
                    .build();
            FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter
                    = new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull OrderViewHolder cartViewHolder, int i, @NonNull Orders cart) {
                    cartViewHolder.txtProductQuantity.setText("Quantity:"+cart.getQuantity());
                    cartViewHolder.txtProductPrice.setText("Price:"+cart.getPrice());
                    cartViewHolder.txtProductName.setText("Name:"+cart.getPname());
                    cartViewHolder.txtProductState.setText(cart.getState());

                    cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence options[]=new CharSequence[]
                                    {
                                            "Yes",
                                            "No"

                                    };
                            AlertDialog.Builder builder=new AlertDialog.Builder(CustomerOrderActivity.this);
                            builder.setTitle("Do you want to cancel this product?");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==0)
                                    {
                                        String r=getRef(i).getKey();
                                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Cart List").child("Retailer View")
                                                .child(Prevalent.currentonlineUser.getName()).child("Products").child(r);
                                        ref.removeValue();
                                    }


                                }
                            });
                            builder.show();
                        }
                    });

                }

                @NonNull
                @Override
                public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items_layout,parent,false);
                    OrderViewHolder holder = new OrderViewHolder(view);
                    return holder;
                }
            };
            productList.setAdapter(adapter);
            adapter.startListening();
        }
        catch (NullPointerException e)
        {
            Toast.makeText(this, "No orders", Toast.LENGTH_SHORT).show();

        }



    }
}