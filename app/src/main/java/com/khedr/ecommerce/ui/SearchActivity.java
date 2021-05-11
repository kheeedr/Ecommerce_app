package com.khedr.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivitySearchBinding;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.pojo.product.search.SearchResponse;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.ui.adapters.SearchSuggestionsAdapter;
import com.khedr.ecommerce.utils.ProductOperations;
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
                b.rvSearchSuggestion.setVisibility(View.GONE);
                b.rvSearchProducts.setVisibility(View.VISIBLE);
                ProductOperations.performSearch(this, v.getText().toString(), isSearchSucceeded, body);
                return true;
            }
            return false;
        });
        isSearchSucceeded.observe(this, booleans -> {
            if (booleans[0]) {
                productsAdapter.setProductsList(body[0].getData().getData());
            } else {
                UiUtils.shortToast(this, "Connection error");
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
        ArrayList<Product> products = SplashActivity.homeResponse.getData().getProducts();
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