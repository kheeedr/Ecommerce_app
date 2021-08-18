package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ItemSelectAddressBinding;
import com.khedr.ecommerce.pojo.Address.GetAddressData;
import com.khedr.ecommerce.pojo.Address.SelectableAddressModel;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressAdapter.AddressesViewHolder> {
    Context context;

    public List<SelectableAddressModel> getAddressesList() {
        return addressesList;
    }

    List<SelectableAddressModel> addressesList = new ArrayList<>();
    OnItemClickListener onItemClickListener;

    public SelectAddressAdapter(Context context) {
        this.context = context;
        this.onItemClickListener = (OnItemClickListener) context;
    }


    @NonNull
    @Override
    public AddressesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new AddressesViewHolder(ItemSelectAddressBinding.
                inflate(LayoutInflater.from(parent.getContext()), parent, false), onItemClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull SelectAddressAdapter.AddressesViewHolder holder, int position) {
        holder.b.tvSelectAddressName.setText(addressesList.get(position).getName());
        holder.b.tvExpandedCityName.setText(context.getString(R.string.expanded_city) + " " + addressesList.get(position).getCity() + " ");
        holder.b.tvExpandedRegionName.setText(context.getString(R.string.expanded_region) + " " + addressesList.get(position).getRegion() + " ");
        holder.b.tvExpandedAddressDetails.setText(context.getString(R.string.expanded_details) + " " + addressesList.get(position).getDetails() + " ");
        if (addressesList.get(position).isSelected()) {
            holder.b.ivSelectAddressSelected.setImageResource(R.drawable.ic_true);
        } else {
            holder.b.ivSelectAddressSelected.setImageResource(R.drawable.ic_empty_circle);
        }
        if (addressesList.get(position).isExpanded()) {
            holder.b.expandableLayoutSelectAddress.setVisibility(View.VISIBLE);
            holder.b.ivSelectAddressExpand.setRotation(270);

        } else {
            holder.b.expandableLayoutSelectAddress.setVisibility(View.GONE);
            holder.b.ivSelectAddressExpand.setRotation(90);

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAddressesList(List<GetAddressData> addressesList) {
        List<SelectableAddressModel> selectableAddressesList = new ArrayList<>();
        for (GetAddressData addressData : addressesList) {
            selectableAddressesList.add(new SelectableAddressModel(addressData));
        }
        this.addressesList = selectableAddressesList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return addressesList.size();
    }

    public static class AddressesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ItemSelectAddressBinding b;
        OnItemClickListener onItemClickListener;

        public AddressesViewHolder(@NonNull @NotNull ItemSelectAddressBinding b, OnItemClickListener onItemClickListener) {
            super(b.getRoot());
            this.b = b;
            this.onItemClickListener = onItemClickListener;
            b.layoutSelectAddress.setOnClickListener(this);
            b.ivSelectAddressExpand.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v == b.layoutSelectAddress) {
                onItemClickListener.onAddressClick(getAdapterPosition(), this);
            } else if (v == b.ivSelectAddressExpand) {
                onItemClickListener.onExpandClick(getAdapterPosition(), this);
            }
        }
    }

    public interface OnItemClickListener {
        void onAddressClick(int position, AddressesViewHolder holder);

        void onExpandClick(int position, AddressesViewHolder holder);
    }
}
