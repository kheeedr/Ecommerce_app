/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.databinding.ItemMarginBottomBinding;
import com.khedr.ecommerce.databinding.ItemOrderStatusBinding;
import com.khedr.ecommerce.data.model.order.GetAllOrdersResponse.GetAllOrdersInnerData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "CartAdapter";
    List<GetAllOrdersInnerData> ordersData = new ArrayList<>();
    Context context;
    OnItemClickListener mOnItemClickListener;

    public OrdersAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new OrdersViewHolder(ItemOrderStatusBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false),
                    mOnItemClickListener);
        } else {
            return new MarginBottomViewHolder(ItemMarginBottomBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            OrdersViewHolder itemHolder=(OrdersViewHolder)holder;
            //set order price;
            itemHolder.b.tvOrderItemTotalCost.setText((int) ordersData.get(position).getTotal() + " EGP");
            //set order status
            itemHolder.b.tvOrderItemStatus.setText(String.valueOf(ordersData.get(position).getStatus()));
            //set order id
            itemHolder.b.tvOrderItemId.setText(ordersData.get(position).getId() + "");
            //set order date
            itemHolder.b.tvOrderDate.setText(String.valueOf(ordersData.get(position).getDate()));
        }


    }

    public List<GetAllOrdersInnerData> getList() {
        return ordersData;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setOrdersData(List<GetAllOrdersInnerData> ordersData) {
        this.ordersData = ordersData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ordersData.size()+1 ;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1) return 1;

        else return 0;
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ItemOrderStatusBinding b;
        OnItemClickListener onItemClickListener;

        public OrdersViewHolder(@NonNull ItemOrderStatusBinding b, OnItemClickListener onItemClickListener) {
            super(b.getRoot());
            this.b = b;
            this.onItemClickListener = onItemClickListener;
            b.layoutOrderItemParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == b.layoutOrderItemParent) {
                onItemClickListener.onParentClicked(getAdapterPosition());
            }
        }
    }

    public static class MarginBottomViewHolder extends RecyclerView.ViewHolder {

        public ItemMarginBottomBinding b;


        public MarginBottomViewHolder(@NonNull ItemMarginBottomBinding b) {
            super(b.getRoot());
            this.b = b;
        }


    }

    public interface OnItemClickListener {

        void onParentClicked(int position);
    }


}


