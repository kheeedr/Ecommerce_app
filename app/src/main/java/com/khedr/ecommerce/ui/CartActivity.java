package com.khedr.ecommerce.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityCartBinding;
import com.khedr.ecommerce.model.product.cart.get.GetCartItems;
import com.khedr.ecommerce.model.product.cart.get.GetCartResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.ui.adapters.CartAdapter;
import com.khedr.ecommerce.ui.operations.UiOperations;
import com.khedr.ecommerce.ui.operations.UserOperations;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    public ActivityCartBinding b;
    CartAdapter cartAdapter;
    SharedPreferences pref;
    public static double total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        pref= UserOperations.getPref(this);
        UiOperations.AnimJumpAndFade(this,b.progressCart);
        UiOperations.AnimEndToStart(this,b.viewCartUnderMoto);
        b.btCartShopNow.setOnClickListener(this);
        b.btCartBack.setOnClickListener(this);
        b.layoutCartProgressBack.setOnClickListener(this);
        cartAdapter = new CartAdapter(this,b);
        b.rvCart.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        b.rvCart.setAdapter(cartAdapter);
        getCartProducts();


    }

    @Override
    public void onClick(View v) {
        if (v == b.btCartBack||v == b.btCartShopNow) {
         onBackPressed();
        }
        else if (v==b.layoutCartProgressBack){
            Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(CartActivity.this, MainPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finishAffinity();
        startActivity(intent);
        finish();
    }

    public void getCartProducts() {

        if (UserOperations.isSignedIn(this)) {
            String token = pref.getString(getString(R.string.pref_user_token), "");

            Call<GetCartResponse> call = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class).getCart(token);
            call.enqueue(new Callback<GetCartResponse>() {
                @Override
                public void onResponse(Call<GetCartResponse> call, Response<GetCartResponse> response) {
                    UiOperations.AnimCenterToEnd(CartActivity.this,b.progressCart);
                    if (response.body().isStatus()) {
                        ArrayList<GetCartItems> cartItems = response.body().getData().getCart_items();
                        total=response.body().getData().getTotal();
                        b.tvCartTotal.setText("Total: " +String.valueOf((int)Math.ceil(total)) + " EGP");
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

                @Override
                public void onFailure(Call<GetCartResponse> call, Throwable t) {
                    Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    UiOperations.AnimCenterToEnd(CartActivity.this,b.progressCart);

                    }
            });
        } else {
            Toast.makeText(this, "you should login first", Toast.LENGTH_LONG).show();
            UiOperations.AnimCenterToEnd(CartActivity.this,b.progressCart);;
        }
    }

}