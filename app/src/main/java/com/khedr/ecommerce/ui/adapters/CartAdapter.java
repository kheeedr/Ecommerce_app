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


import com.khedr.ecommerce.databinding.ActivityCartBinding;
import com.khedr.ecommerce.databinding.ItemCartBinding;

import com.khedr.ecommerce.pojo.product.cart.get.GetCartItems;
import com.khedr.ecommerce.utils.ProductUtils;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
//import com.khedr.ecommerce.pojo.product.cart.post.PostCartResponse;
//import com.khedr.ecommerce.network.ApiInterface;
//import com.khedr.ecommerce.network.RetrofitInstance;
//import com.khedr.ecommerce.pojo.product.ProductId;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import com.khedr.ecommerce.R;
//import static com.khedr.ecommerce.ui.CartActivity.total;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public static final String TAG = "CartAdapter";
    List<GetCartItems> cartItems = new ArrayList<>();
    Context context;
    SharedPreferences pref;
    ActivityCartBinding cartBinding;

    public CartAdapter(Context context, ActivityCartBinding b) {
        this.context = context;
        this.cartBinding = b;
        pref = UserUtils.getPref(context);
    }

    @NonNull
    @NotNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartViewHolder(ItemCartBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdapter.CartViewHolder holder, int position) {

        //set product image
        UiUtils.getImageViaUrl(context, cartItems.get(position).getProduct().getImage(), holder.b.ivCartProduct, TAG);

        //set product name and price;
        holder.b.tvCartPrice.setText("EGP " + cartItems.get(position).getProduct().getPrice());
        if (cartItems.get(position).getProduct().getDiscount() > 0) {
            holder.b.tvCartOldPrice.setText(String.valueOf(cartItems.get(position).getProduct().getOld_price()));
            holder.b.tvCartOldPrice.setPaintFlags(holder.b.tvCartOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.b.tvCartDiscount.setVisibility(View.VISIBLE);
            holder.b.tvCartDiscount.setText((int) cartItems.get(position).getProduct().getDiscount() + "%");
        } else {
            holder.b.tvCartOldPrice.setVisibility(View.GONE);
            holder.b.tvCartDiscount.setVisibility(View.GONE);
        }
        holder.b.tvCartProductName.setText(cartItems.get(position).getProduct().getName());
        holder.b.tvCartEditableQuantity.setText(String.valueOf(cartItems.get(position).getQuantity()));
//        holder.b.ivCartDelete.setOnClickListener(v -> {
//            holder.b.ivCartDelete.setVisibility(View.GONE);
//            holder.b.progressCartDelete.setVisibility(View.VISIBLE);
//            cartBinding.layoutCartProgressBack.setVisibility(View.VISIBLE);
//
//            cartBinding.progressCart.setVisibility(View.VISIBLE);
//            deleteProductFromCart(position, holder);
//        });
        holder.b.layoutCartPlus.setOnClickListener(v -> {
            int oldQ = Integer.parseInt(holder.b.tvCartEditableQuantity.getText().toString());
            int newQ = oldQ + 1;
            holder.b.tvCartEditableQuantity.setText(String.valueOf(newQ));
            int id = cartItems.get(position).getId();
            ProductUtils.onClickUpdateQuantity(context, newQ, oldQ, id, holder.b.tvCartEditableQuantity, TAG, cartBinding.tvCartTotal);
        });
        holder.b.layoutCartMinus.setOnClickListener(v -> {
            int oldQ = Integer.parseInt(holder.b.tvCartEditableQuantity.getText().toString());
            if (oldQ > 1) {
                int newQ = oldQ - 1;
                holder.b.tvCartEditableQuantity.setText(String.valueOf(newQ));
                int id = cartItems.get(position).getId();
                ProductUtils.onClickUpdateQuantity(context, newQ, oldQ, id, holder.b.tvCartEditableQuantity, TAG, cartBinding.tvCartTotal);
            }
        });

    }

//
//    public void deleteProductFromCart(int position, CartViewHolder holder) {
//        if (UserUtils.isSignedIn(context)) {
//            int id = cartItems.get(position).getProduct().getId();
//            ProductId productId = new ProductId(id);
//            String token = pref.getString(context.getString(R.string.pref_user_token), "");
//            Call<PostCartResponse> call = RetrofitInstance.getRetrofitInstance()
//                    .create(ApiInterface.class).addToCart(token, productId);
//            call.enqueue(new Callback<PostCartResponse>() {
//                @SuppressLint("SetTextI18n")
//                @Override
//                public void onResponse(@NotNull Call<PostCartResponse> call, @NotNull Response<PostCartResponse> response) {
//                    if (response.body().isStatus()) {
//                        cartItems.remove(position);
//                        notifyDataSetChanged();
//                        if (!cartItems.isEmpty()) {
//                            setCartItems(cartItems);
//                            cartBinding.layoutCartFilled.setVisibility(View.VISIBLE);
//                            cartBinding.layoutCartEmpty.setVisibility(View.GONE);
//                            total -= (response.body().getData().getQuantity()) * (response.body().getData().getProduct().getPrice());
//                            cartBinding.tvCartTotal.setText("Total: " + (int) total + " EGP");
//                        } else {
//                            cartBinding.layoutCartFilled.setVisibility(View.GONE);
//                            cartBinding.layoutCartEmpty.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    holder.b.ivCartDelete.setVisibility(View.VISIBLE);
//                    holder.b.progressCartDelete.setVisibility(View.GONE);
//                    cartBinding.layoutCartProgressBack.setVisibility(View.GONE);
//                    cartBinding.progressCart.setVisibility(View.GONE);
//                    UiUtils.shortToast(context, response.body().getMessage());
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<PostCartResponse> call, @NotNull Throwable t) {
//                    UiUtils.shortToast(context, t.getMessage());
//                    holder.b.ivCartDelete.setVisibility(View.VISIBLE);
//                    holder.b.progressCartDelete.setVisibility(View.GONE);
//                    cartBinding.layoutCartProgressBack.setVisibility(View.GONE);
//                    cartBinding.progressCart.setVisibility(View.GONE);
//
//                }
//            });
//        } else {
//            UiUtils.shortToast(context, "you should login first");
//        }
//    }


    public void setCartItems(List<GetCartItems> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        ItemCartBinding b;
        public CartViewHolder(@NonNull @NotNull ItemCartBinding b) {
            super(b.getRoot());
            this.b = b;
        }

    }
}


