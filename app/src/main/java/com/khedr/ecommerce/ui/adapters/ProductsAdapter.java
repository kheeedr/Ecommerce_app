package com.khedr.ecommerce.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.model.product.Product;
import com.khedr.ecommerce.model.product.ProductId;
import com.khedr.ecommerce.model.product.favorites.post.PostFavoriteResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.ui.ProductDetailsActivity;
import com.khedr.ecommerce.ui.SplashActivity;
import com.khedr.ecommerce.ui.operations.ProductOperations;
import com.khedr.ecommerce.ui.operations.UiOperations;
import com.khedr.ecommerce.ui.operations.UserOperations;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    List<Product> productsList = new ArrayList<>();
    Context context;
    SharedPreferences pref ;
    public static final String TAG = "ProductsAdapter";

    public ProductsAdapter(Context context) {
        this.context = context;
        pref = UserOperations.getPref(context);
    }


    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ProductsAdapter.ProductsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ProductsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductsAdapter.ProductsViewHolder holder, int position) {

        //set product image
        UiOperations.getImageViaUrl(context, productsList.get(position).getImage(), holder.ivProduct,TAG,holder.ivProgressbar);

        //set product name and price;
        holder.tvProductPrice.setText("EGP " + String.valueOf(productsList.get(position).getPrice()));
//        if (productsList.get(position).isIn_cart()) {
//            holder.ivToCart.setImageResource(R.drawable.ic_true);
//        } else {
//            holder.ivToCart.setImageResource(R.drawable.ic_add);
//        }
        if (productsList.get(position).getDiscount() > 0) {
            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvDiscount.setText(String.valueOf((int) productsList.get(position).getDiscount()) + "%");
            holder.tvOldPrice.setText(String.valueOf(productsList.get(position).getOld_price()));
        } else {
            holder.tvOldPrice.setVisibility(View.GONE);
            holder.tvDiscount.setVisibility(View.GONE);
        }
        holder.tvProductName.setText(productsList.get(position).getName());
        holder.btToCart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (v.getId() == R.id.layout_product_to_cart) {
                    addProductToFavorite(position);
                }
                return false;
            }
        });

        holder.btToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                int id = SplashActivity.homeResponse.getData().getProducts().get(position).getId();
                ProductOperations.addProductToCart(context, id, holder.btToCart, holder.addProgressbar);
            }
        });
        holder.productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);

                intent.putExtra("product",  productsList.get(position));
//                intent.putExtra("product_image", productsList.get(position).getImage());
//                intent.putExtra("product_name", productsList.get(position).getName());
//                intent.putExtra("product_id", productsList.get(position).getId());
//                intent.putExtra("product_description", productsList.get(position).getDescription());
//                intent.putExtra("product_is_favourite", productsList.get(position).isIn_favorites());
//                intent.putExtra("product_is_cart", productsList.get(position).isIn_cart());
//                intent.putExtra("product_price", productsList.get(position).getPrice());
//                intent.putExtra("product_discount", productsList.get(position).getDiscount());
//                intent.putExtra("product_old_price", productsList.get(position).getOld_price());
//                intent.putStringArrayListExtra("images_list", productsList.get(position).getImages());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public void addProductToFavorite(int position) {

        if (UserOperations.isSignedIn(context)) {
            int id = SplashActivity.homeResponse.getData().getProducts().get(position).getId();
            ProductId productId = new ProductId(id);
            String token = pref.getString(context.getString(R.string.pref_user_token), "");
            Call<PostFavoriteResponse> call = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class).addToFavorite(token, productId);
            call.enqueue(new Callback<PostFavoriteResponse>() {
                @Override
                public void onResponse(Call<PostFavoriteResponse> call, Response<PostFavoriteResponse> response) {

                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<PostFavoriteResponse> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(context, "you should login first", Toast.LENGTH_LONG).show();
        }
    }


    static class ProductsViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProduct, ivToCart;
        LinearLayout btToCart;
        TextView tvProductPrice, tvOldPrice, tvProductName, tvDiscount;
        ImageView addProgressbar,ivProgressbar;
        ConstraintLayout productLayout;

        public ProductsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            btToCart = itemView.findViewById(R.id.layout_product_to_cart);
            tvOldPrice = itemView.findViewById(R.id.tv_product_old_price);
            tvDiscount = itemView.findViewById(R.id.tv_product_discount);
            ivToCart = itemView.findViewById(R.id.iv_product_to_cart);
            addProgressbar = itemView.findViewById(R.id.progress_product_add);
            productLayout = itemView.findViewById(R.id.layout_product);
            ivProgressbar=itemView.findViewById(R.id.progress_product_iv);
        }
    }
}
