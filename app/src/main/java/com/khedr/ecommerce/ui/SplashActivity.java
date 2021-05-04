package com.khedr.ecommerce.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivitySplashBinding;
import com.khedr.ecommerce.model.homeapi.HomePageApiResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.ui.operations.UiOperations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding b;
    SharedPreferences pref;
    public static HomePageApiResponse homeResponse;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        UiOperations.AnimJumpAndFade(this, b.progressSplash);

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
            public void onResponse(Call<HomePageApiResponse> call, Response<HomePageApiResponse> response) {
                if (response.body().isStatus()) {
                    homeResponse = response.body();
                    startActivity(new Intent(SplashActivity.this, MainPageActivity.class));
                    finish();
                } else {
                    Toast.makeText(SplashActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<HomePageApiResponse> call, Throwable t) {
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}