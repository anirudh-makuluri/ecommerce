package com.example.oops.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oops.MainActivity;
import com.example.oops.Prevalent.Prevalent;
import com.example.oops.R;
import com.example.oops.model.Users;
import com.example.oops.retailer.RetailerHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEdittxt, emailEdittxt, addressEdittxt, cityEdittxt;
    private Button confirmorderbtn;
    private String totalAmount = "";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        confirmorderbtn = findViewById(R.id.confirm_final_order_btn);
        nameEdittxt = findViewById(R.id.shipment_name);
        emailEdittxt = findViewById(R.id.shipment_phone);
        addressEdittxt = findViewById(R.id.shipment_address);
        cityEdittxt = findViewById(R.id.shipment_city);
        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, totalAmount, Toast.LENGTH_SHORT).show();
        confirmorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });

        FillText();

    }

    private void FillText() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Accounts").child(Prevalent.currentonlineUser.getName());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users usersdata = snapshot.getValue(Users.class);
                String name = usersdata.getName();
                String email = usersdata.getEmail();
                String address = usersdata.getAddress();
                String city = usersdata.getCity();
                nameEdittxt.setText(name);
                emailEdittxt.setText(email);
                addressEdittxt.setText(address);
                cityEdittxt.setText(city);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Check() {
        if (TextUtils.isEmpty(nameEdittxt.getText().toString())) {
            Toast.makeText(this, "provide name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(emailEdittxt.getText().toString())) {
            Toast.makeText(this, "provide email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(addressEdittxt.getText().toString())) {
            Toast.makeText(this, "provide address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cityEdittxt.getText().toString())) {
            Toast.makeText(this, "provide city", Toast.LENGTH_SHORT).show();
        } else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentonlineUser.getName());
        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEdittxt.getText().toString());
        ordersMap.put("email", emailEdittxt.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("address", addressEdittxt.getText().toString());
        ordersMap.put("city", cityEdittxt.getText().toString());
        ordersMap.put("state", "not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentonlineUser.getName())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "order placed", Toast.LENGTH_SHORT).show();
                                        String email = emailEdittxt.getText().toString();
                                        SendSmsToCustomer(email,totalAmount,nameEdittxt.getText().toString());
                                        SendSmsToRetailer(nameEdittxt.getText().toString());
                                        //Intent intent = new Intent(getApplicationContext(), PlacedActivity.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        //startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });


    }

    private void SendSmsToRetailer(String name) {
        DatabaseReference rootref=FirebaseDatabase.getInstance().getReference("Cart List").child("Retailer View").child(name).child("Products");
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
                            String messageToSend="Greetings from Krazy Kart!!!" +
                                    "\nHello "+ finalRetailername +
                                    "\n You got an order from:"+name+
                                    "\nItem:"+pname+
                                    "\nQuantity:"+quantity+
                                    "\nPrice:"+price+
                                    "\nTotal Amount:"+totalprice;
                            Properties props= new Properties();
                            props.put("mail.smtp.auth","true");
                            props.put("mail.smtp.starttls.enable","true");
                            props.put("mail.smtp.host","smtp.gmail.com");
                            props.put("mail.smtp.port","587");
                            Session session= Session.getInstance(props,
                                    new javax.mail.Authenticator(){
                                        @Override
                                        protected  PasswordAuthentication getPasswordAuthentication(){
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

    private void SendSmsToCustomer(String email,String totalAmount,String name)
    {
        final String username="krazykartoops@gmail.com";
        final String password="vamsithope";
        String messageToSend="Your order is placed successfully in Krazy Kart!!" +
                "\nHello "+name+
                "\nYour order total amount is:"+totalAmount+
                "\nCurrent status:Not shipped" +
                "\nThanks for using Krazy kart";
        Properties props= new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");
        Session session= Session.getInstance(props,
                new javax.mail.Authenticator(){
                    @Override
                    protected  PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(username,password);
                    }
                });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Message message= new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    message.setSubject("Message from Krazy Kart");
                    message.setText(messageToSend);
                    Transport.send(message);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();





    }





}