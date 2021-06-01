package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.databinding.ItemProductBinding;
import com.khedr.ecommerce.pojo.product.favorites.get.InnerData;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    List<InnerData> favoritesList = new ArrayList<>();
    Context context;
    private static final String TAG = "FavoritesAdapter";
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
        UiUtils.getImageViaUrl(context,favoritesList.get(position).getProduct().getImage(), holder.b.ivProduct, holder.b.progressProductIv);


        //set product name and price;
        holder.b.tvProductPrice.setText("EGP " + favoritesList.get(position).getProduct().getPrice());
        holder.b.tvProductName.setText(favoritesList.get(position).getProduct().getName());

//        holder.btToCart.setOnLongClickListener(v -> {
//            if (v.getId() == R.id.layout_product_to_cart) {
//                addProductToFavorite(position);
//            }
//            return false;
//        });

        if (favoritesList.get(position).getProduct().getDiscount() > 0) {

            holder.b.tvProductOldPrice.setPaintFlags(holder.b.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.b.tvProductDiscount.setText((int) favoritesList.get(position).getProduct().getDiscount() + "%");
            holder.b.tvProductDiscount.setVisibility(View.VISIBLE);
            holder.b.tvProductOldPrice.setText(String.valueOf(favoritesList.get(position).getProduct().getOld_price()));

        } else {
            holder.b.tvProductOldPrice.setVisibility(View.GONE);
            holder.b.tvProductDiscount.setVisibility(View.GONE);
        }
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
