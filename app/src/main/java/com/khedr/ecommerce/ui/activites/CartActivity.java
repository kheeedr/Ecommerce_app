package com.khedr.ecommerce.ui.activites;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityCartBinding;
import com.khedr.ecommerce.pojo.product.cart.get.GetCartItems;
import com.khedr.ecommerce.pojo.product.cart.get.GetCartResponse;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.ui.adapters.CartAdapter;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="CartActivity" ;
    public ActivityCartBinding b;
    CartAdapter cartAdapter;
    SharedPreferences pref;
    public static double total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        pref = UserUtils.getPref(this);
        UiUtils.animJumpAndFade(this, b.progressCart);
        UiUtils.animEndToStart(this, b.viewCartUnderMoto);
        b.btCartShopNow.setOnClickListener(this);
        b.btCartBack.setOnClickListener(this);
        b.layoutCartProgressBack.setOnClickListener(this);
        cartAdapter = new CartAdapter(this, b);
        b.rvCart.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        b.rvCart.setAdapter(cartAdapter);
        getCartProducts();

        Log.d(TAG, "mkhedr: onCreate");


    }

    @Override
    public void onClick(View v) {
        if (v == b.btCartBack) {
            onBackPressed();
        } else if (v == b.btCartShopNow) {
            Intent intent = new Intent(CartActivity.this, MainPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finishAffinity();
            startActivity(intent);
            finish();
        } else if (v == b.layoutCartProgressBack) {
            Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show();
        }
    }


    public void getCartProducts() {

        if (UserUtils.isSignedIn(this)) {
            String token = pref.getString(getString(R.string.pref_user_token), "");

            Call<GetCartResponse> call = RetrofitInstance.getRetrofitInstance().getCart(this,token);
            call.enqueue(new Callback<GetCartResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<GetCartResponse> call,@NotNull Response<GetCartResponse> response) {
                    UiUtils.animCenterToEnd(CartActivity.this, b.progressCart);
                    if (response.body() != null) {
                        if (response.body().isStatus()) {
                            ArrayList<GetCartItems> cartItems = response.body().getData().getCart_items();
                            total = response.body().getData().getTotal();
                            b.tvCartTotal.setText(getString(R.string.total_price) + (int) Math.ceil(total) + " EGP");
                            if (!cartItems.isEmpty()) {
                                cartAdapter.setCartItems(cartItems);
                                b.layoutCartFilled.setVisibility(View.VISIBLE);
                                b.layoutCartEmpty.setVisibility(View.GONE);
                            } else {
                                b.layoutCartFilled.setVisibility(View.GONE);
                                b.layoutCartEmpty.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(CartActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(CartActivity.this, "connection error...", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(@NotNull Call<GetCartResponse> call,@NotNull Throwable t) {
                    Toast.makeText(CartActivity.this, "connection error...", Toast.LENGTH_LONG).show();
                    UiUtils.animCenterToEnd(CartActivity.this, b.progressCart);
                }
            });
        } else {
            Toast.makeText(this, "you should login first", Toast.LENGTH_LONG).show();
            UiUtils.animCenterToEnd(CartActivity.this, b.progressCart);
        }
    }

}