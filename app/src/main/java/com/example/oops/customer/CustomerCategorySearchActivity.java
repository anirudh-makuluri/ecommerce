package com.example.oops.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oops.ProductDetailsActivity;
import com.example.oops.R;
import com.example.oops.customer.ViewHolder.ProductViewHolder;
import com.example.oops.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CustomerCategorySearchActivity extends AppCompatActivity {
    private RecyclerView categorysearchList;
    private String category;
    private TextView categorytxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category=getIntent().getStringExtra("category");
        setContentView(R.layout.activity_customer_category_search);
        categorysearchList=findViewById(R.id.category_search_list);
        categorysearchList.setLayoutManager(new LinearLayoutManager(CustomerCategorySearchActivity.this));
        categorytxt=findViewById(R.id.search_category_text);
        categorytxt.setText(category);


    }
    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("category").startAt(category).endAt(category),Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                        productViewHolder.txtproductname.setText(products.getPname());
                        productViewHolder.txtproductdesc.setText(products.getDesc());
                        productViewHolder.txtproductprice.setText(products.getPrice());
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);
                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                                intent.putExtra("pid",products.getPid());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        categorysearchList.setAdapter(adapter);
        adapter.startListening();
    }
}