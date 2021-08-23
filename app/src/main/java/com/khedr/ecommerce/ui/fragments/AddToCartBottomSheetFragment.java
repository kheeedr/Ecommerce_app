/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.FragmentAddToCartBinding;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.ui.activities.cart.CartViewModel;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsViewModel;
import com.khedr.ecommerce.ui.adapters.ProductsAdapter;
import com.khedr.ecommerce.ui.adapters.RecentlyViewedAdapter;
import com.khedr.ecommerce.utils.Anim;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;


public class AddToCartBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    FragmentAddToCartBinding b;
    Product product;
    Drawable productImage;
    boolean isIn_cart;
    CartViewModel cartViewModel;
    ProductDetailsViewModel productViewModel;
    int adapterPosition;
    ProductsAdapter productsAdapter;
    RecentlyViewedAdapter recentlyViewedAdapter;

    public AddToCartBottomSheetFragment(ProductsAdapter productsAdapter, int adapterPosition, Drawable productImage) {
        this.productsAdapter = productsAdapter;
        this.adapterPosition = adapterPosition;
        this.product = productsAdapter.getProductsList().get(adapterPosition);
        this.productImage = productImage;
        isIn_cart = product.isIn_cart();
    }

    public AddToCartBottomSheetFragment(RecentlyViewedAdapter recentlyViewedAdapter, int adapterPosition, Drawable productImage) {
        this.recentlyViewedAdapter = recentlyViewedAdapter;
        this.adapterPosition = adapterPosition;
        this.product = recentlyViewedAdapter.getProductsList().get(adapterPosition);
        this.productImage = productImage;
        isIn_cart = product.isIn_cart();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        b = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_add_to_cart, null, false);


        cartViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())).get(CartViewModel.class);
        productViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())).get(ProductDetailsViewModel.class);

        productViewModel.addRecentProduct(requireContext(), product);

        b.actionCancelAddToCart.setOnClickListener(this);
        b.actionAddToCart.setOnClickListener(this);
        b.includeItemCart.layoutCartPlus.setOnClickListener(this);
        b.includeItemCart.layoutCartMinus.setOnClickListener(this);


        setView();
        observers();
        progressbar();
        dialog.setContentView(b.getRoot());

    }

    private void progressbar() {
        cartViewModel.isPostLoading.observe(this, this::showOrHideProgressbar);
        cartViewModel.isPostMultipleLoading.observe(this, this::showOrHideProgressbar);
        cartViewModel.isUpdateFromProductLoading.observe(this, this::showOrHideProgressbar);

    }

    void showOrHideProgressbar(boolean isLoading) {
        if (isLoading) {
            b.layoutAddToCartFragmentSelectAction.setVisibility(View.INVISIBLE);
            b.progressFragmentAddToCart.setVisibility(View.VISIBLE);
            Anim.animJumpAndFade(requireContext(),b.progressFragmentAddToCart);
        } else {
            b.layoutAddToCartFragmentSelectAction.setVisibility(View.VISIBLE);
            b.progressFragmentAddToCart.clearAnimation();
            b.progressFragmentAddToCart.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    void setView() {
        b.includeItemCart.ivCartDelete.setVisibility(View.GONE);
        b.includeItemCart.ivCartProduct.setImageDrawable(productImage);
        b.includeItemCart.tvCartProductName.setText(product.getName());
        b.includeItemCart.tvCartPrice.setText("EGP " + product.getPrice());
        if (product.getDiscount() > 0) {
            b.includeItemCart.tvCartDiscount.setVisibility(View.VISIBLE);
            b.includeItemCart.tvCartDiscount.setText((int) product.getDiscount() + "%");
            b.includeItemCart.tvCartOldPrice.setText(String.valueOf(product.getOld_price()));
            b.includeItemCart.tvCartOldPrice.setPaintFlags(b.includeItemCart.tvCartOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            b.includeItemCart.tvCartOldPrice.setVisibility(View.GONE);
            b.includeItemCart.tvCartDiscount.setVisibility(View.GONE);
        }
        if (product.isIn_cart()) {
            b.tvAlertName.setText(R.string.already_in_cart);
            b.actionAddToCart.setText(R.string.Update_quantity);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == b.actionCancelAddToCart) {
            dismiss();
        } else if (v == b.actionAddToCart) {
            if (UserUtils.isSignedIn(requireContext())) {
                addToCartOrUpdateQuantity();
            } else {
                UiUtils.shortToast(requireContext(), getString(R.string.you_should_login_first));
            }
        } else if (v == b.includeItemCart.layoutCartPlus) {
            UiUtils.makeTvPlusOne(b.includeItemCart.tvCartEditableQuantity);
        } else if (v == b.includeItemCart.layoutCartMinus) {
            UiUtils.makeTvMinusOne(b.includeItemCart.tvCartEditableQuantity);
        }

    }

    private void observers() {
        cartViewModel.postToCartResponseMLD.observe(requireActivity(), postCartResponse -> {
            if (postCartResponse.isStatus()) {
                product.setIn_cart(true);
                productViewModel.addRecentProduct(requireContext(), product);
                updateAdapterItem();
                dismiss();
            }
            if (getActivity() != null) {
                UiUtils.shortToast(requireContext(), postCartResponse.getMessage());
            }
        });

        cartViewModel.updateQuantityResponseMLD.observe(requireActivity(), updateQuantityResponse -> {
            Log.d("medo BottomSheetFragment observeOnUpdateQuantity \n", cartViewModel.toString());
            if (getActivity() != null) {
                if (updateQuantityResponse.isStatus()) {
                    UiUtils.shortToast(requireContext(), getString(R.string.quantity_updated));
                    dismiss();
                } else {
                    UiUtils.shortToast(requireContext(), updateQuantityResponse.getMessage());
                }
            }
        });
    }


    private void addToCartOrUpdateQuantity() {
        int productId = product.getId();
        int productQuantity = Integer.parseInt(b.includeItemCart.tvCartEditableQuantity.getText().toString());
        if (!isIn_cart) {
            if (productQuantity == 1) {
                cartViewModel.addProductToCart(requireContext(), productId);
            } else if (productQuantity > 1) {
                cartViewModel.addMultipleProductsToCart(requireContext(), productQuantity, productId);
                Log.d("medo", "\n" + productId + "\n" + productQuantity);
            }
        } else {
            cartViewModel.updateQuantityFromProductDetails(requireContext(), productQuantity, productId);
        }
    }

    void updateAdapterItem() {
        Log.d("medo tag ", getTag());

        assert getTag() != null;
        if (getTag().equals("RecentlyViewed")) {
            recentlyViewedAdapter.getProductsList().get(adapterPosition).setIn_cart(true);
            recentlyViewedAdapter.notifyItemChanged(adapterPosition);
        } else {
            productsAdapter.getProductsList().get(adapterPosition).setIn_cart(true);
            productsAdapter.notifyItemChanged(adapterPosition);
        }
    }
}
