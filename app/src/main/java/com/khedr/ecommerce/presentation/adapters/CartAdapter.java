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

import com.khedr.ecommerce.databinding.ItemCartBinding;
import com.khedr.ecommerce.data.model.product.cart.get.GetCartItems;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public static final String TAG = "CartAdapter";
    List<GetCartItems> cartItems = new ArrayList<>();
    Context context;
    OnItemClickListener mOnItemClickListener;

    public CartAdapter(Context context) {
        this.context = context;
        this.mOnItemClickListener = (OnItemClickListener)context;
    }

    @NonNull
    @NotNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartViewHolder(ItemCartBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false), mOnItemClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdapter.CartViewHolder holder, int position) {

        //set product image
        UiUtils.getImageViaUrl(context, cartItems.get(position).getProduct().getImage(), holder.b.ivCartProduct, false);

        //set product price;
        holder.b.tvCartPrice.setText("EGP " + cartItems.get(position).getProduct().getPrice());
        if (cartItems.get(position).getProduct().getDiscount() > 0) {
            holder.b.tvCartOldPrice.setText(String.valueOf(cartItems.get(position).getProduct().getOld_price()));
            holder.b.tvCartOldPrice.setPaintFlags(holder.b.tvCartOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.b.tvCartDiscount.setVisibility(View.VISIBLE);
            holder.b.tvCartDiscount.setText((int) cartItems.get(position).getProduct().getDiscount() + "%");
        } else {
            holder.b.tvCartOldPrice.setVisibility(View.GONE);
            holder.b.tvCartDiscount.setVisibility(View.GONE);
        }

        //set product name
        holder.b.tvCartProductName.setText(cartItems.get(position).getProduct().getName());
        //set product quantity
        holder.b.tvCartEditableQuantity.setText(String.valueOf(cartItems.get(position).getQuantity()));

    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList() {
        setCartItems(cartItems);
        notifyDataSetChanged();
    }

    public List<GetCartItems> getList() {
        return cartItems;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCartItems(List<GetCartItems> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ItemCartBinding b;
        OnItemClickListener onItemClickListener;

        public CartViewHolder(@NonNull @NotNull ItemCartBinding b, OnItemClickListener onItemClickListener) {
            super(b.getRoot());
            this.b = b;
            this.onItemClickListener = onItemClickListener;
            b.ivCartDelete.setOnClickListener(this);
            b.layoutCartPlus.setOnClickListener(this);
            b.layoutCartMinus.setOnClickListener(this);
            b.cartItemParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == b.ivCartDelete) {
                onItemClickListener.onProductDeleteClicked(getAdapterPosition());
            } else if (v == b.layoutCartPlus) {
                onItemClickListener.onPlusClicked(getAdapterPosition(), this);
            } else if (v == b.layoutCartMinus) {
                onItemClickListener.onMinusClicked(getAdapterPosition(), this);
            }else if (v==b.cartItemParent){
                onItemClickListener.onParentClicked(getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onProductDeleteClicked(int position);

        void onPlusClicked(int position, CartViewHolder holder);

        void onMinusClicked(int position, CartViewHolder holder);

        void onParentClicked(int position);
    }


}


