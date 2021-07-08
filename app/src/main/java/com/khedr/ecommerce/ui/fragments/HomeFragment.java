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
import com.khedr.ecommerce.ui.activities.categories.CategoriesActivity;
import com.khedr.ecommerce.ui.activities.categories.CategoryProductsActivity;
import com.khedr.ecommerce.ui.activities.contactUs.ContactUsActivity;
import com.khedr.ecommerce.ui.activities.cart.CartViewModel;
import com.khedr.ecommerce.ui.activities.favourites.FavoritesActivity;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;
import com.khedr.ecommerce.ui.activities.search.SearchActivity;
import com.khedr.ecommerce.ui.activities.splash.SplashActivity;
import com.khedr.ecommerce.ui.activities.splash.SplashViewModel;
import com.khedr.ecommerce.ui.adapters.BannersAdapter;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class HomeFragment extends Fragment implements View.OnClickListener, ProductsAdapter.OnItemClickListener {

    private static final String TAG = "HomeFragment";
    public static BannersAndProductsModel homeResponse;
    FragmentHomeBinding b;
    BannersAdapter bannersAdapter;
    ProductsAdapter productsAdapter;
    SharedPreferences pref;
    SplashViewModel splashViewModel;
    CartViewModel cartViewModel;


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

        // add to cart dialog click listeners
//        b.includedAddToCart.layoutAddToCartDialogParent.setOnClickListener(this);
//        b.includedAddToCart.layoutAddToCartDialog.setOnClickListener(this);
//        b.includedAddToCart.actionSure.setOnClickListener(this);
//        b.includedAddToCart.actionCancel.setOnClickListener(this);
//        b.includedAddToCart.includeItemCart.layoutCartMinus.setOnClickListener(this);
//        b.includedAddToCart.includeItemCart.layoutCartPlus.setOnClickListener(this);
//        //hide delete item button
//        b.includedAddToCart.includeItemCart.ivCartDelete.setVisibility(View.GONE);


        homeResponse = SplashActivity.homeResponse;

        //banners recycler view
        bannersAdapter = new BannersAdapter(getContext());
        bannersAdapter.setBannersList(homeResponse.getBanners());
        b.rvHomeBanners.setAdapter(bannersAdapter);
        cartViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(CartViewModel.class);

        //products recycler view
        productsAdapter = new ProductsAdapter(getContext(),this);
        productsAdapter.setProductsList(homeResponse.getProducts());
        b.rvHomeProducts.setAdapter(productsAdapter);

        splashViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.requireActivity().getApplication())).get(SplashViewModel.class);
        splashViewModel.responseBody.observe(this.requireActivity(), homePageApiResponse -> {
            if (homePageApiResponse.isStatus()) {
                homeResponse = homePageApiResponse.getData();
                Collections.reverse(homeResponse.getProducts());
                if (productsAdapter.getProductsList() != homeResponse.getProducts())
                    productsAdapter.setProductsList(homeResponse.getProducts());
            }
        });

        Log.d(TAG, "mkhedr: onCreateView");


        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        splashViewModel.getHomeContent(requireContext());
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

    //on recycler view item clicked
    @Override
    public void onParentClicked(int position) {
        Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
        intent.putExtra("product", productsAdapter.getProductsList().get(position));
        requireActivity().startActivity(intent);
    }

    @Override
    public void onItemAddToCartClicked(int position, ProductsAdapter.ProductsViewHolder productsViewHolder) {
        
    }

}