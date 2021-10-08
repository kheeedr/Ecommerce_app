/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivitySplashBinding;
import com.khedr.ecommerce.data.model.homeapi.BannersAndProductsModel;
import com.khedr.ecommerce.presentation.ui.main_page.MainPageActivity;
import com.khedr.ecommerce.utils.UiUtils;

import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySplashBinding b;
    SplashViewModel viewModel;
    public static BannersAndProductsModel homeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);

        UiUtils.setLocale(this);
        b = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        b.btTryAgainSplash.setOnClickListener(this);

        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        viewModel.getHomeContent();

        manageProgressbar();

        viewModel.responseBody.observe(this, homePageApiResponse -> {
            if (homePageApiResponse.isStatus()) {
                homeResponse = homePageApiResponse.getData();
                Collections.reverse(homeResponse.getProducts());
                startActivity(new Intent(SplashActivity.this, MainPageActivity.class));
                finish();

            } else {
                UiUtils.shortToast(SplashActivity.this, homePageApiResponse.getMessage());
                b.btTryAgainSplash.setVisibility(View.VISIBLE);
            }
        });

    }
    @Override
    public void onClick(View v) {
        if (v == b.btTryAgainSplash) {
            viewModel.getHomeContent();
        }
    }
    private void manageProgressbar() {

        viewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                b.btTryAgainSplash.setVisibility(View.GONE);
                b.progressSplash.setVisibility(View.VISIBLE);
            }
            else {
                b.progressSplash.setVisibility(View.GONE);
         }
        });

    }


}