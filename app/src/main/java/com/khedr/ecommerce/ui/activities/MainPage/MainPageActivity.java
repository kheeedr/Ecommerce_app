package com.khedr.ecommerce.ui.activities.MainPage;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityMainPageBinding;
import com.khedr.ecommerce.ui.activities.cart.CartActivity;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainPageBinding b;
    SharedPreferences pref;

    private static final String TAG = "MainPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = UserUtils.getPref(this);
        UiUtils.setLocale(this);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main_page);
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

        Intent intent=getIntent();
        if (intent.getStringExtra(getString(R.string.intent_name)).equals(getString(R.string.intent_to_orders))){
            b.bottomNavigationMainPage.setSelectedItemId(R.id.order_option);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "mkhedr:onResume");

    }

    @Override
    public void onClick(View v) {
        if (v == b.fabMainPage) {
            startActivity(new Intent(MainPageActivity.this, CartActivity.class));
        }
    }
}

