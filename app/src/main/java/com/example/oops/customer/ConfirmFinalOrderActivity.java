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

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEdittxt, phoneEdittxt, addressEdittxt, cityEdittxt;
    private Button confirmorderbtn;
    private String totalAmount = "";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        confirmorderbtn = findViewById(R.id.confirm_final_order_btn);
        nameEdittxt = findViewById(R.id.shipment_name);
        phoneEdittxt = findViewById(R.id.shipment_phone);
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
                String phone = usersdata.getPhone();
                String address = usersdata.getAddress();
                String city = usersdata.getCity();
                nameEdittxt.setText(name);
                phoneEdittxt.setText(phone);
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
        } else if (TextUtils.isEmpty(phoneEdittxt.getText().toString())) {
            Toast.makeText(this, "provide phone number", Toast.LENGTH_SHORT).show();
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
        ordersMap.put("phone", phoneEdittxt.getText().toString());
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
                                        String phone = phoneEdittxt.getText().toString();
                                        SendSmsToCustomer(phone,totalAmount,nameEdittxt.getText().toString());
                                        SendSmsToRetailer(nameEdittxt.getText().toString());
                                        Intent intent = new Intent(getApplicationContext(), CustomerHomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
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
                    String retailername=postsnapshot.child("retailername").getValue().toString();
                    String pname=postsnapshot.child("pname").getValue().toString();
                    String price=postsnapshot.child("price").getValue().toString();
                    String quantity=postsnapshot.child("quantity").getValue().toString();
                    System.out.println("retailer name:"+retailername);

                    DatabaseReference phoneref=FirebaseDatabase.getInstance().getReference("Accounts");
                    phoneref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                String phone=snapshot.child(retailername).child("phone").getValue().toString();
                                System.out.println(retailername+":"+phone);
                                Intent intent = new Intent(getApplicationContext(), CustomerHomeActivity.class);
                                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                                SmsManager sms = SmsManager.getDefault();
                                sms.sendTextMessage("+91"+phone, null,
                                        "Greetings from Krazy Kart!!!" +
                                                "\nHello "+retailername +
                                                "\n You got an order from:"+name+
                                                "\nItem:"+pname+
                                                "\nQuantity:"+quantity+
                                                "\nPrice:"+price, pi, null);
                            }catch (NullPointerException e)
                            {

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



    }

    private void SendSmsToCustomer(String phone,String totalAmount,String name)
    {
        Intent intent = new Intent(getApplicationContext(), CustomerHomeActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+91"+phone, null,
                "Your order is placed successfully in Krazy Kart!!" +
                         "\nHello "+name+
                         "\nYour order total amount is:"+totalAmount+
                         "\nCurrent status:Not shipped" +
                         "\nThanks for using Krazy kart", pi, null);
    }




}