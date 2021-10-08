/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.databinding.ItemHorizintalProductBinding;
import com.khedr.ecommerce.data.model.product.Product;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.ProductsViewHolder> {

    ArrayList<Product> productsList = new ArrayList<>();
    Context context;
    OnItemClickListener mOnItemClickListener;

    public RecentlyViewedAdapter(Context context, OnItemClickListener mOnItemClickListener) {
        this.context = context;
        this.mOnItemClickListener = mOnItemClickListener;
    }


    @NonNull
    @NotNull
    @Override
    public RecentlyViewedAdapter.ProductsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ProductsViewHolder(ItemHorizintalProductBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false), mOnItemClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecentlyViewedAdapter.ProductsViewHolder holder, int position) {

        //set product image
        UiUtils.getImageViaUrl(context, productsList.get(position).getImage(), holder.b.ivProduct, holder.b.progressProductIv);

        //set product name and price
        holder.b.tvProductPrice.setText("EGP " + productsList.get(position).getPrice());

        if (productsList.get(position).getDiscount() > 0) {
            holder.b.tvProductDiscount.setVisibility(View.VISIBLE);
            holder.b.tvProductDiscount.setText((int) productsList.get(position).getDiscount() + "%");
            holder.b.tvProductOldPrice.setText(String.valueOf(productsList.get(position).getOld_price()));
            holder.b.tvProductOldPrice.setPaintFlags(holder.b.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            holder.b.tvProductOldPrice.setVisibility(View.GONE);
            holder.b.tvProductDiscount.setVisibility(View.GONE);
        }
        holder.b.tvProductName.setText(productsList.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public ArrayList<Product> getProductsList() {
        return productsList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductsList(ArrayList<Product> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ItemHorizintalProductBinding b;
        OnItemClickListener onItemClickListener;

        public ProductsViewHolder(@NonNull @NotNull ItemHorizintalProductBinding b, OnItemClickListener onItemClickListener) {
            super(b.getRoot());
            this.b = b;
            this.onItemClickListener = onItemClickListener;
            b.itemProductParent.setOnClickListener(this);
            b.layoutProductToCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == b.itemProductParent) {
                onItemClickListener.onParentClicked(getAdapterPosition());
            } else if (v == b.layoutProductToCart) {
                onItemClickListener.onItemAddToCartClicked(getAdapterPosition(), this);
            }
        }
    }

    public interface OnItemClickListener {
        void onParentClicked(int position);

        void onItemAddToCartClicked(int position, ProductsViewHolder productsViewHolder);
    }
}
