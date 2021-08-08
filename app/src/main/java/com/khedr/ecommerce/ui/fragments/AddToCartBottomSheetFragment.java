/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;


public class AddToCartBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    FragmentAddToCartBinding b;
    Product product;
    Drawable productImage;
    boolean isIn_cart;
    CartViewModel cartViewModel;
    ProductDetailsViewModel productViewModel;

    public AddToCartBottomSheetFragment(CartViewModel cartViewModel, Product product, Drawable productImage) {
        this.cartViewModel = cartViewModel;
        this.product = product;
        this.productImage = productImage;
        isIn_cart = product.isIn_cart();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        b = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_add_to_cart, null, false);
        dialog.setContentView(b.getRoot());
        productViewModel=new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())).get(ProductDetailsViewModel.class);

        productViewModel.addRecentProduct(requireContext(),product);

        setView();
        b.actionCancelAddToCart.setOnClickListener(this);
        b.actionAddToCart.setOnClickListener(this);
        b.includeItemCart.layoutCartPlus.setOnClickListener(this);
        b.includeItemCart.layoutCartMinus.setOnClickListener(this);
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

            dismiss();
        } else if (v == b.includeItemCart.layoutCartPlus) {
            UiUtils.makeTvPlusOne(b.includeItemCart.tvCartEditableQuantity);
        } else if (v == b.includeItemCart.layoutCartMinus) {
            UiUtils.makeTvMinusOne(b.includeItemCart.tvCartEditableQuantity);
        }

    }

    private void addToCartOrUpdateQuantity() {
        int productId = product.getId();
        int productQuantity = Integer.parseInt(b.includeItemCart.tvCartEditableQuantity.getText().toString());
        if (!isIn_cart) {
            if (productQuantity == 1) {
                cartViewModel.addProductToCart(requireContext(), productId);
            } else if (productQuantity > 1) {
                cartViewModel.addMultipleProductsToCart(requireContext(), productQuantity, productId);
            }
        } else {
            cartViewModel.updateQuantityFromProductDetails(requireContext(), productQuantity, productId);
        }
    }
}
