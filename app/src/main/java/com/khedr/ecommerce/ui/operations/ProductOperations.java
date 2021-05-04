package com.khedr.ecommerce.ui.operations;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.model.product.ProductId;
import com.khedr.ecommerce.model.product.cart.post.PostCartResponse;
import com.khedr.ecommerce.model.product.cart.update.Quantity;
import com.khedr.ecommerce.model.product.cart.update.UpdateQuantityResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.ui.adapters.CartAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.khedr.ecommerce.ui.CartActivity.total;

public abstract class ProductOperations {


    public static void addProductToCart(Context context, int productId, View btToCart, ImageView progressBar) {
        if (UserOperations.isSignedIn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            UiOperations.AnimJumpAndFade(context, progressBar);
            btToCart.setVisibility(View.GONE);
            ProductId id = new ProductId(productId);
            String token = UserOperations.getPref(context).getString(context.getString(R.string.pref_user_token), "");
            Call<PostCartResponse> call = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class).addToCart(token, id);
            call.enqueue(new Callback<PostCartResponse>() {
                @Override
                public void onResponse(Call<PostCartResponse> call, Response<PostCartResponse> response) {
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    btToCart.setVisibility(View.VISIBLE);
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PostCartResponse> call, Throwable t) {
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    btToCart.setVisibility(View.VISIBLE);
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        } else {
            Toast.makeText(context, "you should login first", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            btToCart.setVisibility(View.VISIBLE);
        }
    }
    public static void updateQuantity(Context context, int newValue, int productId,View btToCart, ImageView progressBar) {
        UiOperations.AnimJumpAndFade(context, progressBar);
        String token = UserOperations.getPref(context).getString(context.getString(R.string.pref_user_token), "");
        Quantity quantity = new Quantity(newValue);
        Call<UpdateQuantityResponse> call = RetrofitInstance.getRetrofitInstance()
                .create(ApiInterface.class).updateQuantity(token, productId, quantity);
        call.enqueue(new Callback<UpdateQuantityResponse>() {
            @Override
            public void onResponse(Call<UpdateQuantityResponse> call, Response<UpdateQuantityResponse> response) {
                if (response.body().isStatus()) {

                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Sorry, " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
                btToCart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<UpdateQuantityResponse> call, Throwable t) {
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
                btToCart.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Sorry, " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public static void onClickUpdateQuantity(Context context, int newValue, int oldValue, int productId, TextView tvQuantity, String TAG, TextView cartTotal) {


        String token = UserOperations.getPref(context).getString(context.getString(R.string.pref_user_token), "");
        Quantity quantity = new Quantity(newValue);
        Call<UpdateQuantityResponse> call = RetrofitInstance.getRetrofitInstance()
                .create(ApiInterface.class).updateQuantity(token, productId, quantity);
        call.enqueue(new Callback<UpdateQuantityResponse>() {
            @Override
            public void onResponse(Call<UpdateQuantityResponse> call, Response<UpdateQuantityResponse> response) {
                if (response.body().isStatus()) {
                    if (TAG == CartAdapter.TAG) {
                        total = response.body().getData().getTotal();
                        cartTotal.setText("Total: " + String.valueOf((int) Math.ceil(total)) + " EGP");
                    }
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    tvQuantity.setText(String.valueOf(oldValue));
                    Toast.makeText(context, "Sorry, " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateQuantityResponse> call, Throwable t) {
                tvQuantity.setText(String.valueOf(oldValue));
                Toast.makeText(context, "Sorry, " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
