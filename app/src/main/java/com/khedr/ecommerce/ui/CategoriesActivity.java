package com.khedr.ecommerce.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityCategoriesBinding;
import com.khedr.ecommerce.model.categories.GetCategoriesResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.ui.adapters.CategoriesAdapter;
import com.khedr.ecommerce.ui.operations.UiOperations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCategoriesBinding b;
    CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_categories);
        UiOperations.AnimJumpAndFade(this,b.progressCategories);
        UiOperations.AnimEndToStart(this,b.viewCategoriesUnderMoto);
        b.rvCategories.setLayoutManager(new GridLayoutManager(this,2, RecyclerView.VERTICAL,false));
        b.rvCategories.setAdapter(categoriesAdapter);
        b.btCategoriesBack.setOnClickListener(this);
        getCategories();
    }

    public void getCategories(){

        Call<GetCategoriesResponse> call=RetrofitInstance.getRetrofitInstance().create(ApiInterface.class).getCategories();
        call.enqueue(new Callback<GetCategoriesResponse>() {
            @Override
            public void onResponse(Call<GetCategoriesResponse> call, Response<GetCategoriesResponse> response) {
                UiOperations.AnimCenterToEnd(CategoriesActivity.this,b.progressCategories);
                if (response.body().isStatus()){
                    categoriesAdapter.setCategoriesList(response.body().getData().getData());
                }
                else {
                    Toast.makeText(CategoriesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategoriesResponse> call, Throwable t) {
                Toast.makeText(CategoriesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                UiOperations.AnimCenterToEnd(CategoriesActivity.this,b.progressCategories);
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v==b.btCategoriesBack){
            onBackPressed();
        }
    }
}