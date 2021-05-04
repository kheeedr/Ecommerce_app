package com.khedr.ecommerce.ui.operations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.model.product.ProductId;
import com.khedr.ecommerce.model.product.cart.get.GetCartItems;
import com.khedr.ecommerce.model.product.cart.get.GetCartResponse;
import com.khedr.ecommerce.model.product.cart.post.PostCartResponse;
import com.khedr.ecommerce.model.product.cart.update.Quantity;
import com.khedr.ecommerce.model.product.cart.update.UpdateQuantityResponse;
import com.khedr.ecommerce.model.product.favorites.post.PostFavoriteResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;

import com.khedr.ecommerce.ui.adapters.CartAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
                public void onResponse(@NotNull Call<PostCartResponse> call, @NotNull Response<PostCartResponse> response) {
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    btToCart.setVisibility(View.VISIBLE);

                    if (response.body() != null) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PostCartResponse> call, @NotNull Throwable t) {
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

    // from product
    public static void updateQuantity(Context context, int newValue, int productId, View btToCart, ImageView progressBar) {
        btToCart.setVisibility(View.GONE);
        UiOperations.AnimJumpAndFade(context, progressBar);

        String token = UserOperations.getPref(context).getString(context.getString(R.string.pref_user_token), "");
        Quantity quantity = new Quantity(newValue);
        Call<UpdateQuantityResponse> call = RetrofitInstance.getRetrofitInstance()
                .create(ApiInterface.class).updateQuantity(token, productId, quantity);
        call.enqueue(new Callback<UpdateQuantityResponse>() {
            @Override
            public void onResponse(@NotNull Call<UpdateQuantityResponse> call, @NotNull Response<UpdateQuantityResponse> response) {
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Sorry, " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Sorry, connection error", Toast.LENGTH_SHORT).show();
                }
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
                btToCart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<UpdateQuantityResponse> call, @NotNull Throwable t) {
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
                btToCart.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Sorry, " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //from cart
    public static void onClickUpdateQuantity(Context context, int newValue, int oldValue, int productId, TextView tvQuantity, String TAG, TextView cartTotal) {
        String token = UserOperations.getPref(context).getString(context.getString(R.string.pref_user_token), "");
        Quantity quantity = new Quantity(newValue);
        Call<UpdateQuantityResponse> call = RetrofitInstance.getRetrofitInstance()
                .create(ApiInterface.class).updateQuantity(token, productId, quantity);
        call.enqueue(new Callback<UpdateQuantityResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<UpdateQuantityResponse> call, @NotNull Response<UpdateQuantityResponse> response) {
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        if (TAG.equals(CartAdapter.TAG)) {
                            total = response.body().getData().getTotal();
                            cartTotal.setText("Total: " + (int) Math.ceil(total) + " EGP");
                        }
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        tvQuantity.setText(String.valueOf(oldValue));
                        Toast.makeText(context, "Sorry, " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Sorry, connection error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<UpdateQuantityResponse> call, @NotNull Throwable t) {
                tvQuantity.setText(String.valueOf(oldValue));
                Toast.makeText(context, "Sorry, " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getCartIdAndUpdateQuantity(Context context, int newValue, int productId, View btToCart, ImageView progressBar) {

        if (UserOperations.isSignedIn(context)) {
            String token = UserOperations.getPref(context).getString(context.getString(R.string.pref_user_token), "");

            Call<GetCartResponse> call = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class).getCart(token);
            call.enqueue(new Callback<GetCartResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetCartResponse> call, @NotNull Response<GetCartResponse> response) {
                    if (response.body() != null) {
                        if (response.body().isStatus()) {
                            ArrayList<GetCartItems> cartItems = response.body().getData().getCart_items();
                            for (GetCartItems item : cartItems) {
                                if (item.getProduct().getId() == productId) {
                                    int cartId = item.getId();
                                    int oldValue = item.getQuantity();
                                    int quantity = newValue + oldValue;
                                    updateQuantity(context, quantity, cartId, btToCart, progressBar);
                                    break;
                                }
                            }

                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Sorry, connection error", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<GetCartResponse> call, @NotNull Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        } else {
            Toast.makeText(context, "you should login first", Toast.LENGTH_LONG).show();
        }
    }


    public static void addProductToFavorite(Context context, int productId, ImageView statusIcon, boolean[] is_favourite) {

        if (UserOperations.isSignedIn(context)) {

            ProductId id = new ProductId(productId);
            String token = UserOperations.getPref(context).getString(context.getString(R.string.pref_user_token), "");
            Call<PostFavoriteResponse> call = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class).addToFavorite(token, id);
            call.enqueue(new Callback<PostFavoriteResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostFavoriteResponse> call, @NonNull Response<PostFavoriteResponse> response) {

                    if (response.body() != null){
                        if (response.body().isStatus()) {
                            is_favourite[0] = !is_favourite[0];
                        } else {
                            if (is_favourite[0]) {
                                statusIcon.setImageResource(R.drawable.ic_red_heart);

                            } else {
                                statusIcon.setImageResource(R.drawable.ic_outlined_heart);
                            }
                            Toast.makeText(context, "Sorry, connection error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(context, "Sorry, connection error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure
                        (@NonNull Call<PostFavoriteResponse> call, @NonNull Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(context, "you should login first", Toast.LENGTH_LONG).show();
        }
    }
}
