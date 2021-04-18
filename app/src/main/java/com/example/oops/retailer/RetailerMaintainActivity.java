package com.example.oops.retailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class RetailerMaintainActivity extends AppCompatActivity {
    private Button applychangesbtn,deletebtn;
    private EditText name,price,desc;
    private ImageView imageView;
    private String productID="";
    private DatabaseReference productsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_maintain);
        applychangesbtn=findViewById(R.id.apply_changes_btn);
        name=findViewById(R.id.maintain_product_name);
        price=findViewById(R.id.maintain_product_price);
        desc=findViewById(R.id.maintain_product_description);
        imageView=findViewById(R.id.maintain_product_image);
        deletebtn=findViewById(R.id.delete_product_btn);
        productID=getIntent().getStringExtra("pid");
        productsRef= FirebaseDatabase.getInstance().getReference().child("Products")
                .child(productID);
        
        displayProductInfo();

        applychangesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletethisproduct();
            }
        });


    }

    private void deletethisproduct() {

        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(RetailerMaintainActivity.this, "Deleted....Reduced to ashes", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),RetailerHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void applyChanges() {

        String Pname=name.getText().toString().toLowerCase();
        String Pprice=price.getText().toString();
        String Pdescription=desc.getText().toString();

        if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "enter name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pprice))
        {
            Toast.makeText(this, "enter price", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pdescription))
        {
            Toast.makeText(this, "enter description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> productmap=new HashMap<>();
            productmap.put("pid",productID);
            productmap.put("desc",Pdescription);
            productmap.put("price",Pprice);
            productmap.put("pname",Pname);
            productsRef.updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful())
                 {
                     Toast.makeText(RetailerMaintainActivity.this, "applied chnages successfully", Toast.LENGTH_SHORT).show();
                 Intent intent= new Intent(getApplicationContext(),RetailerHomeActivity.class);
                 startActivity(intent);
                 finish();
                 }
                }
            });
        }
    }

    private void displayProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String pName=snapshot.child("pname").getValue().toString();
                    String pPrice=snapshot.child("price").getValue().toString();
                    String pDescription=snapshot.child("desc").getValue().toString();
                    String pImage=snapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    desc.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}