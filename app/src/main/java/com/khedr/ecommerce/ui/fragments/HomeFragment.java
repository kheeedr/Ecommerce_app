package com.khedr.ecommerce.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.FragmentHomeBinding;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.operations.UiOperations;
import com.khedr.ecommerce.operations.UserOperations;
import com.khedr.ecommerce.pojo.homeapi.HomePageApiResponse;
import com.khedr.ecommerce.ui.CategoriesActivity;
import com.khedr.ecommerce.ui.FavoritesActivity;
import com.khedr.ecommerce.ui.SplashActivity;
import com.khedr.ecommerce.ui.adapters.BannersAdapter;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    FragmentHomeBinding b;
    BannersAdapter bannersAdapter;
    ProductsAdapter productsAdapter;
    SharedPreferences pref;
    //private static final String TAG = "BannersAdapter";

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        pref = UserOperations.getPref(requireContext());

        //banners rv
        bannersAdapter = new BannersAdapter(getContext());
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        linearLayoutManager.setStackFromEnd(true);
        b.rvHomeBanners.setLayoutManager(linearLayoutManager);
        b.rvHomeBanners.setAdapter(bannersAdapter);
        bannersAdapter.setBannersList(SplashActivity.homeResponse.getData().getBanners());

        //products rv
        productsAdapter=new ProductsAdapter(getContext());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext()
                ,2,RecyclerView.VERTICAL,false);
        b.rvHomeProducts.setLayoutManager(gridLayoutManager);
        b.rvHomeProducts.setAdapter(productsAdapter);
        productsAdapter.setProductsList(SplashActivity.homeResponse.getData().getProducts());
        b.ivHomeFavorite.setOnClickListener(this);
        b.layoutHomeToCategories.setOnClickListener(this);

        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getHomeContent();
    }

    @Override
    public void onClick(View v) {
        if (v==b.ivHomeFavorite){
            startActivity(new Intent(getContext(), FavoritesActivity.class));
        }
        else if(v==b.layoutHomeToCategories){
            startActivity(new Intent(getContext(), CategoriesActivity.class));
        }
    }
    void getHomeContent(){
        String token = pref.getString(getString(R.string.pref_user_token), "");
        Call<HomePageApiResponse> call= RetrofitInstance.getRetrofitInstance()
                .create(ApiInterface.class).getHomePage(token);
        call.enqueue(new Callback<HomePageApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<HomePageApiResponse> call, @NotNull Response<HomePageApiResponse> response) {
                if (response.body() != null) {
                    if(response.body().isStatus()){
                        SplashActivity.homeResponse=response.body();
                        productsAdapter.setProductsList(response.body().getData().getProducts());
                    }
                    else {
                        UiOperations.shortToast(getContext(),response.body().getMessage());
                    }
                }
                else {
                    UiOperations.shortToast(getContext(), "Sorry, connection error");
                }
            }
            @Override
            public void onFailure(@NotNull Call<HomePageApiResponse> call, @NotNull Throwable t) {
                UiOperations.shortToast(getContext(), "Sorry, connection error");
            }
        });
    }
}