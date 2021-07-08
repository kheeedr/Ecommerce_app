package com.khedr.ecommerce.ui.activities.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivitySearchBinding;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.ui.activities.cart.CartViewModel;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;
import com.khedr.ecommerce.ui.activities.splash.SplashActivity;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.ui.adapters.SearchSuggestionsAdapter;
import com.khedr.ecommerce.utils.MyTextWatcher;
import com.khedr.ecommerce.utils.UiUtils;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener, ProductsAdapter.OnItemClickListener {
    ActivitySearchBinding b;
    SearchSuggestionsAdapter suggestionsAdapter;
    ProductsAdapter productsAdapter;
    CartViewModel cartViewModel;
    SearchViewModel searchViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_search);
        b.btSearchCancel.setOnClickListener(this);
        suggestionsAdapter = new SearchSuggestionsAdapter(this);
        cartViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(CartViewModel.class);
        searchViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(SearchViewModel.class);

        productsAdapter = new ProductsAdapter(this);
        b.rvSearchSuggestion.setAdapter(suggestionsAdapter);
        b.rvSearchProducts.setAdapter(productsAdapter);
        b.rvSearchProducts.setVisibility(View.GONE);

        manageSearchKeyboardButton();

        observeOnSearchResponse();

        manageProgressbar();

        setSuggestionsListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == b.btSearchCancel) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onParentClicked(int position) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product", productsAdapter.getProductsList().get(position));
        startActivity(intent);
    }

    @Override
    public void onItemAddToCartClicked(int position, ProductsAdapter.ProductsViewHolder productsViewHolder) {

    }

    private void observeOnSearchResponse() {
        searchViewModel.searchResponseMLD.observe(this, searchResponse -> {
            if (searchResponse.isStatus()) {
                if (searchResponse.getData().getData().isEmpty()) {
                    b.rvSearchProducts.setVisibility(View.GONE);
                    b.layoutSearchProductNotFound.setVisibility(View.VISIBLE);
                } else {
                    productsAdapter.setProductsList(searchResponse.getData().getData());
                    b.rvSearchProducts.setVisibility(View.VISIBLE);
                    b.layoutSearchProductNotFound.setVisibility(View.GONE);
                }
            } else {
                UiUtils.shortToast(this, searchResponse.getMessage());
            }
        });
    }
    private void setSuggestionsListener() {
        b.svSearchEt.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.rvSearchSuggestion.setVisibility(View.VISIBLE);
                b.rvSearchProducts.setVisibility(View.GONE);
                b.layoutSearchProductNotFound.setVisibility(View.GONE);
                if (!s.toString().isEmpty()) {
                    suggestionsAdapter.setSuggestionsList(getSuggestions(s.toString()));
                } else {
                    ArrayList<Product> emptyList = new ArrayList<>();
                    suggestionsAdapter.setSuggestionsList(emptyList);
                }
            }
        });
    }

    private void manageProgressbar() {
        searchViewModel.isSearchLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                b.progressSearch.setVisibility(View.VISIBLE);
                b.rvSearchSuggestion.setVisibility(View.GONE);
                b.rvSearchProducts.setVisibility(View.GONE);
                UiUtils.motoJumpAndFade(this, b.progressSearch, b.vProgressSearch);

            } else {
                UiUtils.animCenterToEnd(this, b.progressSearch);
            }
        });
    }


    private void manageSearchKeyboardButton() {
        b.svSearchEt.requestFocus();
        b.svSearchEt.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                searchViewModel.performSearch(this, v.getText().toString());
                return true;
            }
            return false;
        });
    }

    public ArrayList<Product> getSuggestions(String text) {
        ArrayList<Product> products = SplashActivity.homeResponse.getProducts();
        ArrayList<Product> suggestionsProducts = new ArrayList<>();
        for (Product product : products) {
            String name = product.getName().toLowerCase();
            String description = product.getDescription().toLowerCase();
            if (name.contains(text.toLowerCase()) || description.contains(text.toLowerCase())) {
                suggestionsProducts.add(product);
            }
        }
        return suggestionsProducts;
    }

}