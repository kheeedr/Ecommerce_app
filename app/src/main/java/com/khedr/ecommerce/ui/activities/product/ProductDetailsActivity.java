package com.khedr.ecommerce.ui.activities.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityProductDetailsBinding;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.ui.activities.cart.CartActivity;
import com.khedr.ecommerce.ui.activities.cart.CartViewModel;
import com.khedr.ecommerce.ui.activities.favourites.FavouritesViewModel;
import com.khedr.ecommerce.ui.adapters.ProductImagesAdapter;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("SetTextI18n")
public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityProductDetailsBinding b;

    boolean is_favourite;
    boolean isIn_cart;
    ProductImagesAdapter adapter;
    SnapHelper helper = new LinearSnapHelper();
    ArrayList<String> imagesList;
    Product product;
    FavouritesViewModel favouritesViewModel;
    CartViewModel cartViewModel;
    ProductDetailsViewModel productViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_product_details);

        favouritesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(FavouritesViewModel.class);
        cartViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(CartViewModel.class);
        productViewModel=new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ProductDetailsViewModel.class);
        product = (Product) getIntent().getSerializableExtra("product");
        productViewModel.addRecentProduct(this,product);

        imagesList = product.getImages();
        adapter = new ProductImagesAdapter(this, imagesList);
        b.rvProductDetails.setAdapter(adapter);

        b.btProductDetailsBack.setOnClickListener(this);
        b.ivProductDetailsInFavourite.setOnClickListener(this);
        b.ivProductDetailsInCart.setOnClickListener(this);
        b.layoutProductDetailsPlus.setOnClickListener(this);
        b.layoutProductDetailsMinus.setOnClickListener(this);
        b.btProductDetailsToCart.setOnClickListener(this);

        // handles product images recyclerview
        helper.attachToRecyclerView(b.rvProductDetails);
        b.tvProductDetailsImageNum.setText(1 + " / " + imagesList.size());

        b.rvProductDetails.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = ((LinearLayoutManager) Objects.requireNonNull(b.rvProductDetails.getLayoutManager())).findFirstVisibleItemPosition();
                    b.tvProductDetailsImageNum.setText((position + 1) + " / " + imagesList.size());
                }
            }
        });
        manageProgressbar();

        observeOnSetFavoriteResponse();

        observeOnPostToCart();

        observeOnUpdateQuantity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    @Override
    public void onClick(View v) {
        if (v == b.btProductDetailsBack) {
            onBackPressed();
        } else if (v == b.ivProductDetailsInFavourite) {
            addProductToFavorite();
        } else if (v == b.ivProductDetailsInCart) {
            startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class));
        } else if (v == b.layoutProductDetailsPlus) {
            UiUtils.makeTvPlusOne(b.tvProductDetailsEditableQuantity);
        } else if (v == b.layoutProductDetailsMinus) {
            UiUtils.makeTvMinusOne(b.tvProductDetailsEditableQuantity);
        } else if (v == b.btProductDetailsToCart) {
            addToCartOrUpdateQuantity();
        }
    }

    public void refreshView() {

        b.tvProductDetailsName.setText(product.getName());
        b.tvProductDetailsDescription.setText(product.getDescription());
        if (product.isIn_favorites()) {
            b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_red_heart);
            is_favourite = true;
        } else {
            b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_outlined_heart);
            is_favourite = false;
        }
        if (product.isIn_cart()) {
            isIn_cart = true;
            b.btProductDetailsToCart.setText(R.string.Update_quantity);
            b.ivProductDetailsInCart.setImageResource(R.drawable.iv_shopping_cart);
        } else {
            isIn_cart = false;
            b.btProductDetailsToCart.setText(R.string.add_to_cart);
            b.ivProductDetailsInCart.setImageResource(R.drawable.iv_empty_shopping_cart);
        }
        b.tvProductDetailsPrice.setText(product.getPrice() + " EGP");

        if (product.getDiscount() > 0) {
            //old price
            b.tvProductDetailsOldPrice.setPaintFlags(b.tvProductDetailsOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            b.tvProductDetailsOldPrice.setText(String.valueOf(product.getOld_price()));
            b.tvProductDetailsDiscount.setVisibility(View.VISIBLE);
            b.tvProductDetailsDiscount.setText((int) Math.ceil(product.getDiscount()) + "%");

        } else {
            b.tvProductDetailsDiscount.setVisibility(View.GONE);
            b.tvProductDetailsOldPrice.setVisibility(View.GONE);
        }
    }

    private void addToCartOrUpdateQuantity() {

        if (UserUtils.isSignedIn(this)) {
            int productId = product.getId();
            if (!isIn_cart) {
                int productQuantity = Integer.parseInt(b.tvProductDetailsEditableQuantity.getText().toString());
                if (productQuantity == 1) {
                    cartViewModel.addProductToCart(this, productId);
                } else if (productQuantity > 1) {
                    cartViewModel.addMultipleProductsToCart(this, productQuantity, productId);
                }
            } else {
                int newQuantity = Integer.parseInt(b.tvProductDetailsEditableQuantity.getText().toString());
                cartViewModel.updateQuantityFromProductDetails(this, newQuantity, productId);
            }
        } else {
            UiUtils.shortToast(this, getString(R.string.you_should_login_first));
        }
    }

    private void observeOnPostToCart() {
        cartViewModel.postToCartResponseMLD.observe(this, postCartResponse -> {
            if (postCartResponse.isStatus()) {
                isIn_cart = true;
                b.ivProductDetailsInCart.setImageResource(R.drawable.iv_shopping_cart);
                b.btProductDetailsToCart.setText(R.string.Update_quantity);
            }
            UiUtils.shortToast(this, postCartResponse.getMessage());
        });
    }

    private void observeOnUpdateQuantity() {
        cartViewModel.updateQuantityResponseMLD.observe(this, updateQuantityResponse -> {
            if (updateQuantityResponse.isStatus()) {
                b.tvProductDetailsEditableQuantity.setText("1");
                UiUtils.shortToast(this, getString(R.string.quantity_updated));
            } else {
                UiUtils.shortToast(this, updateQuantityResponse.getMessage());
            }
        });
    }

    private void observeOnSetFavoriteResponse() {
        favouritesViewModel.setFavoriteResponseMTL.observe(this, postFavoriteResponse -> {
            if (postFavoriteResponse.isStatus()) {
                is_favourite = !is_favourite;
            } else {
                UiUtils.shortToast(this, postFavoriteResponse.getMessage());
            }

            if (is_favourite) {
                b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_red_heart);
            } else {
                b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_outlined_heart);
            }
        });
    }

    void manageProgressbar() {

        favouritesViewModel.isSetFavoriteLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                UiUtils.animJumpAndFade(this, b.layoutProductDetailsInFavourite);
            } else {
                b.layoutProductDetailsInFavourite.clearAnimation();
            }
        });
        cartViewModel.isPostLoading.observe(this, this::selectBetweenButtonAndProgressbar);
        cartViewModel.isUpdateFromProductLoading.observe(this, this::selectBetweenButtonAndProgressbar);
        cartViewModel.isPostMultipleLoading.observe(this, this::selectBetweenButtonAndProgressbar);
    }
    void selectBetweenButtonAndProgressbar(boolean isLoading){
        if (isLoading) {
            b.btProductDetailsToCart.setVisibility(View.GONE);
            b.progressProductDetailsToCart.setVisibility(View.VISIBLE);
            UiUtils.animJumpAndFade(this, b.progressProductDetailsToCart);

        } else {
            b.progressProductDetailsToCart.clearAnimation();
            b.progressProductDetailsToCart.setVisibility(View.GONE);
            b.btProductDetailsToCart.setVisibility(View.VISIBLE);
        }
    }


    void addProductToFavorite() {
        if (UserUtils.isSignedIn(this))   favouritesViewModel.addProductToFavorite(this, product.getId());

        else UiUtils.shortToast(this, getString(R.string.you_should_login_first));

    }


}