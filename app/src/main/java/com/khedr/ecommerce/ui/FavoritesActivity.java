package com.khedr.ecommerce.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityFavoritesBinding;
import com.khedr.ecommerce.model.product.favorites.get.GetFavoritesResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.ui.adapters.FavoritesAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity implements View.OnClickListener {
    static ActivityFavoritesBinding b;
    FavoritesAdapter favoritesAdapter;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_favorites);
        pref = getSharedPreferences("logined", 0);
        b.btFavoritesBack.setOnClickListener(this);
        favoritesAdapter = new FavoritesAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this
                , 2, RecyclerView.VERTICAL, false);
        b.rvFavorites.setLayoutManager(gridLayoutManager);
        b.rvFavorites.setAdapter(favoritesAdapter);
        getFavorites();
    }

    @Override
    public void onClick(View v) {
        if (v == b.btFavoritesBack) {
            onBackPressed();
        }
    }

    void getFavorites() {
        if (isSignedIn()) {
            String token = pref.getString(getString(R.string.pref_user_token), "");
            Call<GetFavoritesResponse> call = RetrofitInstance.getRetrofitInstance()
                    .create(ApiInterface.class).getFavorites(token);
            call.enqueue(new Callback<GetFavoritesResponse>() {
                @Override
                public void onResponse(Call<GetFavoritesResponse> call, Response<GetFavoritesResponse> response) {
                    if (response.body().isStatus()) {
                        favoritesAdapter.setFavoritesList(response.body().getData().getData());
                    } else {
                        Toast.makeText(FavoritesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    b.progressFavorite.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<GetFavoritesResponse> call, Throwable t) {
                    Toast.makeText(FavoritesActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    b.progressFavorite.setVisibility(View.GONE);

                }
            });
        } else {
            Toast.makeText(this, "you should login first", Toast.LENGTH_LONG).show();
            b.progressFavorite.setVisibility(View.GONE);
        }
    }

    public boolean isSignedIn() {
        return pref.getBoolean(getString(R.string.pref_status), false);
    }
}

