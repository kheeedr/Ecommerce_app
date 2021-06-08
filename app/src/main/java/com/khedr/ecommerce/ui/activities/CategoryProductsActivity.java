package com.khedr.ecommerce.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityCategoryItemsBinding;
import com.khedr.ecommerce.pojo.categories.GetCategoriesInnerData;
import com.khedr.ecommerce.pojo.categories.GetCategoriesResponse;
import com.khedr.ecommerce.pojo.categories.item.GetCategoryItemsResponse;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryProductsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCategoryItemsBinding b;
    SharedPreferences pref;
    ProductsAdapter productsAdapter;
    private static final String TAG = "CategoryProductsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_category_items);
        pref = UserUtils.getPref(this);
        productsAdapter = new ProductsAdapter(this);

        UiUtils.animJumpAndFade(this, b.progressCategoryProducts);
        UiUtils.animEndToStart(this, b.viewCategoryProductsUnderMoto);

        b.btCategoryProductsBack.setOnClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this
                , 2, RecyclerView.VERTICAL, false);
        b.rvCategoryProducts.setLayoutManager(gridLayoutManager);
        b.rvCategoryProducts.setAdapter(productsAdapter);

        Intent intent = getIntent();
        b.mainTvCategoryProducts.setText(intent.getStringExtra(getString(R.string.category_name)));
        if (intent.getStringExtra(getString(R.string.category_name)).equals(getString(R.string.prevent_corona))) {
            getCategoryByName(getString(R.string.prevent_corona));
        } else {
            getItemsByCategory(intent.getIntExtra(getString(R.string.category_id), 0));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == b.btCategoryProductsBack) {
            onBackPressed();
        }
    }

    public void getItemsByCategory(int id) {
        String token = pref.getString(getString(R.string.pref_user_token), "");
        String lang=UiUtils.getAppLang(this);

        Call<GetCategoryItemsResponse> call = RetrofitInstance.getRetrofitInstance().getCategoryItems(this,token, id);
        call.enqueue(new Callback<GetCategoryItemsResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetCategoryItemsResponse> call, @NotNull Response<GetCategoryItemsResponse> response) {
                UiUtils.animCenterToEnd(CategoryProductsActivity.this, b.progressCategoryProducts);
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        if (!response.body().getData().getData().isEmpty()) {
                            ArrayList<Product> products=response.body().getData().getData();
                            Collections.reverse(products);
                            productsAdapter.setProductsList(products);
                        }else {
                            b.layoutCategoryProductsProductNotFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        UiUtils.shortToast(CategoryProductsActivity.this, response.body().getMessage());
                    }
                } else {
                    UiUtils.shortToast(CategoryProductsActivity.this, getString(R.string.connection_error));

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCategoryItemsResponse> call, @NonNull Throwable t) {
                UiUtils.animCenterToEnd(CategoryProductsActivity.this, b.progressCategoryProducts);
                UiUtils.shortToast(CategoryProductsActivity.this, getString(R.string.connection_error));

            }
        });
    }

    public void getCategoryByName(String categoryName) {


        Call<GetCategoriesResponse> call = RetrofitInstance.getRetrofitInstance().getCategories(this);
        call.enqueue(new Callback<GetCategoriesResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetCategoriesResponse> call, @NotNull Response<GetCategoriesResponse> response) {
                if (response.body() != null) {
                    boolean succeeded = false;
                    if (response.body().isStatus()) {
                        for (GetCategoriesInnerData data : response.body().getData().getData()) {
                            if (data.getName().equals(categoryName)) {
                                getItemsByCategory(data.getId());
                                succeeded = true;
                                break;
                            }
                        }
                        if (!succeeded) {
                            UiUtils.shortToast(CategoryProductsActivity.this, getString(R.string.connection_error));
                        }

                    } else {
                        UiUtils.shortToast(CategoryProductsActivity.this, response.body().getMessage());
                    }
                } else {
                    UiUtils.shortToast(CategoryProductsActivity.this, getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetCategoriesResponse> call, @NotNull Throwable t) {
                UiUtils.shortToast(CategoryProductsActivity.this, getString(R.string.connection_error));
            }
        });


    }

}