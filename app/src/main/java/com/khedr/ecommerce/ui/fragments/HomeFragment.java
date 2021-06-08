package com.khedr.ecommerce.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.FragmentHomeBinding;
import com.khedr.ecommerce.pojo.homeapi.BannersAndProductsModel;
import com.khedr.ecommerce.ui.activities.CategoriesActivity;
import com.khedr.ecommerce.ui.activities.CategoryProductsActivity;
import com.khedr.ecommerce.ui.activities.ContactUsActivity;
import com.khedr.ecommerce.ui.activities.favourites.FavoritesActivity;
import com.khedr.ecommerce.ui.activities.search.SearchActivity;
import com.khedr.ecommerce.ui.activities.splash.SplashActivity;
import com.khedr.ecommerce.ui.activities.splash.SplashViewModel;
import com.khedr.ecommerce.ui.adapters.BannersAdapter;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    public static BannersAndProductsModel homeResponse;
    FragmentHomeBinding b;
    BannersAdapter bannersAdapter;
    ProductsAdapter productsAdapter;
    SharedPreferences pref;
    SplashViewModel viewModel;


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
        pref = UserUtils.getPref(requireContext());

        b.ivHomeFavorite.setOnClickListener(this);
        b.layoutHomeToCategories.setOnClickListener(this);
        b.layoutHomeToContactUs.setOnClickListener(this);
        b.layoutHomeToPreventCorona.setOnClickListener(this);
        b.svHome.setOnClickListener(this);

        homeResponse = SplashActivity.homeResponse;

        //banners rv
        bannersAdapter = new BannersAdapter(getContext());
        bannersAdapter.setBannersList(homeResponse.getBanners());
        b.rvHomeBanners.setAdapter(bannersAdapter);

        //products rv
        productsAdapter = new ProductsAdapter(getContext());
        productsAdapter.setProductsList(homeResponse.getProducts());
        b.rvHomeProducts.setAdapter(productsAdapter);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.requireActivity().getApplication())).get(SplashViewModel.class);
        viewModel.responseBody.observe(this.requireActivity(), homePageApiResponse -> {
            if (homePageApiResponse.isStatus()) {
                homeResponse = homePageApiResponse.getData();
                Collections.reverse(homeResponse.getProducts());
                productsAdapter.setProductsList(homeResponse.getProducts());
            }
        });

        Log.d(TAG, "mkhedr: onCreateView");


        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getHomeContent(requireContext());
        Log.d(TAG, "mkhedr: contentRestart");


    }

    @Override
    public void onClick(View v) {
        if (v == b.ivHomeFavorite) {
            startActivity(new Intent(getContext(), FavoritesActivity.class));
        } else if (v == b.layoutHomeToCategories) {
            startActivity(new Intent(getContext(), CategoriesActivity.class));
        } else if (v == b.layoutHomeToContactUs) {
            startActivity(new Intent(getContext(), ContactUsActivity.class));
        } else if (v == b.layoutHomeToPreventCorona) {
            Intent intent = new Intent(getContext(), CategoryProductsActivity.class);
            intent.putExtra(requireContext().getString(R.string.category_name), getString(R.string.prevent_corona));
            startActivity(intent);
        } else if (v == b.svHome) {
            startActivity(new Intent(getContext(), SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }
    }


}