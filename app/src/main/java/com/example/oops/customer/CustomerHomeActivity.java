package com.example.oops.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.oops.MainActivity;
import com.example.oops.Prevalent.Prevalent;
import com.example.oops.ProductDetailsActivity;
import com.example.oops.R;
import com.example.oops.RegisterActivity;
import com.example.oops.customer.ViewHolder.ProductViewHolder;
import com.example.oops.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class CustomerHomeActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener
         {
             private DatabaseReference ProductsRef;
             private RecyclerView recyclerView;
             RecyclerView.LayoutManager layoutManager;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(Prevalent.currentonlineUser.getName());


        Picasso.get().load(Prevalent.currentonlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

             @Override
             protected void onStart() {
                 super.onStart();
                 FirebaseRecyclerOptions<Products> options=
                         new FirebaseRecyclerOptions.Builder<Products>()
                         .setQuery(ProductsRef,Products.class).build();
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
                 recyclerView.setAdapter(adapter);
                 adapter.startListening();
             }
             @Override
             public void onBackPressed() {
                 DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                 if (drawer.isDrawerOpen(GravityCompat.START)) {
                     drawer.closeDrawer(GravityCompat.START);
                 } else {
                     super.onBackPressed();
                 }
             }

             @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }




             @SuppressWarnings("StatementWithEmptyBody")
             @Override
             public boolean onNavigationItemSelected(MenuItem item)
             {
                 // Handle navigation view item clicks here.
                 int id = item.getItemId();

                 if (id == R.id.nav_cart)
                 {
                     Intent intent = new Intent(getApplicationContext(),CartActivity.class);
                     startActivity(intent);
                 }
                 else if (id == R.id.nav_search)
                 {
                     Intent intent = new Intent(getApplicationContext(),SearchProductsActivity.class);
                     startActivity(intent);
                 }
                 else if (id == R.id.nav_categories)
                 {

                 }
                 else if (id == R.id.nav_settings)
                 {
                     Intent intent= new Intent(getApplicationContext(),Settings.class);
                     startActivity(intent);

                 }
                 else if (id == R.id.nav_logout)
                 {
                     Paper.book().destroy();
                     FirebaseAuth.getInstance().signOut();
                     Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     startActivity(intent);
                     finish();
                 }

                 DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                 drawer.closeDrawer(GravityCompat.START);
                 return true;
             }
         }