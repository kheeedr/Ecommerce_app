package com.khedr.ecommerce.ui.activities.cart;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityCartBinding;
import com.khedr.ecommerce.pojo.product.cart.get.GetCartItems;
import com.khedr.ecommerce.ui.activities.MainPage.MainPageActivity;
import com.khedr.ecommerce.ui.activities.addOrder.AddOrderActivity;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;
import com.khedr.ecommerce.ui.adapters.CartAdapter;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressLint("SetTextI18n")
public class CartActivity extends AppCompatActivity implements View.OnClickListener, CartAdapter.OnItemClickListener {

    ActivityCartBinding b;
    CartAdapter cartAdapter;
    CartViewModel cartViewModel;
    public double total;
    int deletedPosition;
    boolean isFirstResume = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        cartViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(CartViewModel.class);


        b.btCartShopNow.setOnClickListener(this);
        b.btCartBack.setOnClickListener(this);
        b.btCartOrderNow.setOnClickListener(this);
        b.includedAlertDialog.layoutAlertDialogParent.setOnClickListener(this);
        b.includedAlertDialog.layoutAlertDialog.setOnClickListener(this);
        b.includedAlertDialog.actionSure.setOnClickListener(this);
        b.includedAlertDialog.actionCancel.setOnClickListener(this);
        b.includeProgressCart.getRoot().setOnClickListener(this);
        b.layoutCartFilled.setVisibility(View.INVISIBLE);
        cartAdapter = new CartAdapter(this);
        b.rvCart.setAdapter(cartAdapter);

        manageProgressbar();

        observers();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserUtils.isSignedIn(this)) {
            cartViewModel.getCartProducts(this);
        } else {
            UiUtils.shortToast(this, getString(R.string.you_should_login_first));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstResume = false;
    }

    @Override
    public void onClick(View v) {
        if (v == b.btCartBack) {
            onBackPressed();
        } else if (v == b.btCartShopNow) {
            intentToMainPage();
        } else if (v == b.includedAlertDialog.actionCancel) {
            hideDeletingDialog();
        } else if (v == b.includedAlertDialog.layoutAlertDialogParent) {
            hideDeletingDialog();
        } else if (v == b.includedAlertDialog.actionSure) {
            // hide deleting dialog and the background
            UiUtils.animZoomOut(this, b.includedAlertDialog.layoutAlertDialog);
            b.includedAlertDialog.getRoot().setVisibility(View.GONE);
            // calling delete function from the viewModel
            cartViewModel.deleteCartProduct(this, cartAdapter.getList().get(deletedPosition).getId());
        } else if (v == b.includeProgressCart.getRoot()) {
            UiUtils.shortToast(this, getString(R.string.wait));
        } else if (v == b.btCartOrderNow) {
            if (UserUtils.isSignedIn(this)) {
                Intent intent = new Intent(this, AddOrderActivity.class);
                intent.putExtra(getString(R.string.order_total), (int) Math.ceil(total));
                startActivity(intent);
            } else {
                UiUtils.shortToast(this, getString(R.string.you_should_login_first));
            }

        }

    }


    // on recycler view item clicked
    @Override
    public void onProductDeleteClicked(int position) {
        showDeletingDialog();
        deletedPosition = position;
    }

    @Override
    public void onPlusClicked(int position, CartAdapter.CartViewHolder holder) {
        int oldQ = Integer.parseInt(holder.b.tvCartEditableQuantity.getText().toString());
        int newQ = oldQ + 1;
        holder.b.tvCartEditableQuantity.setText(String.valueOf(newQ));
        int id = cartAdapter.getList().get(position).getId();
        cartViewModel.updateQuantityFromCart(this, newQ, id);
    }

    @Override
    public void onMinusClicked(int position, CartAdapter.CartViewHolder holder) {

        int oldQ = Integer.parseInt(holder.b.tvCartEditableQuantity.getText().toString());
        if (oldQ > 1) {
            int newQ = oldQ - 1;
            holder.b.tvCartEditableQuantity.setText(String.valueOf(newQ));
            int id = cartAdapter.getList().get(position).getId();
            cartViewModel.updateQuantityFromCart(this, newQ, id);
        }
    }

    @Override
    public void onParentClicked(int position) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product", cartAdapter.getList().get(position).getProduct());
        startActivity(intent);
    }

    // Observers
    private void observers() {
        cartViewModel.getCartResponseMLD.observe(this, getCartResponse -> {
            if (getCartResponse.isStatus()) {
                ArrayList<GetCartItems> cartItems = getCartResponse.getData().getCart_items();
                total = getCartResponse.getData().getTotal();
                b.tvCartTotal.setText(getString(R.string.total) + " " + (int) Math.ceil(total) + " EGP");
                if (cartItems.isEmpty()) {
                    UiUtils.animFadeIn(this,b.layoutCartEmpty);
                    b.layoutCartFilled.setVisibility(View.GONE);
                } else {
                    Collections.reverse(cartItems);
                    cartAdapter.setCartItems(cartItems);
                    b.layoutCartFilled.setVisibility(View.VISIBLE);
                    b.layoutCartEmpty.setVisibility(View.GONE);
                    if (isFirstResume) {
                        UiUtils.animFadeIn(this, b.layoutCartFilled);
                    }
                }
            } else {
                UiUtils.shortToast(this, getCartResponse.getMessage());
            }
        });

        cartViewModel.updateQuantityResponseMLD.observe(this, updateQuantityResponse -> {
            if (updateQuantityResponse.isStatus()) {
                total = updateQuantityResponse.getData().getTotal();
                b.tvCartTotal.setText(getString(R.string.total) + " " + (int) Math.ceil(total) + " EGP");
                List<GetCartItems> items = cartAdapter.getList();
                for (GetCartItems item : items) {
                    if (updateQuantityResponse.getData().getCart().getId() == item.getId()) {
                        item.setQuantity(updateQuantityResponse.getData().getCart().getQuantity());
                        break;
                    }
                }
                UiUtils.shortToast(this, getString(R.string.quantity_updated));
            } else {
                cartAdapter.updateList();
                UiUtils.shortToast(this, updateQuantityResponse.getMessage());
            }

        });

        cartViewModel.deleteProductResponseMLD.observe(this, deleteProductResponse -> {

            if (deleteProductResponse.isStatus()) {
                total = deleteProductResponse.getData().getTotal();
                b.tvCartTotal.setText(getString(R.string.total) + " " + (int) Math.ceil(total) + " EGP");
                cartAdapter.getList().remove(deletedPosition);
                cartAdapter.notifyItemRemoved(deletedPosition);
                if (cartAdapter.getList().isEmpty()) {
                    UiUtils.animFadeOut(this, b.layoutCartFilled);
                    UiUtils.animFadeIn(this,b.layoutCartEmpty);
                }
            }
            UiUtils.shortToast(this, deleteProductResponse.getMessage());
        });

    }


    // Ui control Functions

    private void intentToMainPage() {
        Intent intent = new Intent(CartActivity.this, MainPageActivity.class);
        intent.putExtra(getString(R.string.intent_name), "null");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finishAffinity();
        startActivity(intent);
        finish();
    }


    void showDeletingDialog() {
        UiUtils.animFadeIn(this, b.includedAlertDialog.getRoot(), 500);
        new Handler().postDelayed(() -> b.includedAlertDialog.getRoot().setClickable(true), 500);
        UiUtils.animZoomIn(this, b.includedAlertDialog.layoutAlertDialog);
    }

    void hideDeletingDialog() {
        b.includedAlertDialog.getRoot().setClickable(false);
        UiUtils.animZoomOut(this, b.includedAlertDialog.layoutAlertDialog);
        UiUtils.animFadeOut(this, b.includedAlertDialog.getRoot());
    }

    void manageProgressbar() {
        cartViewModel.isGetLoading.observe(this, isLoading -> {
            if (isFirstResume) showOrHideProgressMoto(isLoading);
        });
        cartViewModel.isDeleteLoading.observe(this, this::showOrHideProgressMoto);
    }

    void showOrHideProgressMoto(boolean isLoading) {
        UiUtils.motoProgressbar(
                this, isLoading,
                b.includeProgressCart.progressMoto,
                b.includeProgressCart.viewUnderMoto,
                b.includeProgressCart.getRoot());
    }

}



