package com.khedr.ecommerce.ui.activities.favourites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityFavoritesBinding;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.ui.fragments.AddToCartBottomSheetFragment;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

public class FavoritesActivity extends AppCompatActivity implements View.OnClickListener, ProductsAdapter.OnItemClickListener {

    ActivityFavoritesBinding b;
    ProductsAdapter productsAdapter;
    FavouritesViewModel favouritesViewModel;

    boolean isFirstResume = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_favorites);
        favouritesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(FavouritesViewModel.class);

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
            favouritesViewModel.getFavorites(this);
        } else {
            UiUtils.shortToast(this, getString(R.string.you_should_login_first));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstResume = false;
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
        UiUtils.motoProgressbar(
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

