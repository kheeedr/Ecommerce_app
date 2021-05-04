package com.khedr.ecommerce.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityCategoryItemsBinding;
import com.khedr.ecommerce.model.categories.item.GetCategoryItemsResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.ui.operations.UiOperations;
import com.khedr.ecommerce.ui.operations.UserOperations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryProductsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCategoryItemsBinding b;
    SharedPreferences pref;
    ProductsAdapter productsAdapter;
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
        getItemsByCategory(intent.getIntExtra(getString(R.string.category_id),0));
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
            public void onResponse(@NonNull Call<GetCategoryItemsResponse> call, Response<GetCategoryItemsResponse> response) {
                UiOperations.AnimCenterToEnd(CategoryProductsActivity.this,b.progressItemCategory);
                if (response.body().isStatus()){
                    productsAdapter.setProductsList(response.body().getData().getData());
                }
                else {
                    Toast.makeText(CategoryProductsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategoryItemsResponse> call, Throwable t) {
                UiOperations.AnimCenterToEnd(CategoryProductsActivity.this,b.progressItemCategory);
                Toast.makeText(CategoryProductsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}