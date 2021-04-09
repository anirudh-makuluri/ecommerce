package com.example.oops.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oops.ProductDetailsActivity;
import com.example.oops.model.Cart;
import com.example.oops.Prevalent.Prevalent;
import com.example.oops.R;
import com.example.oops.customer.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView txtTotalAmount,txtmsg1;
    private int overalltotalprice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextProcessBtn=findViewById(R.id.next_process_btn);
        txtmsg1=findViewById(R.id.msg1);
        txtTotalAmount=findViewById(R.id.total_price);
        System.out.println("in oncreate"+overalltotalprice);
        txtTotalAmount.setText("Total price:"+overalltotalprice);
        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("In onclick"+overalltotalprice);
                txtTotalAmount.setText("Total price:"+overalltotalprice);
                Intent intent = new Intent(getApplicationContext(),ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overalltotalprice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        System.out.println("In onstart"+overalltotalprice);
        txtTotalAmount.setText("Total price:"+overalltotalprice);
        super.onStart();
        CheckOrderState();
        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(Prevalent.currentonlineUser.getName()).child("Products"),Cart.class)
                        .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                cartViewHolder.txtProductQuantity.setText("Quantity:"+cart.getQuantity());
                cartViewHolder.txtProductPrice.setText("Price:"+cart.getPrice());
                cartViewHolder.txtProductName.setText("Name:"+cart.getPname());
                int oneTypeProductprice=((Integer.valueOf(cart.getPrice())))*Integer.valueOf(cart.getQuantity());
                overalltotalprice=overalltotalprice+oneTypeProductprice;
                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"

                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 if(which==0)
                                 {
                                     Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);
                                 }
                                 if(which==1)
                                 {
                                    cartListRef.child("User View")
                                    .child(Prevalent.currentonlineUser.getName())
                                    .child("Products")
                                    .child(cart.getPid())
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(CartActivity.this, "item deleted", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    }) ;
                                 }
                            }
                        });
                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        System.out.println("end of onstart"+overalltotalprice);
        txtTotalAmount.setText("Total price:"+overalltotalprice);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
                    String username=snapshot.child("name").getValue().toString();
                    if(shippingstate.equals("shipped"))
                    {
                        txtTotalAmount.setText("Shipped success");
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        nextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "purchase more once you have received your previous order", Toast.LENGTH_SHORT).show();
                    }
                    else if(shippingstate.equals("shipped"))
                    {
                        txtTotalAmount.setText("Shipping state=Not shipped");
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        nextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "purchase more once you have received your previous order", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}