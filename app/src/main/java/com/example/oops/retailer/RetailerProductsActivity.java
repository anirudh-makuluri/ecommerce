package com.example.oops.retailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.ProductDetailsActivity;
import com.example.oops.R;
import com.example.oops.customer.CartActivity;
import com.example.oops.customer.CustomerHomeActivity;
import com.example.oops.customer.CustomerOrderActivity;
import com.example.oops.customer.SearchProductsActivity;
import com.example.oops.customer.ViewHolder.ProductViewHolder;
import com.example.oops.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.ImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class RetailerProductsActivity extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String type="";
    private ImageView searchbtn;
    int coun=0;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_products);

        searchbtn=findViewById(R.id.retailer_product_search);
        try{
            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
            if(bundle!=null)
                type=getIntent().getExtras().get("Admin").toString();
        }
        catch (NullPointerException e)
        {
            System.out.println(e);
        }
        searchbtn.setVisibility(View.INVISIBLE);

        if(type.equals("Admin"))
        {
            searchbtn.setVisibility(View.INVISIBLE);
        }
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!type.equals("Admin"))
                {
                    Intent intent = new Intent(getApplicationContext(), SearchProductsActivity.class);
                    startActivity(intent);
                }
            }
        });



        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.retailer_toolbar);
        toolbar.setTitle("Products");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.retailer_fab);
        if(type.equals("Admin")) fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("Admin"))
                {
                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(intent);
                }
            }
        });


        FloatingActionButton orders = findViewById(R.id.retailer_orders);
        if(type.equals("Admin")) orders.setVisibility(View.INVISIBLE);
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("Admin"))
                {
                    Intent intent = new Intent(getApplicationContext(), CustomerOrderActivity.class);
                    startActivity(intent);
                }
            }
        });




        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }



    @Override
    protected void onStart() {
        super.onStart();
        //FirebaseRecyclerOptions<Products> options;
        if(!type.equals("Admin")) {
            ProductsRef=FirebaseDatabase.getInstance().getReference().child("Wholesaler Products");
            FirebaseRecyclerOptions<Products> options=
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(ProductsRef,Products.class)
                            .build();

            FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                    new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                            productViewHolder.txtproductname.setText(products.getPname());
                            productViewHolder.txtproductdesc.setText(products.getDesc());
                            productViewHolder.txtproductprice.setText(products.getPrice());
                            productViewHolder.txtretailername.setText(products.getRetailername());
                            if(products.getStock().equals("in stock"))
                            {
                                productViewHolder.instock.setVisibility(View.VISIBLE);
                                productViewHolder.notinstock.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                productViewHolder.instock.setVisibility(View.INVISIBLE);
                                productViewHolder.notinstock.setVisibility(View.VISIBLE);
                            }
                            Picasso.get().load(products.getImage()).into(productViewHolder.imageView);
                            productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(products.getStock().equals("in stock"))
                                    {

                                        Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                                        intent.putExtra("pid",products.getPid());
                                        intent.putExtra("type","retailer");
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        Toast.makeText(RetailerProductsActivity.this, "This product is currently not in stock", Toast.LENGTH_SHORT).show();
                                    }


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
            recyclerView.setAdapter(adapter);
            adapter.startListening();

        }
        else{
            ProductsRef=FirebaseDatabase.getInstance().getReference("Wholesaler Products");
            FirebaseRecyclerOptions<Products> options;
            options =
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(ProductsRef.orderByChild("wholesalername").startAt(Prevalent.currentonlineUser.getName()).endAt(Prevalent.currentonlineUser.getName()), Products.class).build();
            FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                    new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                            productViewHolder.txtproductname.setText(products.getPname());
                            productViewHolder.txtproductdesc.setText(products.getDesc());
                            productViewHolder.txtproductprice.setText(products.getPrice());
                            productViewHolder.txtretailername.setText(products.getRetailername());
                            if(products.getStock().equals("in stock"))
                            {
                                productViewHolder.instock.setVisibility(View.VISIBLE);
                                productViewHolder.notinstock.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                productViewHolder.instock.setVisibility(View.INVISIBLE);
                                productViewHolder.notinstock.setVisibility(View.VISIBLE);
                            }
                            Picasso.get().load(products.getImage()).into(productViewHolder.imageView);


                            productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(type.equals("Admin"))
                                    {
                                        Intent intent = new Intent(getApplicationContext(), RetailerMaintainActivity.class);
                                        intent.putExtra("type","retailer");
                                        intent.putExtra("pid",products.getPid());
                                        startActivity(intent);
                                    }

                                    else
                                    {  if(products.getStock().equals("in stock"))
                                    {
                                        Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                                        intent.putExtra("pid",products.getPid());
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(RetailerProductsActivity.this, "This product is currently not in stock", Toast.LENGTH_SHORT).show();
                                    }

                                    }




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
            recyclerView.setAdapter(adapter);
            adapter.startListening();

        }


    }

    private Double CalculateDistance(String retailerlat, String retailerlon, String customerlat, String customerlon) {
        double lat1=Double.valueOf(retailerlat);
        double lon1=Double.valueOf(retailerlon);
        double lat2=Double.valueOf(customerlat);
        double lon2=Double.valueOf(customerlon);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);

    }



}