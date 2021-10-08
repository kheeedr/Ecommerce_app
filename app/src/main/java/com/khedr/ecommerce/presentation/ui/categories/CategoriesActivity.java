/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.categories;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityCategoriesBinding;
import com.khedr.ecommerce.presentation.adapters.CategoriesAdapter;
import com.khedr.ecommerce.utils.Animations;
import com.khedr.ecommerce.utils.UiUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener, CategoriesAdapter.OnItemClickListener {
    ActivityCategoriesBinding b;
    CategoriesAdapter categoriesAdapter;
    CategoriesViewModel categoriesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_categories);
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        categoriesAdapter = new CategoriesAdapter(this);
        b.rvCategories.setAdapter(categoriesAdapter);
        b.btCategoriesBack.setOnClickListener(this);
        categoriesViewModel.getCategories();

        observeOnGetCategoriesResponse();
        mangeProgressbar();
    }

    @Override
    public void onClick(View v) {
        if (v == b.btCategoriesBack) {
            onBackPressed();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, CategoryProductsActivity.class);
        intent.putExtra(getString(R.string.category_name), categoriesAdapter.getList().get(position).getName());
        intent.putExtra(getString(R.string.category_id), categoriesAdapter.getList().get(position).getId());
        startActivity(intent);
    }

    private void observeOnGetCategoriesResponse() {
        categoriesViewModel.getCategoriesResponseMLD.observe(this, getCategoriesResponse -> {
            if (getCategoriesResponse.isStatus()) {
                categoriesAdapter.setCategoriesList(getCategoriesResponse.getData().getData());
                Animations.animFadeIn(this, b.rvCategories);
            } else {
                UiUtils.shortToast(this, getCategoriesResponse.getMessage());
            }
        });
    }

    private void mangeProgressbar() {
        categoriesViewModel.isGetCategoriesLoading.observe(this, this::showOrHideProgressMoto);
    }

    void showOrHideProgressMoto(boolean isLoading) {
        Animations.motoProgressbar(
                this, isLoading,
                b.includedProgressCategories.progressMoto,
                b.includedProgressCategories.viewUnderMoto,
                b.includedProgressCategories.getRoot());
    }

}