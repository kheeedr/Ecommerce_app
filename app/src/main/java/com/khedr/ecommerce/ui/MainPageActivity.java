package com.khedr.ecommerce.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityMainPageBinding;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainPageBinding b;
    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main_page);
        activity=this;
        // bottom navigation
        b.bottomNavigationMainPage.getMenu().findItem(R.id.under_fab_option).setCheckable(false);
        b.bottomNavigationMainPage.getMenu().findItem(R.id.under_fab_option).setEnabled(false);
        b.bottomNavigationMainPage.setElevation(0);
        b.bottomNavigationMainPage.setTranslationZ(0);
        b.bottomNavigationMainPage.setTranslationY(0);
        b.bottomNavigationMainPage.setTranslationX(0);

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(b.bottomNavigationMainPage, navController);

        b.fabMainPage.setOnClickListener(this);




    }


    @Override
    public void onClick(View v) {
        if (v==b.fabMainPage){
            startActivity(new Intent(MainPageActivity.this,CartActivity.class));
        }
    }
}

