package com.khedr.ecommerce.ui.activities.favourites;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityFavoritesBinding;
import com.khedr.ecommerce.ui.adapters.FavoritesAdapter;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

public class FavoritesActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityFavoritesBinding b;
    FavoritesAdapter favoritesAdapter;
    FavouritesViewModel favouritesViewModel;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_favorites);
        favouritesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FavouritesViewModel.class);

        b.btFavoritesBack.setOnClickListener(this);

        favoritesAdapter = new FavoritesAdapter(this);
        b.rvFavorites.setAdapter(favoritesAdapter);
        manageProgressbar();

        favouritesViewModel.getFavoriteResponseBody.observe(this, getFavoritesResponse -> {
            if (getFavoritesResponse.isStatus()) {
                favoritesAdapter.setFavoritesList(getFavoritesResponse.getData().getData());
            } else {
                UiUtils.shortToast(this, getFavoritesResponse.getMessage());
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


}

