package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.databinding.ItemProductBinding;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.pojo.product.favorites.get.InnerData;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;
import com.khedr.ecommerce.ui.activities.splash.SplashActivity;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    List<InnerData> favoritesList = new ArrayList<>();
    Context context;
    SharedPreferences pref;

    public FavoritesAdapter(Context context) {
        this.context = context;
    }

    public void setFavoritesList(List<InnerData> favoritesList) {
        this.favoritesList = favoritesList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new FavoritesViewHolder(ItemProductBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull FavoritesViewHolder holder, int position) {
        pref = context.getSharedPreferences("logined", 0);
        //set product image
        UiUtils.getImageViaUrl(context, favoritesList.get(position).getProduct().getImage(), holder.b.ivProduct, holder.b.progressProductIv);


        //set product name and price;
        holder.b.tvProductPrice.setText("EGP " + favoritesList.get(position).getProduct().getPrice());
        holder.b.tvProductName.setText(favoritesList.get(position).getProduct().getName());


        if (favoritesList.get(position).getProduct().getDiscount() > 0) {

            holder.b.tvProductOldPrice.setPaintFlags(holder.b.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.b.tvProductDiscount.setText((int) favoritesList.get(position).getProduct().getDiscount() + "%");
            holder.b.tvProductDiscount.setVisibility(View.VISIBLE);
            holder.b.tvProductOldPrice.setText(String.valueOf(favoritesList.get(position).getProduct().getOld_price()));

        } else {
            holder.b.tvProductOldPrice.setVisibility(View.GONE);
            holder.b.tvProductDiscount.setVisibility(View.GONE);
        }
        holder.b.layoutProduct.setOnClickListener(v -> {

            int id = favoritesList.get(position).getProduct().getId();
            for (Product product : SplashActivity.homeResponse.getProducts()) {
                if (product.getId()==id){
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
                break;
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return favoritesList.size();
    }


    static class FavoritesViewHolder extends RecyclerView.ViewHolder {

        ItemProductBinding b;

        public FavoritesViewHolder(@NonNull @NotNull ItemProductBinding b) {
            super(b.getRoot());
            this.b = b;
        }

    }
}
