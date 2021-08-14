package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.databinding.ItemShowAdressesBinding;
import com.khedr.ecommerce.pojo.Address.GetAddressData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressesViewHolder> {
    Context context;

    public List<GetAddressData> getAddressesList() {
        return AddressesList;
    }

    List<GetAddressData> AddressesList = new ArrayList<>();
    OnItemClickListener onItemClickListener;

    public AddressesAdapter(Context context) {
        this.context = context;
        this.onItemClickListener = (OnItemClickListener)context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull AddressesAdapter.AddressesViewHolder holder, int position) {
        holder.b.tvAddressName.setText(AddressesList.get(position).getName());


    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAddressesList(List<GetAddressData> addressesList) {
        this.AddressesList = addressesList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AddressesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new AddressesViewHolder(ItemShowAdressesBinding.
                inflate(LayoutInflater.from(parent.getContext()), parent, false),onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return AddressesList.size();
    }

    public static class AddressesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemShowAdressesBinding b;
        OnItemClickListener onItemClickListener;

        public AddressesViewHolder(@NonNull @NotNull ItemShowAdressesBinding b, OnItemClickListener onItemClickListener) {
            super(b.getRoot());
            this.b = b;
            this.onItemClickListener = onItemClickListener;
            b.layoutAddress.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            onItemClickListener.onAddressClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onAddressClick(int position);
    }
}
