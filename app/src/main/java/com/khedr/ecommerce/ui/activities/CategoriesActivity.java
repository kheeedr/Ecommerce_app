package com.khedr.ecommerce.ui.activities;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityCategoriesBinding;
import com.khedr.ecommerce.pojo.categories.GetCategoriesResponse;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.ui.adapters.CategoriesAdapter;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCategoriesBinding b;
    CategoriesAdapter categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_categories);
        UiUtils.animJumpAndFade(this, b.progressCategories);
        UiUtils.animEndToStart(this, b.viewCategoriesUnderMoto);
        categoriesAdapter = new CategoriesAdapter(this);
        b.rvCategories.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        b.rvCategories.setAdapter(categoriesAdapter);
        b.btCategoriesBack.setOnClickListener(this);
        getCategories();
    }

    public void getCategories() {
        String lang = UiUtils.getAppLang(this);
        Call<GetCategoriesResponse> call = RetrofitInstance.getRetrofitInstance().getCategories(this);
        call.enqueue(new Callback<GetCategoriesResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetCategoriesResponse> call, @NotNull Response<GetCategoriesResponse> response) {
                UiUtils.animCenterToEnd(CategoriesActivity.this, b.progressCategories);
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        categoriesAdapter.setCategoriesList(response.body().getData().getData());
                    } else {
                        UiUtils.shortToast(CategoriesActivity.this, response.body().getMessage());
                    }
                } else {
                    UiUtils.shortToast(CategoriesActivity.this, getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetCategoriesResponse> call, @NotNull Throwable t) {
                UiUtils.shortToast(CategoriesActivity.this, getString(R.string.connection_error));
                UiUtils.animCenterToEnd(CategoriesActivity.this, b.progressCategories);
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v == b.btCategoriesBack) {
            onBackPressed();
        }
    }


}