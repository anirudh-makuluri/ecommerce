package com.example.oops.wholesaler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.oops.LoginActivity;
import com.example.oops.MainActivity;
import com.example.oops.R;
import com.example.oops.wholesaler.subcats.WholesalerDairy;
import com.example.oops.wholesaler.subcats.WholesalerFruits;
import com.example.oops.wholesaler.subcats.WholesalerReadymade;
import com.example.oops.wholesaler.subcats.WholesalerVegetables;

public class WholesalerHomeActivity extends AppCompatActivity {
    private Button vegetables,fruits,dairy,readymade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaler_home);
        vegetables=findViewById(R.id.vegetables_btn);
        fruits=findViewById(R.id.fruits_btn);
        dairy=findViewById(R.id.dairy_btn);
        readymade=findViewById(R.id.readymade_btn);


        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WholesalerVegetables.class
                );
                startActivity(intent);
            }
        });

        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WholesalerFruits.class
                );
                startActivity(intent);
            }
        });

        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WholesalerDairy.class
                );
                startActivity(intent);
            }
        });

        readymade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WholesalerReadymade.class
                );
                startActivity(intent);
            }
        });
    }
}