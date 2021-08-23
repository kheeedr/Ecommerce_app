/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;
import com.khedr.ecommerce.databinding.ActivityCategoryItemsBinding;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsViewModel;
import com.khedr.ecommerce.ui.activities.search.SearchViewModel;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.ui.fragments.AddToCartBottomSheetFragment;
import com.khedr.ecommerce.utils.Anim;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import java.util.ArrayList;
import java.util.Collections;

public class CategoryProductsActivity extends AppCompatActivity implements View.OnClickListener, ProductsAdapter.OnItemClickListener {
    static final String TAG = "CategoryProductsActivity";
    ActivityCategoryItemsBinding b;
    ProductDetailsViewModel productViewModel;
    ProductsAdapter productsAdapter;
    CategoriesViewModel categoriesViewModel;
    SearchViewModel searchViewModel;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_category_items);

        categoriesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(CategoriesViewModel.class);
        searchViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(SearchViewModel.class);
        productViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ProductDetailsViewModel.class);

        productsAdapter = new ProductsAdapter(this);

        b.btCategoryProductsBack.setOnClickListener(this);

        b.rvCategoryProducts.setAdapter(productsAdapter);

        intent = getIntent();
        b.mainTvCategoryProducts.setText(intent.getStringExtra(getString(R.string.category_name)));

        if (intent.getIntExtra(getString(R.string.category_id), 0) > -1) {
            categoriesViewModel.getCategoryProducts(this,
                    intent.getIntExtra(getString(R.string.category_id), 0));

        } else if (intent.getIntExtra(getString(R.string.category_id), 0) == -1) {
            ArrayList<Product> products = Converters.fromStringToProductArrayList(
                    intent.getStringExtra(getString(R.string.products_intent)));

            productsAdapter.setProductsList(products);
        } else if (intent.getIntExtra(getString(R.string.category_id), 0) == -2) {
            searchViewModel.performSearch(this, intent.getStringExtra(getString(R.string.category_name)));
        }
        observers();
        manageProgressbar();
    }


    @Override
    public void onClick(View v) {
        if (v == b.btCategoryProductsBack) {
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
        if (UserUtils.isSignedIn(this)) {
            AddToCartBottomSheetFragment bottomSheetFragment = new AddToCartBottomSheetFragment(
                    productsAdapter,
                    position,
                    productsViewHolder.b.ivProduct.getDrawable());

            bottomSheetFragment.show(getSupportFragmentManager(), "TAG");
            getSupportFragmentManager().executePendingTransactions();
        } else {
            UiUtils.showLoginFragment(this, TAG);
        }

    }


    private void observers() {

        categoriesViewModel.getCategoryItemsResponseMLD.observe(this, getCategoryItemsResponse -> {
            if (getCategoryItemsResponse.isStatus()) {
                if (getCategoryItemsResponse.getData().getData().isEmpty()) {
                    Anim.animFadeIn(this, b.layoutCategoryProductsProductNotFound);
                } else {
                    ArrayList<Product> products = getCategoryItemsResponse.getData().getData();
                    Collections.reverse(products);
                    productsAdapter.setProductsList(products);
                    Anim.animFadeIn(this, b.rvCategoryProducts);
                }
            } else {
                UiUtils.shortToast(this, getCategoryItemsResponse.getMessage());
            }
        });
        searchViewModel.searchResponseMLD.observe(this, searchResponse -> {
            if (searchResponse.isStatus()) {
                if (searchResponse.getData().getData().isEmpty()) {
                    Anim.animFadeIn(this, b.layoutCategoryProductsProductNotFound);
                } else {
                    ArrayList<Product> products = searchResponse.getData().getData();
                    Collections.reverse(products);
                    productsAdapter.setProductsList(products);
                    Anim.animFadeIn(this, b.rvCategoryProducts);
                }
            } else {
                UiUtils.shortToast(this, searchResponse.getMessage());
            }
        });
    }

    private void manageProgressbar() {

        categoriesViewModel.isGetCategoryProductsLoading.observe(this, this::showOrHideMotoProgressbar);

        searchViewModel.isSearchLoading.observe(this, this::showOrHideMotoProgressbar);
    }

    void showOrHideMotoProgressbar(boolean isLoading) {
        Anim.motoProgressbar(
                this, isLoading,
                b.includedProgressCategoryProducts.progressMoto,
                b.includedProgressCategoryProducts.viewUnderMoto,
                b.includedProgressCategoryProducts.getRoot());

    }


}