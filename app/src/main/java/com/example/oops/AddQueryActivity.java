package com.example.oops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddQueryActivity extends AppCompatActivity {
    private EditText querytext;
    private Button querybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_query);
        querytext=findViewById(R.id.query_add);
        querybtn=findViewById(R.id.query_btn);

        querybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query=querytext.getText().toString();
                String name= Prevalent.currentonlineUser.getName();
                DatabaseReference queryRef= FirebaseDatabase.getInstance().getReference("Query");
                HashMap<String,Object> userdatamap= new HashMap<>();
                userdatamap.put("query",query);
                userdatamap.put("name",name);
                queryRef.child(name).updateChildren(userdatamap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(AddQueryActivity.this, "Query updated", Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(getApplicationContext(),QueriesActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(AddQueryActivity.this, "Couldn't add your query", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
    }
}