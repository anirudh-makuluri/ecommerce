package com.example.oops.retailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.ProductDetailsActivity;
import com.example.oops.R;
import com.example.oops.customer.CartActivity;
import com.example.oops.customer.ViewHolder.CartViewHolder;
import com.example.oops.customer.ViewHolder.OrderViewHolder;
import com.example.oops.model.Cart;
import com.example.oops.model.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RetailerCustomerActivity extends AppCompatActivity {

    private RecyclerView productList;
    private RecyclerView.LayoutManager layoutManager;
    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_customer);
        userID=getIntent().getStringExtra("uid");
       // Toast.makeText(this, userID+" in customerhomeactivity", Toast.LENGTH_SHORT).show();
        //System.out.println(userID+" in customerhomeactivity");
        productList=findViewById(R.id.products_list);
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
                    .setQuery(cartListRef.child("Retailer View").child(userID).child("Products"), Orders.class)
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
                            CharSequence options[] = new CharSequence[]
                                    {
                                            "Order placed",
                                            "Order dispatched",
                                            "In transit",
                                            "Delivered"

                                    };
                            AlertDialog.Builder builder= new AlertDialog.Builder(RetailerCustomerActivity.this);
                            builder.setTitle("Status of Product?");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==0)
                                    {
                                        try {
                                            String text="Order Placed";
                                            String uID=getRef(i).getKey();
                                            DatabaseReference orderListRef=FirebaseDatabase.getInstance().getReference("Cart List")
                                            .child("Retailer View").child(userID).child("Products");
                                            HashMap<String, Object> userMap = new HashMap<>();
                                            userMap. put("state", text);
                                            orderListRef.child(uID).updateChildren(userMap);
                                            System.out.println(userID);
                                            System.out.println(uID);
                                            System.out.println(text);
//                                            Intent intent=new Intent(getApplicationContext(),RetailerHomeActivity.class);
//                                            startActivity(intent);



                                        }
                                        catch (IndexOutOfBoundsException e)
                                        {
                                            Toast.makeText(RetailerCustomerActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else if(which==1)
                                    {
                                        try {
                                            String text="Order dispatched";
                                            String uID=getRef(i).getKey();
                                            DatabaseReference orderListRef=FirebaseDatabase.getInstance().getReference("Cart List")
                                                    .child("Retailer View").child(userID).child("Products");
                                            HashMap<String, Object> userMap = new HashMap<>();
                                            userMap. put("state", text);
                                            orderListRef.child(uID).updateChildren(userMap);
                                            System.out.println(userID);
                                            System.out.println(uID);
                                            System.out.println(text);
//                                            Intent intent=new Intent(getApplicationContext(),RetailerHomeActivity.class);
//                                            startActivity(intent);


                                        }
                                        catch (IndexOutOfBoundsException e)
                                        {
                                            Toast.makeText(RetailerCustomerActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else if(which==2)
                                    {
                                        try {
                                            String text="In transit";
                                            String uID=getRef(i).getKey();
                                            DatabaseReference orderListRef=FirebaseDatabase.getInstance().getReference("Cart List")
                                                    .child("Retailer View").child(userID).child("Products");
                                            HashMap<String, Object> userMap = new HashMap<>();
                                            userMap. put("state", text);
                                            orderListRef.child(uID).updateChildren(userMap);
                                            System.out.println(userID);
                                            System.out.println(uID);
                                            System.out.println(text);
//                                            Intent intent=new Intent(getApplicationContext(),RetailerHomeActivity.class);
//                                            startActivity(intent);



                                        }
                                        catch (IndexOutOfBoundsException e)
                                        {
                                            Toast.makeText(RetailerCustomerActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    else if(which==3)
                                    {
                                        try {
                                            String text="Delivered";
                                            String uID=getRef(i).getKey();
                                            DatabaseReference orderListRef=FirebaseDatabase.getInstance().getReference("Cart List")
                                                    .child("Retailer View").child(userID).child("Products");
                                            HashMap<String, Object> userMap = new HashMap<>();
                                            userMap. put("state", text);
                                            orderListRef.child(uID).updateChildren(userMap);
                                            System.out.println(userID);
                                            System.out.println(uID);
                                            System.out.println(text);
//                                            Intent intent=new Intent(getApplicationContext(),RetailerHomeActivity.class);
//                                            startActivity(intent);
                                            Intent intent=new Intent(getApplicationContext(),RetailerHomeActivity.class);
                                            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                                            SmsManager sms = SmsManager.getDefault();
                                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Accounts");
                                            ref.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String phone=snapshot.child(userID).child("phone").getValue().toString();
                                                    sms.sendTextMessage("+91"+phone, null,

                                                                    "Hello "+userID+

                                                                            "\nYour order:"+cart.getPname() +" has been delivered successfully!"+
                                                                            "\nDont forget to leave a feedback in the app "
                                                                    , pi, null);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });




                                        }
                                        catch (IndexOutOfBoundsException e)
                                        {
                                            Toast.makeText(RetailerCustomerActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        //Toast.makeText(RetailerCustomerActivity.this, "nothing", Toast.LENGTH_SHORT).show();
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