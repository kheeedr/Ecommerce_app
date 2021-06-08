package com.khedr.ecommerce.ui.activities.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.khedr.ecommerce.pojo.product.favorites.get.InnerData;
import com.khedr.ecommerce.ui.activities.cart.CartActivity;
import com.khedr.ecommerce.ui.activities.favourites.FavouritesViewModel;
import com.khedr.ecommerce.ui.adapters.ProductImagesAdapter;
import com.khedr.ecommerce.utils.ProductUtils;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;


public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    public ActivityProductDetailsBinding b;
//    private static final String TAG = "ProductDetailsActivity";

    SharedPreferences pref;
    boolean is_favourite;

    ProductImagesAdapter adapter;
    SnapHelper helper = new LinearSnapHelper();
    ArrayList<String> imagesList;
    Product product;
    InnerData favouriteProduct;
    FavouritesViewModel favouritesViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_product_details);
        pref = UserUtils.getPref(this);
        favouritesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FavouritesViewModel.class);

        product = (Product) getIntent().getSerializableExtra("product");


        imagesList = product.getImages();
        adapter = new ProductImagesAdapter(this, imagesList);
        b.rvProductDetails.setAdapter(adapter);

        b.btProductDetailsBack.setOnClickListener(this);
        b.ivProductDetailsInFavourite.setOnClickListener(this);
        b.ivProductDetailsInCart.setOnClickListener(this);
        b.layoutProductDetailsPlus.setOnClickListener(this);
        b.layoutProductDetailsMinus.setOnClickListener(this);
        b.btProductDetailsToCart.setOnClickListener(this);

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

        favouritesViewModel.setFavoriteResponseBody.observe(this, postFavoriteResponse -> {
            if (postFavoriteResponse.isStatus()) {
                is_favourite = !is_favourite;

            } else {
                UiUtils.shortToast(this, postFavoriteResponse.getMessage());
                if (is_favourite) {
                    b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_red_heart);

                } else {
                    b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_outlined_heart);
                }
            }
        });

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
            int oldQ = Integer.parseInt(b.tvProductDetailsEditableQuantity.getText().toString());
            int newQ = oldQ + 1;
            b.tvProductDetailsEditableQuantity.setText(String.valueOf(newQ));
        } else if (v == b.layoutProductDetailsMinus) {
            int oldQ = Integer.parseInt(b.tvProductDetailsEditableQuantity.getText().toString());
            if (oldQ > 1) {
                int newQ = oldQ - 1;
                b.tvProductDetailsEditableQuantity.setText(String.valueOf(newQ));
            }
        } else if (v == b.btProductDetailsToCart) {
            boolean inCart = product.isIn_cart();
            int productId = product.getId();
            if (!inCart) {
                ProductUtils.addProductToCart(this, productId, b.btProductDetailsToCart, b.progressProductDetailsToCart);
            } else {
                int newQuantity = Integer.parseInt(b.tvProductDetailsEditableQuantity.getText().toString());
                ProductUtils.getCartIdAndUpdateQuantity(this, newQuantity, productId, b.btProductDetailsToCart, b.progressProductDetailsToCart);
            }
        }
    }

    void addProductToFavorite() {
        if (UserUtils.isSignedIn(this)) {

            if (is_favourite) {
                b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_outlined_heart);

            } else {
                b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_red_heart);
            }
            favouritesViewModel.addProductToFavorite(this, product.getId());
        } else {
            UiUtils.shortToast(this, getString(R.string.you_should_login_first));
            if (is_favourite) {
                b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_red_heart);

            } else {
                b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_outlined_heart);
            }
        }

    }

    @SuppressLint("SetTextI18n")
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
            b.ivProductDetailsInCart.setImageResource(R.drawable.iv_shopping_cart);
        } else {
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
}