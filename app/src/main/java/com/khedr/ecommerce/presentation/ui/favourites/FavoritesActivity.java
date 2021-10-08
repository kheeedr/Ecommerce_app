/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.favourites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityFavoritesBinding;
import com.khedr.ecommerce.presentation.adapters.ProductsAdapter;
import com.khedr.ecommerce.presentation.ui.AddToCartBottomSheetFragment;
import com.khedr.ecommerce.presentation.ui.product.ProductDetailsActivity;
import com.khedr.ecommerce.utils.Animations;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavoritesActivity extends AppCompatActivity implements View.OnClickListener, ProductsAdapter.OnItemClickListener {

    public static final String TAG = "FavoritesActivity";

    ActivityFavoritesBinding b;
    ProductsAdapter productsAdapter;
    FavouritesViewModel favouritesViewModel;

    boolean isFirstResume = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_favorites);
        favouritesViewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        b.btFavoritesBack.setOnClickListener(this);

        productsAdapter = new ProductsAdapter(this);
        b.rvFavorites.setAdapter(productsAdapter);
        manageProgressbar();
        observers();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserUtils.isSignedIn(this)) {
            favouritesViewModel.getFavorites();
        } else {
            UiUtils.showLoginFragment(this, TAG);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (UserUtils.isSignedIn(this)) isFirstResume = false;
    }

    private void observers() {
        favouritesViewModel.getFavoriteResponseBody.observe(this, getFavoritesResponse -> {
            if (getFavoritesResponse != null) {
                productsAdapter.setProductsList(getFavoritesResponse);
            } else {
                UiUtils.shortToast(this, getString(R.string.connection_error));
            }
        });
    }

    private void manageProgressbar() {

        favouritesViewModel.isGetFavoriteLoading.observe(this, isLoading -> {
            if (isFirstResume) {
                showOrHideProgressMoto(isLoading);
            }
        });
    }

    void showOrHideProgressMoto(boolean isLoading) {
        Animations.motoProgressbar(
                this, isLoading,
                b.includedProgressFavorites.progressMoto,
                b.includedProgressFavorites.viewUnderMoto,
                b.includedProgressFavorites.getRoot());
    }

    @Override
    public void onClick(View v) {
        if (v == b.btFavoritesBack) {
            onBackPressed();
        }
    }


    @Override
    public void onParentClicked(int position) {

        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product", productsAdapter.getProductsList().get(position));
        Log.d("medo fav",""+productsAdapter.getProductsList().get(position).isIn_cart());
        startActivity(intent);
    }

    @Override
    public void onItemAddToCartClicked(int position, ProductsAdapter.ProductsViewHolder productsViewHolder) {

        AddToCartBottomSheetFragment bottomSheetFragment = new AddToCartBottomSheetFragment(
                productsAdapter,
                position,
                productsViewHolder.b.ivProduct.getDrawable());

        bottomSheetFragment.show(getSupportFragmentManager(), "TAG");
        getSupportFragmentManager().executePendingTransactions();
    }
}

