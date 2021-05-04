package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.model.product.favorites.get.InnerData;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    List<InnerData> favoritesList = new ArrayList<>();
    Context context;
    private static final String TAG = "ProductsAdapter";
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
        return new FavoritesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull FavoritesViewHolder holder, int position) {
        pref = context.getSharedPreferences("logined", 0);
        //set product image
        Glide.with(context).load(favoritesList.get(position).getProduct().getImage())
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e
                            , Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(context, "glide failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "mkhedr: glide failed");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target
                            , DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "mkhedr: glide succeeded");

                        return false;
                    }
                }).into(holder.iv);

        //set product name and price;
        holder.tvProductPrice.setText("EGP " + favoritesList.get(position).getProduct().getPrice());
        holder.tvProductName.setText(favoritesList.get(position).getProduct().getName());

//        holder.btToCart.setOnLongClickListener(v -> {
//            if (v.getId() == R.id.layout_product_to_cart) {
//                addProductToFavorite(position);
//            }
//            return false;
//        });

        if (favoritesList.get(position).getProduct().getDiscount() > 0) {

            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvDiscount.setText((int) favoritesList.get(position).getProduct().getDiscount() + "%");
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvOldPrice.setText(String.valueOf(favoritesList.get(position).getProduct().getOld_price()));

        } else {
            holder.tvOldPrice.setVisibility(View.GONE);
            holder.tvDiscount.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return favoritesList.size();
    }


    static class FavoritesViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        LinearLayout btToCart;
        TextView tvProductPrice, tvProductName, tvOldPrice, tvDiscount;
        ImageView progressBar;

        public FavoritesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            btToCart = itemView.findViewById(R.id.layout_product_to_cart);
            tvDiscount = itemView.findViewById(R.id.tv_product_discount);
            tvOldPrice = itemView.findViewById(R.id.tv_product_old_price);
            progressBar = itemView.findViewById(R.id.progress_product_add);
        }
    }
}
