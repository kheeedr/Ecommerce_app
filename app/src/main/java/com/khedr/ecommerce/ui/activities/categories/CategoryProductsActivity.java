/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;
import com.khedr.ecommerce.databinding.ActivityCategoryItemsBinding;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.ui.activities.cart.CartViewModel;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;
import com.khedr.ecommerce.ui.activities.search.SearchViewModel;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.ui.fragments.AddToCartBottomSheetFragment;
import com.khedr.ecommerce.utils.UiUtils;

import java.util.ArrayList;
import java.util.Collections;

public class CategoryProductsActivity extends AppCompatActivity implements View.OnClickListener, ProductsAdapter.OnItemClickListener {
    ActivityCategoryItemsBinding b;

    ProductsAdapter productsAdapter;
    CartViewModel cartViewModel;
    CategoriesViewModel categoriesViewModel;
    SearchViewModel searchViewModel;
    int adapterPosition;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_category_items);

        cartViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(CartViewModel.class);
        categoriesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(CategoriesViewModel.class);
        searchViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(SearchViewModel.class);

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

        observeOnGetCategoryProducts();

        observeOnGetSearchProducts();

        observeOnPostToCart();

        observeOnUpdateQuantity();

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

        AddToCartBottomSheetFragment fragment =
                new AddToCartBottomSheetFragment(
                        cartViewModel,
                        productsAdapter.getProductsList().get(position),
                        productsViewHolder.b.ivProduct.getDrawable());
        fragment.show(getSupportFragmentManager(), "TAG");
        adapterPosition = position;
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

    private void observeOnUpdateQuantity() {
        cartViewModel.updateQuantityResponseMLD.observe(this, updateQuantityResponse -> {
            if (updateQuantityResponse.isStatus()) {
                UiUtils.shortToast(this, getString(R.string.quantity_updated));
            } else {
                UiUtils.shortToast(this, updateQuantityResponse.getMessage());
            }
        });
    }

    private void manageProgressbar() {

        categoriesViewModel.isGetCategoryProductsLoading.observe(this, this::showOrHideMotoProgressbar);

        cartViewModel.isPostLoading.observe(this, this::showOrHideMotoProgressbar);

        cartViewModel.isUpdateFromProductLoading.observe(this, this::showOrHideMotoProgressbar);

        cartViewModel.isPostMultipleLoading.observe(this, this::showOrHideMotoProgressbar);

        searchViewModel.isSearchLoading.observe(this, this::showOrHideMotoProgressbar);
    }

    void showOrHideMotoProgressbar(boolean isLoading) {
        UiUtils.motoProgressbar(
                this, isLoading,
                b.includedProgressCategoryProducts.progressMoto,
                b.includedProgressCategoryProducts.viewUnderMoto,
                b.includedProgressCategoryProducts.getRoot());

    }

    private void observeOnGetCategoryProducts() {
        categoriesViewModel.getCategoryItemsResponseMLD.observe(this, getCategoryItemsResponse -> {
            if (getCategoryItemsResponse.isStatus()) {
                if (getCategoryItemsResponse.getData().getData().isEmpty()) {
                    UiUtils.animFadeIn(this, b.layoutCategoryProductsProductNotFound);
                } else {
                    ArrayList<Product> products = getCategoryItemsResponse.getData().getData();
                    Collections.reverse(products);
                    productsAdapter.setProductsList(products);
                    UiUtils.animFadeIn(this, b.rvCategoryProducts);
                }
            } else {
                UiUtils.shortToast(this, getCategoryItemsResponse.getMessage());
            }
        });
    }

    private void observeOnGetSearchProducts() {
        searchViewModel.searchResponseMLD.observe(this, searchResponse -> {
            if (searchResponse.isStatus()) {
                if (searchResponse.getData().getData().isEmpty()) {
                    UiUtils.animFadeIn(this, b.layoutCategoryProductsProductNotFound);
                } else {
                    ArrayList<Product> products = searchResponse.getData().getData();
                    Collections.reverse(products);
                    productsAdapter.setProductsList(products);
                    UiUtils.animFadeIn(this, b.rvCategoryProducts);
                }
            } else {
                UiUtils.shortToast(this, searchResponse.getMessage());
            }
        });
    }

}