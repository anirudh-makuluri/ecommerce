package com.example.oops;

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
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.customer.CustomerOrderActivity;
import com.example.oops.customer.ViewHolder.QueryViewHolder;
import com.example.oops.model.Orders;
import com.example.oops.model.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QueriesActivity extends AppCompatActivity {
     private RecyclerView queryList;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addQuery;
    private DatabaseReference queryListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queries);
        queryList=findViewById(R.id.queries_list);
        queryList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        queryList.setLayoutManager(layoutManager);
        addQuery=findViewById(R.id.add_query);
        addQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddQuery();
            }
        });

    }

    private void AddQuery()
    {
        Intent intent= new Intent(getApplicationContext(),AddQueryActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
         queryListRef= FirebaseDatabase.getInstance().getReference();
        FirebaseRecyclerOptions<Query> options;
        try
        {
            options= new FirebaseRecyclerOptions.Builder<Query>()
                    .setQuery(queryListRef.child("Query"), Query.class)
                    .build();
            FirebaseRecyclerAdapter<Query, QueryViewHolder> adapter=
                    new FirebaseRecyclerAdapter<Query, QueryViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull QueryViewHolder queryViewHolder, int i, @NonNull Query query) {
                            queryViewHolder.txtquery.setText(query.getQuery());
                            queryViewHolder.txtname.setText(query.getName());
                            if(Prevalent.currentonlineUser.getName().equals(query.getName()))
                            {
                                queryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CharSequence options[]=new CharSequence[]
                                                {
                                                        "Yes",
                                                        "No"

                                                };
                                        AlertDialog.Builder builder=new AlertDialog.Builder(QueriesActivity.this);
                                        builder.setTitle("Is your query solved?");
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(which==0)
                                                {
                                                    String uID=getRef(i).getKey();
                                                    RemoveOrder(uID);

                                                }


                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }

                        }

                        @NonNull
                        @Override
                        public QueryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.queries_layout,parent,false);
                            QueryViewHolder holder= new QueryViewHolder(view);
                            return  holder;


                        }
                    };
            queryList.setAdapter(adapter);
            adapter.startListening();

        }
        catch (NullPointerException e)
        {
            Toast.makeText(this, "No Queries", Toast.LENGTH_SHORT).show();
        }



    }

    private void RemoveOrder(String uID) {
        queryListRef.child("Query").child(uID).removeValue();
    }
}