package com.khedr.ecommerce.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.pojo.product.ProductId;
import com.khedr.ecommerce.pojo.product.cart.get.GetCartItems;
import com.khedr.ecommerce.pojo.product.cart.get.GetCartResponse;
import com.khedr.ecommerce.pojo.product.cart.post.PostCartResponse;
import com.khedr.ecommerce.pojo.product.cart.update.Quantity;
import com.khedr.ecommerce.pojo.product.cart.update.UpdateQuantityResponse;
import com.khedr.ecommerce.network.RetrofitInstance;

import com.khedr.ecommerce.pojo.product.search.SearchRequest;
import com.khedr.ecommerce.pojo.product.search.SearchResponse;
import com.khedr.ecommerce.ui.adapters.CartAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.khedr.ecommerce.ui.activities.cart.CartActivity.total;

public abstract class ProductUtils {

    public static void addProductToCart(Context context, int productId, View btToCart, ImageView progressBar) {
        if (UserUtils.isSignedIn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            UiUtils.animJumpAndFade(context, progressBar);
            btToCart.setVisibility(View.GONE);
            ProductId id = new ProductId(productId);
            String token = UserUtils.getPref(context).getString(context.getString(R.string.pref_user_token), "");
            String lang=UiUtils.getAppLang(context);

            Call<PostCartResponse> call = RetrofitInstance.getRetrofitInstance().addToCart(context,token, id);
            call.enqueue(new Callback<PostCartResponse>() {
                @Override
                public void onResponse(@NotNull Call<PostCartResponse> call, @NotNull Response<PostCartResponse> response) {
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    btToCart.setVisibility(View.VISIBLE);

                    if (response.body() != null) {
                        UiUtils.shortToast(context, response.body().getMessage());
                    } else {
                        UiUtils.shortToast(context,  context.getString(R.string.connection_error));
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PostCartResponse> call, @NotNull Throwable t) {
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    btToCart.setVisibility(View.VISIBLE);
                    UiUtils.shortToast(context, context.getString(R.string.connection_error));
                }
            });
        } else {
            UiUtils.shortToast(context, context.getString(R.string.you_should_login_first));
            progressBar.setVisibility(View.GONE);
            btToCart.setVisibility(View.VISIBLE);
        }
    }

    // from product
    public static void updateQuantity(Context context, int newValue, int productId, View btToCart, ImageView progressBar) {
        btToCart.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        UiUtils.animJumpAndFade(context, progressBar);

        String token = UserUtils.getPref(context).getString(context.getString(R.string.pref_user_token), "");
        Quantity quantity = new Quantity(newValue);
        String lang=UiUtils.getAppLang(context);

        Call<UpdateQuantityResponse> call = RetrofitInstance.getRetrofitInstance()
                .updateQuantity(context,token, productId, quantity);
        call.enqueue(new Callback<UpdateQuantityResponse>() {
            @Override
            public void onResponse(@NotNull Call<UpdateQuantityResponse> call, @NotNull Response<UpdateQuantityResponse> response) {
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        UiUtils.shortToast(context, response.body().getMessage());

                    } else {
                        UiUtils.shortToast(context, context.getString(R.string.toast_sorry)  + response.body().getMessage());
                    }
                } else {
                    UiUtils.shortToast(context, context.getString(R.string.connection_error));
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
                UiUtils.shortToast(context, t.getMessage());
            }
        });

    }

    //from cart
    public static void onClickUpdateQuantity(Context context, int newValue, int oldValue, int productId, TextView tvQuantity, String TAG, TextView cartTotal) {
        String token = UserUtils.getPref(context).getString(context.getString(R.string.pref_user_token), "");
        Quantity quantity = new Quantity(newValue);
        String lang=UiUtils.getAppLang(context);

        Call<UpdateQuantityResponse> call = RetrofitInstance.getRetrofitInstance()
                .updateQuantity(context,token, productId, quantity);
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
                        UiUtils.shortToast(context, response.body().getMessage());

                    } else {
                        tvQuantity.setText(String.valueOf(oldValue));
                        UiUtils.shortToast(context, context.getString(R.string.toast_sorry)  + response.body().getMessage());
                    }
                } else {
                    UiUtils.shortToast(context, context.getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(@NotNull Call<UpdateQuantityResponse> call, @NotNull Throwable t) {
                tvQuantity.setText(String.valueOf(oldValue));
                UiUtils.shortToast(context, t.getMessage());
            }
        });
    }

    public static void getCartIdAndUpdateQuantity(Context context, int newValue, int productId, View btToCart, ImageView progressBar) {

        if (UserUtils.isSignedIn(context)) {
            String token = UserUtils.getUserToken(context);
            String lang=UiUtils.getAppLang(context);

            Call<GetCartResponse> call = RetrofitInstance.getRetrofitInstance().getCart(context,token);
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
                            UiUtils.shortToast(context, context.getString(R.string.toast_sorry) + response.body().getMessage());
                        }
                    } else {
                        UiUtils.shortToast(context, context.getString(R.string.connection_error));
                    }

                }

                @Override
                public void onFailure(@NotNull Call<GetCartResponse> call, @NotNull Throwable t) {
                    UiUtils.shortToast(context, t.getMessage());
                }
            });
        } else {
            UiUtils.shortToast(context, context.getString(R.string.you_should_login_first));
        }
    }



    public static void performSearch(Context context, String searchText, MutableLiveData<boolean[]> isSucceeded, SearchResponse[] body) {
        boolean[] succeeded = {false};
        String token = UserUtils.getUserToken(context);
        String lang=UiUtils.getAppLang(context);
        SearchRequest searchRequest = new SearchRequest(searchText);
        Call<SearchResponse> call = RetrofitInstance.getRetrofitInstance()
                .getSearchProducts(context,token, searchRequest);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NotNull Call<SearchResponse> call, @NotNull Response<SearchResponse> response) {
                if (response.body() != null) {
                    succeeded[0] = response.body().isStatus();
                    body[0] = response.body();

                } else {
                    succeeded[0] = false;
                    body[0] = null;
                }
                isSucceeded.setValue(succeeded);
            }

            @Override
            public void onFailure(@NotNull Call<SearchResponse> call, @NotNull Throwable t) {
                succeeded[0] = false;
                body[0] = null;
                isSucceeded.setValue(succeeded);
            }
        });

    }
}
