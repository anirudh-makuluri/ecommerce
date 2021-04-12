package com.example.oops.retailer;

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

import com.example.oops.R;
import com.example.oops.model.RetailerOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RetailerNewOrderActivity extends AppCompatActivity {
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_new_order);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList=findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<RetailerOrders> options=
                new FirebaseRecyclerOptions.Builder<RetailerOrders>()
                .setQuery(ordersRef,RetailerOrders.class)
                .build();
        FirebaseRecyclerAdapter<RetailerOrders,RetailerViewHolder> adapter=
                new FirebaseRecyclerAdapter<RetailerOrders, RetailerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RetailerViewHolder retailerViewHolder,final int i, @NonNull RetailerOrders retailerOrders) {
                        retailerViewHolder.username.setText("name:"+retailerOrders.getName());
                        retailerViewHolder.userphone.setText("phone:"+retailerOrders.getPhone());
                        retailerViewHolder.usertotalprice.setText("total amount:"+retailerOrders.getTotalAmount());
                        retailerViewHolder.userdatetime.setText("date:"+retailerOrders.getDate());
                        retailerViewHolder.usershippingaddress.setText("address:"+retailerOrders.getAddress()+" "+retailerOrders.getCity());
                        retailerViewHolder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uID=getRef(i).getKey();
                                Intent intent = new Intent(getApplicationContext(),RetailerCustomerActivity.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });
                        retailerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[]= new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder= new AlertDialog.Builder(RetailerNewOrderActivity.this);
                                builder.setTitle("Have you shipped this order?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0)
                                        {
                                            String uID=getRef(i).getKey();
                                            RemoveOrder(uID);
                                        }
                                        else
                                        {
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RetailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderslayout,parent,false);
                        return new RetailerViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String uID) {
        ordersRef.child(uID).removeValue();
    }

    public static class RetailerViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username,userphone,usertotalprice,userdatetime,usershippingaddress;
        public Button ShowOrdersBtn;
        public RetailerViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.order_user_name);
            userphone=itemView.findViewById(R.id.order_phone_number);
            usertotalprice=itemView.findViewById(R.id.order_total_price);
            userdatetime=itemView.findViewById(R.id.order_date_time);
            usershippingaddress=itemView.findViewById(R.id.order_city);
            ShowOrdersBtn=itemView.findViewById(R.id.show_all_products_btn);


        }

    }


}