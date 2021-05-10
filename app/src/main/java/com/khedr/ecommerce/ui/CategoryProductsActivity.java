package com.khedr.ecommerce.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityCategoryItemsBinding;
import com.khedr.ecommerce.pojo.categories.GetCategoriesInnerData;
import com.khedr.ecommerce.pojo.categories.GetCategoriesResponse;
import com.khedr.ecommerce.pojo.categories.item.GetCategoryItemsResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.operations.UiOperations;
import com.khedr.ecommerce.operations.UserOperations;
import com.khedr.ecommerce.ui.fragments.HomeFragment;

import org.jetbrains.annotations.NotNull;

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
        b= DataBindingUtil.setContentView(this, R.layout.activity_category_items);
        pref= UserOperations.getPref(this);
        productsAdapter=new ProductsAdapter(this);

        UiOperations.AnimJumpAndFade(this,b.progressItemCategory);
        UiOperations.AnimEndToStart(this,b.viewItemCategoryUnderMoto);

        b.btItemCategoryBack.setOnClickListener(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this
                ,2, RecyclerView.VERTICAL,false);
        b.rvItemCategory.setLayoutManager(gridLayoutManager);
        b.rvItemCategory.setAdapter(productsAdapter);

        Intent intent=getIntent();
        b.mainTvItemCategory.setText(intent.getStringExtra(getString(R.string.category_name)));
        if (intent.getStringExtra(getString(R.string.category_name)).equals("Prevent Corona"))
        {
            getCategoryByName("Prevent Corona");
        }
        else {
            getItemsByCategory(intent.getIntExtra(getString(R.string.category_id), 0));
        }
    }

    @Override
    public void onClick(View v) {
        if (v==b.btItemCategoryBack){
            onBackPressed();
        }
    }
    public void getItemsByCategory(int id){
        String token = pref.getString(getString(R.string.pref_user_token), "");
        Call<GetCategoryItemsResponse> call= RetrofitInstance.getRetrofitInstance().create(ApiInterface.class).getCategoryItems(token,id);
        call.enqueue(new Callback<GetCategoryItemsResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetCategoryItemsResponse> call, @NotNull Response<GetCategoryItemsResponse> response) {
                UiOperations.AnimCenterToEnd(CategoryProductsActivity.this,b.progressItemCategory);
                if (response.body() != null) {
                    if (response.body().isStatus()){
                        productsAdapter.setProductsList(response.body().getData().getData());
                    }
                    else {
                        UiOperations.shortToast(CategoryProductsActivity.this, response.body().getMessage());
                    }
                }else {
                    UiOperations.shortToast(CategoryProductsActivity.this, "Sorry, connection error");

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCategoryItemsResponse> call,@NonNull  Throwable t) {
                UiOperations.AnimCenterToEnd(CategoryProductsActivity.this,b.progressItemCategory);
                UiOperations.shortToast(CategoryProductsActivity.this, "Sorry, connection error");

            }
        });
    }
    public void getCategoryByName(String categoryName){

        Call<GetCategoriesResponse> call=RetrofitInstance.getRetrofitInstance().create(ApiInterface.class).getCategories();
        call.enqueue(new Callback<GetCategoriesResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetCategoriesResponse> call, @NotNull Response<GetCategoriesResponse> response) {
                if (response.body() != null) {
                    boolean succeeded=false;
                    if (response.body().isStatus()){
                        for (GetCategoriesInnerData data:response.body().getData().getData()){
                            if (data.getName().equals(categoryName)){
                                getItemsByCategory(data.getId());
                                succeeded=true;
                                break;
                            }
                        }
                        if (!succeeded){
                            UiOperations.shortToast(CategoryProductsActivity.this, "Sorry, no item found");
                        }

                    }
                    else {
                        UiOperations.shortToast(CategoryProductsActivity.this, response.body().getMessage());
                    }
                }else {
                    UiOperations.shortToast(CategoryProductsActivity.this, "Sorry, connection error");
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetCategoriesResponse> call, @NotNull Throwable t) {
                UiOperations.shortToast(CategoryProductsActivity.this, "Sorry, connection error");
            }
        });


    }

}