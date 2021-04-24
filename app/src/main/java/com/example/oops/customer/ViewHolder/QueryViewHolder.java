package com.example.oops.customer.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.Interface.ItemClickListener;
import com.example.oops.R;

public class QueryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtname,txtquery;
    private ItemClickListener itemClickListener;

    public QueryViewHolder(@NonNull View itemView) {
        super(itemView);
        txtname=itemView.findViewById(R.id.query_name);
        txtquery=itemView.findViewById(R.id.query_text);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
