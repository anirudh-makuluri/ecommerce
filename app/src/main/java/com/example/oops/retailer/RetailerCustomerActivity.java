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
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oops.MainActivity;
import com.example.oops.Prevalent.Prevalent;
import com.example.oops.ProductDetailsActivity;
import com.example.oops.R;
import com.example.oops.customer.CartActivity;
import com.example.oops.customer.CustomerOrderActivity;
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
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
//                                            Intent intent=new Intent(getApplicationContext(),RetailerMaintainActivity.class);
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
//                                            Intent intent=new Intent(getApplicationContext(),RetailerMaintainActivity.class);
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
//                                            Intent intent=new Intent(getApplicationContext(),RetailerMaintainActivity.class);
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
                                            SendSmsToCustomer(userID,cart.getPname());






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

    private void SendSmsToCustomer(String userID,String product) {
        DatabaseReference rootref=FirebaseDatabase.getInstance().getReference("Cart List").child("Retailer View").child(userID).child("Products");
        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postsnapshot:snapshot.getChildren())
                {
                    String retailername = null;
                    try {
                        retailername=postsnapshot.child("retailername").getValue().toString();
                    }catch (NullPointerException e){}

                    if(retailername==null){
                        retailername=postsnapshot.child("wholesalername").getValue().toString();
                    }
                    String pname=postsnapshot.child("pname").getValue().toString();
                    String price=postsnapshot.child("price").getValue().toString();
                    String quantity=postsnapshot.child("quantity").getValue().toString();
                    System.out.println("retailer name:"+retailername);

                    DatabaseReference phoneref=FirebaseDatabase.getInstance().getReference("Accounts");
                    String finalRetailername = retailername;
                    phoneref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String email=snapshot.child(finalRetailername).child("email").getValue().toString();
                            final String username="krazykartoops@gmail.com";
                            final String password="vamsithope";
                            int q=Integer.valueOf(quantity);
                            int p=Integer.valueOf(price);
                            int totalprice=q*p;
                            String messageToSend="Hello "+userID+
                                    "\nYour order:"+product+" has been delivered successfully!"+
                                    "\nDont forget to leave a feedback in the app ";
                            Properties props= new Properties();
                            props.put("mail.smtp.auth","true");
                            props.put("mail.smtp.starttls.enable","true");
                            props.put("mail.smtp.host","smtp.gmail.com");
                            props.put("mail.smtp.port","587");
                            Session session= Session.getInstance(props,
                                    new javax.mail.Authenticator(){
                                        @Override
                                        protected PasswordAuthentication getPasswordAuthentication(){
                                            return new PasswordAuthentication(username,password);
                                        }
                                    });
                            try{
                                Message message= new MimeMessage(session);
                                message.setFrom(new InternetAddress(username));
                                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                                message.setSubject("Message from Krazy Kart");
                                message.setText(messageToSend);
                                Transport.send(message);
                                // Toast.makeText(MainActivity.this, "sent mail", Toast.LENGTH_SHORT).show();
                            }
                            catch (MessagingException e){
                                throw new RuntimeException(e);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }


}