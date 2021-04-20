package com.example.oops.customer.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.Interface.ItemClickListener;
import com.example.oops.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtproductname, txtproductdesc,txtproductprice,txtretailername,instock,notinstock;
    public ImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtproductname = itemView.findViewById(R.id.product_name);
        txtproductdesc = itemView.findViewById(R.id.product_description);
        txtproductprice = itemView.findViewById(R.id.product_price);
        txtretailername=itemView.findViewById(R.id.retailer_name);
        instock=itemView.findViewById(R.id.instock);
        notinstock=itemView.findViewById(R.id.notinstock);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
}
