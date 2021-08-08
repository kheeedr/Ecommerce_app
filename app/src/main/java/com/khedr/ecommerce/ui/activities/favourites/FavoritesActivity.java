package com.khedr.ecommerce.ui.activities.favourites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityFavoritesBinding;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.ui.activities.cart.CartViewModel;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;
import com.khedr.ecommerce.ui.activities.splash.SplashActivity;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.ui.fragments.AddToCartBottomSheetFragment;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

public class FavoritesActivity extends AppCompatActivity implements View.OnClickListener,ProductsAdapter.OnItemClickListener {

    ActivityFavoritesBinding b;
    ProductsAdapter productsAdapter;
    FavouritesViewModel favouritesViewModel;
    int i = 0;
    private int adapterPosition;
    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_favorites);
        favouritesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FavouritesViewModel.class);
        cartViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CartViewModel.class);

        b.btFavoritesBack.setOnClickListener(this);

        productsAdapter = new ProductsAdapter(this);
        b.rvFavorites.setAdapter(productsAdapter);
        manageProgressbar();
        observeOnPostToCart();
        favouritesViewModel.getFavoriteResponseBody.observe(this, getFavoritesResponse -> {
            if (getFavoritesResponse != null) {
                productsAdapter.setProductsList(getFavoritesResponse);
            } else {
                UiUtils.shortToast(this, getString(R.string.connection_error));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        i++;
        if (UserUtils.isSignedIn(this)) {
            favouritesViewModel.getFavorites(this);
        } else {
            UiUtils.shortToast(this, getString(R.string.you_should_login_first));
        }

    }
    private void observeOnPostToCart() {
        cartViewModel.postToCartResponseMLD.observe(this, postCartResponse -> {
            if (postCartResponse.isStatus()) {
                productsAdapter.getProductsList().get(adapterPosition).setIn_cart(true);
                productsAdapter.notifyItemChanged(adapterPosition);
            }

            UiUtils.shortToast(this, postCartResponse.getMessage());
        });
    }
    private void manageProgressbar() {

        favouritesViewModel.isGetFavoriteLoading.observe(this, aBoolean -> {
            if (i < 2) {
                if (aBoolean) {
                    b.progressFavorites.setVisibility(View.VISIBLE);
                    UiUtils.animJumpAndFade(this, b.progressFavorites);
                    UiUtils.animEndToStart(this, b.viewCategoriesUnderMoto);
                } else {
                    UiUtils.animCenterToEnd(this, b.progressFavorites);
                }
            }
        });
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
        AddToCartBottomSheetFragment fragment =
                new AddToCartBottomSheetFragment(
                        cartViewModel,
                        productsAdapter.getProductsList().get(position),
                        productsViewHolder.b.ivProduct.getDrawable());
        fragment.show(getSupportFragmentManager(), "TAG");
        adapterPosition = position;
    }
}

