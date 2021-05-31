package com.khedr.ecommerce.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivitySplashBinding;
import com.khedr.ecommerce.pojo.homeapi.HomePageApiResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding b;
    SharedPreferences pref;

    public static HomePageApiResponse homeResponse = new HomePageApiResponse(false, null, null);
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        pref = UserUtils.getPref(this);
        UiUtils.setLocale(this);
        b = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        UiUtils.animJumpAndFade(this, b.progressSplash);

        Log.d(TAG, "mkhedr: onCreate");
    }


    @Override
    protected void onStart() {
        super.onStart();
        getHomeContent();
        Log.d(TAG, "mkhedr: onStart");
    }

    void getHomeContent() {

        String token = pref.getString(getString(R.string.pref_user_token), "");
        String lang=UiUtils.getAppLang(this);

        Call<HomePageApiResponse> call = RetrofitInstance.getRetrofitInstance()
                .getHomePage(this,token);
        call.enqueue(new Callback<HomePageApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<HomePageApiResponse> call, @NotNull Response<HomePageApiResponse> response) {
                b.progressSplash.clearAnimation();
                b.progressSplash.setVisibility(View.GONE);

                if (response.body() != null) {
                    if (response.body().isStatus()) {
//                        ArrayList<Product> products=response.body().getData().getProducts();
//                        Collections.reverse(products);


                        homeResponse = response.body();
                        Collections.reverse(homeResponse.getData().getProducts());

                        startActivity(new Intent(SplashActivity.this, MainPageActivity.class));
                        finish();

                    } else {
                        UiUtils.shortToast(SplashActivity.this, response.body().getMessage());
                    }
                } else {
                    UiUtils.shortToast(SplashActivity.this, getString(R.string.connection_error));
                }

            }

            @Override
            public void onFailure(@NotNull Call<HomePageApiResponse> call, @NotNull Throwable t) {
                UiUtils.shortToast(SplashActivity.this, getString(R.string.connection_error));
            }
        });
    }
}