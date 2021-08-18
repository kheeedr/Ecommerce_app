package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ItemBannerBinding;
import com.khedr.ecommerce.pojo.homeapi.Banner;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BannersAdapter extends RecyclerView.Adapter<BannersAdapter.BannersViewHolder> {

    List<Banner>bannersList=new ArrayList<>();
    Context context;

    public BannersAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBannersList(List<Banner> bannersList) {
        this.bannersList = bannersList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public BannersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new BannersViewHolder(ItemBannerBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BannersAdapter.BannersViewHolder holder, int position) {

        UiUtils.getImageViaUrl(context,bannersList.get(position).getImage(), holder.b.ivBannerItem, holder.b.progressBannerIv);
        holder.b.getRoot().setOnClickListener(v ->UiUtils.shortToast(context,context.getString(R.string.coming_soon)));
    }

    @Override
    public int getItemCount() {
        return bannersList.size();
    }

    static class BannersViewHolder extends RecyclerView.ViewHolder{

        ItemBannerBinding b;

        public BannersViewHolder(@NonNull @NotNull ItemBannerBinding b) {
            super(b.getRoot());
            this.b = b;
        }

    }
}
