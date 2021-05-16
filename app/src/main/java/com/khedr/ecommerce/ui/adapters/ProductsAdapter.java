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
import com.khedr.ecommerce.ui.ProductDetailsActivity;
import com.khedr.ecommerce.ui.SplashActivity;
import com.khedr.ecommerce.utils.ProductUtils;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

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
        pref = UserUtils.getPref(context);
    }

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ProductsAdapter.ProductsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ProductsViewHolder(ItemProductBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductsAdapter.ProductsViewHolder holder, int position) {

        //set product image
        UiUtils.getImageViaUrl(context, productsList.get(position).getImage(), holder.b.ivProduct, TAG, holder.b.progressProductIv);

        //set product name and price
        holder.b.tvProductPrice.setText("EGP " + productsList.get(position).getPrice());

        if (productsList.get(position).getDiscount() > 0) {
            holder.b.tvProductOldPrice.setPaintFlags(holder.b.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.b.tvProductDiscount.setVisibility(View.VISIBLE);
            holder.b.tvProductDiscount.setText((int) productsList.get(position).getDiscount() + "%");
            holder.b.tvProductOldPrice.setText(String.valueOf(productsList.get(position).getOld_price()));
        } else {
            holder.b.tvProductOldPrice.setVisibility(View.GONE);
            holder.b.tvProductDiscount.setVisibility(View.GONE);
        }
        holder.b.tvProductName.setText(productsList.get(position).getName());
     

        holder.b.layoutProductToCart.setOnClickListener(v -> {
            int id = SplashActivity.homeResponse.getData().getProducts().get(position).getId();
            ProductUtils.addProductToCart(context, id, holder.b.layoutProductToCart, holder.b.progressProductAdd);
        });

        holder.b.layoutProduct.setOnClickListener(v -> {
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

//        ImageView ivProduct, ivProductToCart;
//        LinearLayout layoutProductToCart;
//        TextView tvProductPrice, tvProductOldPrice, tvProductName, tvProductDiscount;
//        ImageView progressProductAdd, progressProductIv;
//        ConstraintLayout layoutProduct;

        ItemProductBinding b;

        public ProductsViewHolder(@NonNull @NotNull ItemProductBinding b) {
            super(b.getRoot());
            this.b = b;
        }

//        public ProductsViewHolder(@NonNull @NotNull View itemView) {
//            super(itemView);
//            ivProduct = itemView.findViewById(R.id.iv_product);
//            tvProductName = itemView.findViewById(R.id.tv_product_name);
//            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
//            layoutProductToCart = itemView.findViewById(R.id.layout_product_to_cart);
//            tvProductOldPrice = itemView.findViewById(R.id.tv_product_old_price);
//            tvProductDiscount = itemView.findViewById(R.id.tv_product_discount);
//            ivProductToCart = itemView.findViewById(R.id.iv_product_to_cart);
//            progressProductAdd = itemView.findViewById(R.id.progress_product_add);
//            layoutProduct = itemView.findViewById(R.id.layout_product);
//            progressProductIv = itemView.findViewById(R.id.progress_product_iv);
//        }
    }
}
