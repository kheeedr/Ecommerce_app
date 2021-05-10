package com.khedr.ecommerce.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.ui.ProductDetailsActivity;
import com.khedr.ecommerce.ui.SplashActivity;
import com.khedr.ecommerce.utils.ProductOperations;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserOperations;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    List<Product> productsList = new ArrayList<>();
    Context context;
    SharedPreferences pref;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductsAdapter.ProductsViewHolder holder, int position) {

        //set product image
        UiUtils.getImageViaUrl(context, productsList.get(position).getImage(), holder.ivProduct, TAG, holder.ivProgressbar);

        //set product name and price
        holder.tvProductPrice.setText("EGP " + productsList.get(position).getPrice());

        if (productsList.get(position).getDiscount() > 0) {
            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvDiscount.setText((int) productsList.get(position).getDiscount() + "%");
            holder.tvOldPrice.setText(String.valueOf(productsList.get(position).getOld_price()));
        } else {
            holder.tvOldPrice.setVisibility(View.GONE);
            holder.tvDiscount.setVisibility(View.GONE);
        }
        holder.tvProductName.setText(productsList.get(position).getName());
     

        holder.btToCart.setOnClickListener(v -> {
            int id = SplashActivity.homeResponse.getData().getProducts().get(position).getId();
            ProductOperations.addProductToCart(context, id, holder.btToCart, holder.addProgressbar);
        });

        holder.productLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("product", productsList.get(position));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    static class ProductsViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProduct, ivToCart;
        LinearLayout btToCart;
        TextView tvProductPrice, tvOldPrice, tvProductName, tvDiscount;
        ImageView addProgressbar, ivProgressbar;
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
            ivProgressbar = itemView.findViewById(R.id.progress_product_iv);
        }
    }
}
