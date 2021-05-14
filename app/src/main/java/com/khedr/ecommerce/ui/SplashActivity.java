package com.khedr.ecommerce.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding b;
    SharedPreferences pref;
    public static HomePageApiResponse homeResponse=new HomePageApiResponse(false,null,null);
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        b = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        UiUtils.animJumpAndFade(this, b.progressSplash);

        pref = getSharedPreferences("logined", 0);
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
        Call<HomePageApiResponse> call = RetrofitInstance.getRetrofitInstance()
                .create(ApiInterface.class).getHomePage(token);
        call.enqueue(new Callback<HomePageApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<HomePageApiResponse> call, @NotNull Response<HomePageApiResponse> response) {
                b.progressSplash.clearAnimation();
                b.progressSplash.setVisibility(View.GONE);

                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        homeResponse = response.body();
                        startActivity(new Intent(SplashActivity.this, MainPageActivity.class));
                        finish();
                    } else {
                        UiUtils.shortToast(SplashActivity.this, response.body().getMessage());
                    }
                }else {
                    UiUtils.shortToast(SplashActivity.this, "Sorry, connection error");
                }

            }

            @Override
            public void onFailure(@NotNull Call<HomePageApiResponse> call, @NotNull Throwable t) {
                UiUtils.shortToast(SplashActivity.this, "Sorry, connection error");
            }
        });
    }
}