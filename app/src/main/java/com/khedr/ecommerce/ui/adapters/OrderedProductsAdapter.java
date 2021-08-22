/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */
package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.databinding.ItemOrderedProductBinding;
import com.khedr.ecommerce.pojo.order.GetOrderDetailsResponse.OrderDetailsProduct;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class OrderedProductsAdapter extends RecyclerView.Adapter<OrderedProductsAdapter.CartViewHolder> {

    List<OrderDetailsProduct> orderedItems = new ArrayList<>();
    Context context;

    public OrderedProductsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public OrderedProductsAdapter.CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartViewHolder(ItemOrderedProductBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderedProductsAdapter.CartViewHolder holder, int position) {

        //set product image
        UiUtils.getImageViaUrl(context, orderedItems.get(position).getImage(), holder.b.ivOrderedProductProduct, false);

        //set product price;
        holder.b.tvOrderedProductPrice.setText("EGP " + (int) orderedItems.get(position).getPrice());


        //set product name
        holder.b.tvOrderedProductName.setText(orderedItems.get(position).getName());
        //set product quantity
        holder.b.tvOrderedProductEditableQuantity.setText(String.valueOf(orderedItems.get(position).getQuantity()));

    }


    public List<OrderDetailsProduct> getList() {
        return orderedItems;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setOrderedItems(List<OrderDetailsProduct> orderedItems) {
        this.orderedItems = orderedItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return orderedItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        public ItemOrderedProductBinding b;


        public CartViewHolder(@NonNull ItemOrderedProductBinding b) {
            super(b.getRoot());
            this.b = b;

        }

    }


}


