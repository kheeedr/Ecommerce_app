package com.khedr.ecommerce.ui.activites.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivitySplashBinding;
import com.khedr.ecommerce.pojo.homeapi.HomePageApiResponse;
import com.khedr.ecommerce.ui.activites.MainPageActivity;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import java.util.Collections;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySplashBinding b;
    SharedPreferences pref;
    SplashViewModel viewModel;
    public static HomePageApiResponse homeResponse = new HomePageApiResponse(false, null, null);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        pref = UserUtils.getPref(this);
        UiUtils.setLocale(this);
        b = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        b.btTryAgainSplash.setOnClickListener(this);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SplashViewModel.class);
        viewModel.getHomeContent(this);
        UiUtils.animJumpAndFade(this, b.progressSplash);
        manageProgressbar();


        viewModel.responseBody.observe(this,homePageApiResponse -> {
            if(homePageApiResponse.isStatus()){
                homeResponse = homePageApiResponse;
                Collections.reverse(homeResponse.getData().getProducts());
                startActivity(new Intent(SplashActivity.this, MainPageActivity.class));
                finish();

            }else {
                UiUtils.shortToast(SplashActivity.this, homePageApiResponse.getMessage());
                b.btTryAgainSplash.setVisibility(View.VISIBLE);
            }
        });

    }

    private void manageProgressbar() {

        viewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                b.btTryAgainSplash.setVisibility(View.GONE);
                b.progressSplash.setVisibility(View.VISIBLE);
                UiUtils.animJumpAndFade(this, b.progressSplash);
            } else {
                b.progressSplash.clearAnimation();
                b.progressSplash.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v==b.btTryAgainSplash){
            viewModel.getHomeContent(this);
        }
    }
}