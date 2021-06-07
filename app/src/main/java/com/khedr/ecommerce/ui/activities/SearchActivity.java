package com.khedr.ecommerce.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivitySearchBinding;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.pojo.product.search.SearchResponse;
import com.khedr.ecommerce.ui.activities.splash.SplashActivity;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.ui.adapters.SearchSuggestionsAdapter;
import com.khedr.ecommerce.utils.ProductUtils;
import com.khedr.ecommerce.utils.UiUtils;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySearchBinding b;
    SearchSuggestionsAdapter suggestionsAdapter;
    ProductsAdapter productsAdapter;
    MutableLiveData<boolean[]> isSearchSucceeded = new MutableLiveData<>();
    SearchResponse[] body = {null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_search);
        b.btSearchCancel.setOnClickListener(this);
        suggestionsAdapter = new SearchSuggestionsAdapter(this);
        productsAdapter = new ProductsAdapter(this);
        b.rvSearchSuggestion.setAdapter(suggestionsAdapter);
        b.rvSearchProducts.setAdapter(productsAdapter);
        b.rvSearchProducts.setVisibility(View.GONE);
        b.svSearchEt.requestFocus();
        b.svSearchEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                b.progressSearch.setVisibility(View.VISIBLE);
                UiUtils.motoJumpAndFade(this,b.progressSearch,b.vProgressSearch);
                b.rvSearchSuggestion.setVisibility(View.GONE);
                b.rvSearchProducts.setVisibility(View.GONE);
                ProductUtils.performSearch(this, v.getText().toString(), isSearchSucceeded, body);
                b.parentSearch.setId(View.generateViewId());

                return true;
            }
            return false;
        });

        isSearchSucceeded.observe(this, booleans -> {
            UiUtils.animCenterToEnd(this,b.progressSearch);
            if (booleans[0]) {
                if (!body[0].getData().getData().isEmpty()) {
                    productsAdapter.setProductsList(body[0].getData().getData());
                    b.rvSearchProducts.setVisibility(View.VISIBLE);
                    b.layoutSearchProductNotFound.setVisibility(View.GONE);
                }else {
                    b.rvSearchProducts.setVisibility(View.GONE);
                    b.layoutSearchProductNotFound.setVisibility(View.VISIBLE);
                }
            } else {
                UiUtils.shortToast(this,  getString(R.string.connection_error));
            }
        });

        b.svSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        if (v == b.btSearchCancel) {
            onBackPressed();
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

}