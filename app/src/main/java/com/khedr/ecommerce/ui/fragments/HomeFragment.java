package com.khedr.ecommerce.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;
import com.khedr.ecommerce.databinding.FragmentHomeBinding;
import com.khedr.ecommerce.pojo.homeapi.BannersAndProductsModel;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.ui.activities.Address.AddAddressActivity;
import com.khedr.ecommerce.ui.activities.cart.CartViewModel;
import com.khedr.ecommerce.ui.activities.categories.CategoriesActivity;
import com.khedr.ecommerce.ui.activities.categories.CategoryProductsActivity;
import com.khedr.ecommerce.ui.activities.contactUs.ContactUsActivity;
import com.khedr.ecommerce.ui.activities.favourites.FavoritesActivity;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsViewModel;
import com.khedr.ecommerce.ui.activities.search.SearchActivity;
import com.khedr.ecommerce.ui.activities.splash.SplashActivity;
import com.khedr.ecommerce.ui.adapters.BannersAdapter;
import com.khedr.ecommerce.ui.adapters.RecentlyViewedAdapter;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener, RecentlyViewedAdapter.OnItemClickListener {


    public static BannersAndProductsModel homeResponse;
    FragmentHomeBinding b;
    BannersAdapter bannersAdapter;
    RecentlyViewedAdapter recentProductsAdapter;
    CartViewModel cartViewModel;
    ProductDetailsViewModel productViewModel;
    private int adapterPosition;
    ArrayList<Product> recentProducts = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        productViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ProductDetailsViewModel.class);
        cartViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(CartViewModel.class);

        b.ivHomeFavorite.setOnClickListener(this);
        b.layoutHomeToCategories.setOnClickListener(this);
        b.layoutHomeToContactUs.setOnClickListener(this);
        b.layoutHomeToPreventCorona.setOnClickListener(this);
        b.tvHomeViewAllRecentViewed.setOnClickListener(this);
        b.layoutHomeToChatSupport.setOnClickListener(this);
        b.layoutHomeToDiscountVoucher.setOnClickListener(this);
        b.qrHome.setOnClickListener(this);
        b.svHome.setOnClickListener(this);
        b.layoutBrandApple.setOnClickListener(this);
        b.layoutBrandSamsung.setOnClickListener(this);
        b.layoutBrandXiaomi.setOnClickListener(this);
        b.layoutBrandNike.setOnClickListener(this);
        b.layoutBrandSony.setOnClickListener(this);

        homeResponse = SplashActivity.homeResponse;

        //banners recycler view
        bannersAdapter = new BannersAdapter(getContext());
        bannersAdapter.setBannersList(homeResponse.getBanners());
        b.rvHomeBanners.setAdapter(bannersAdapter);

        //Recent products recycler view
        recentProductsAdapter = new RecentlyViewedAdapter(getContext(), this);
        b.rvHomeRecentlyViewed.setAdapter(recentProductsAdapter);

        productViewModel.recentProductsMTL.observe(getViewLifecycleOwner(), products -> {
            recentProducts = (ArrayList<Product>) products;
            ArrayList<Product> sixProducts = new ArrayList<>();
            for (int i = 0; (i < products.size() && i < 6); i++) {
                sixProducts.add(products.get(i));
            }
            recentProductsAdapter.setProductsList(sixProducts);

            if (recentProductsAdapter.getProductsList().isEmpty()) {
                b.layoutRecentlyViewed.setVisibility(View.GONE);
            } else {
                b.layoutRecentlyViewed.setVisibility(View.VISIBLE);
            }
        });

        observeOnAddProductToCart();
        observeOnUpdateQuantity();


        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        productViewModel.getRecentProducts(requireContext());
        b.rvHomeRecentlyViewed.scrollToPosition(0);
    }

    @Override
    public void onClick(View v) {
        if (v == b.ivHomeFavorite) {
            startActivity(new Intent(requireContext(), FavoritesActivity.class));
        } else if (v == b.layoutHomeToCategories) {
            startActivity(new Intent(requireContext(), CategoriesActivity.class));
        } else if (v == b.layoutHomeToContactUs) {
            startActivity(new Intent(requireContext(), ContactUsActivity.class));
        } else if (v == b.svHome) {
            startActivity(new Intent(requireContext(), SearchActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (v == b.tvHomeViewAllRecentViewed) {
            intentToRecentlyViewedProduct();
        } else if (v == b.layoutHomeToPreventCorona) {
            intentToBrandProducts(getString(R.string.prevent_corona));
        } else if (v == b.layoutBrandApple) {
            intentToBrandProducts(getString(R.string.apple));
        } else if (v == b.layoutBrandSamsung) {
            intentToBrandProducts(getString(R.string.samsung));
        } else if (v == b.layoutBrandXiaomi) {
            intentToBrandProducts(getString(R.string.xiaomi));
        } else if (v == b.layoutBrandNike) {
            intentToBrandProducts(getString(R.string.nike));
        } else if (v == b.layoutBrandSony) {
            intentToBrandProducts(getString(R.string.sony));
        } else if (v == b.qrHome || v == b.layoutHomeToDiscountVoucher || v == b.layoutHomeToChatSupport) {
            UiUtils.shortToast(requireContext(), getString(R.string.coming_soon));
        }

    }
    private void  observeOnAddProductToCart (){
        cartViewModel.postToCartResponseMLD.observe(getViewLifecycleOwner(), postCartResponse -> {
            if (postCartResponse.isStatus()) {
                if (adapterPosition < recentProductsAdapter.getProductsList().size()) {
                    recentProductsAdapter.getProductsList().get(adapterPosition).setIn_cart(true);
                    recentProductsAdapter.notifyItemChanged(adapterPosition);
                }
            }
            if (getActivity() != null) {
                UiUtils.shortToast(requireContext(), postCartResponse.getMessage());
            }
        });
    }
    private void observeOnUpdateQuantity() {
        cartViewModel.updateQuantityResponseMLD.observe(getViewLifecycleOwner(), updateQuantityResponse -> {
            if (updateQuantityResponse.isStatus()) {
                UiUtils.shortToast(requireContext(), getString(R.string.quantity_updated));
            } else {
                UiUtils.shortToast(requireContext(), updateQuantityResponse.getMessage());
            }
        });
    }

    private void intentToRecentlyViewedProduct() {
        Intent intent = new Intent(requireContext(), CategoryProductsActivity.class);
        intent.putExtra(getString(R.string.category_id), -1);
        intent.putExtra(requireContext().getString(R.string.category_name), getString(R.string.recently_viewed));
        intent.putExtra(getString(R.string.products_intent), Converters.fromProductArrayListToString(recentProducts));
        startActivity(intent);
    }

    private void intentToBrandProducts(String brandName) {
        Intent intent = new Intent(requireContext(), CategoryProductsActivity.class);
        intent.putExtra(getString(R.string.category_id), -2);
        intent.putExtra(requireContext().getString(R.string.category_name), brandName);
        startActivity(intent);
    }

    //on recycler view item clicked
    @Override
    public void onParentClicked(int position) {
        Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
        intent.putExtra("product", recentProductsAdapter.getProductsList().get(position));
        requireActivity().startActivity(intent);
    }

    @Override
    public void onItemAddToCartClicked(int position, RecentlyViewedAdapter.ProductsViewHolder productsViewHolder) {

        AddToCartBottomSheetFragment fragment =
                new AddToCartBottomSheetFragment(
                        cartViewModel
                        , recentProductsAdapter.getProductsList().get(position)
                        , productsViewHolder.b.ivProduct.getDrawable());

        fragment.show(requireActivity().getSupportFragmentManager(), "TAG");

        adapterPosition = position;
    }

}