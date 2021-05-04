package com.khedr.ecommerce.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityProductDetailsBinding;
import com.khedr.ecommerce.model.product.Product;
import com.khedr.ecommerce.ui.adapters.ProductImagesAdapter;
import com.khedr.ecommerce.ui.operations.ProductOperations;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    public ActivityProductDetailsBinding b;
    private static final String TAG = "ProductDetailsActivity";

    SharedPreferences pref;
    boolean is_favourite;

    ProductImagesAdapter adapter;
    SnapHelper helper = new LinearSnapHelper();
    ArrayList<String> imagesList;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_product_details);
        pref = getSharedPreferences("logined", 0);

        product= (Product) getIntent().getSerializableExtra("product");
        //imagesList = intent.getStringArrayListExtra("images_list");
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
                    int position = ((LinearLayoutManager) b.rvProductDetails.getLayoutManager()).findFirstVisibleItemPosition();
                    b.tvProductDetailsImageNum.setText((position + 1) + " / " + imagesList.size());
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        refreshView();
    }

    @Override
    public void onClick(View v) {
        if (v == b.btProductDetailsBack) {
            onBackPressed();
        } else if (v == b.ivProductDetailsInFavourite) {
            if (is_favourite) {
                b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_outlined_heart);
                is_favourite = false;
            } else {
                b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_red_heart);
                is_favourite = true;
            }
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
//            boolean inCart = intent.getBooleanExtra("product_is_cart", false);
            int productId = product.getId();
//            int productId = intent.getIntExtra("product_id", 0);
            if (!inCart) {
                ProductOperations.addProductToCart(this, productId, b.btProductDetailsToCart, b.progressProductDetailsToCart);
            } else {
                int newQuantity = Integer.parseInt(b.tvProductDetailsEditableQuantity.getText().toString());
                ProductOperations.updateQuantity(this, newQuantity, productId, b.btProductDetailsToCart, b.progressProductDetailsToCart);
            }
        }
    }

    public void refreshView() {

        b.tvProductDetailsName.setText(product.getName());
//        b.tvProductDetailsName.setText(intent.getStringExtra("product_name"));
        b.tvProductDetailsDescription.setText(product.getDescription());
//        b.tvProductDetailsDescription.setText(intent.getStringExtra("product_description"));
        if (product.isIn_favorites()) {//intent.getBooleanExtra("product_is_favourite", false)
            b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_red_heart);
            is_favourite = true;
        } else {
            b.ivProductDetailsInFavourite.setImageResource(R.drawable.ic_outlined_heart);
            is_favourite = false;
        }
        if (product.isIn_cart()) {//intent.getBooleanExtra("product_is_cart", false)
            b.ivProductDetailsInCart.setImageResource(R.drawable.iv_shopping_cart);
        } else {
            b.ivProductDetailsInCart.setImageResource(R.drawable.iv_empty_shopping_cart);

        }
        b.tvProductDetailsPrice.setText(String.valueOf(product.getPrice()) + " EGP");//intent.getDoubleExtra("product_price", 0)

        if ( product.getDiscount()> 0) {//intent.getDoubleExtra("product_discount", 0)
            //old price
            b.tvProductDetailsOldPrice.setPaintFlags(b.tvProductDetailsOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            b.tvProductDetailsOldPrice.setText(String.valueOf(product.getOld_price()));//intent.getDoubleExtra("product_old_price", 0)
            b.tvProductDetailsDiscount.setVisibility(View.VISIBLE);
            b.tvProductDetailsDiscount.setText(String.valueOf((int) Math.ceil(product.getDiscount())) + "%");//intent.getDoubleExtra("product_discount", 0)

        } else {
            b.tvProductDetailsDiscount.setVisibility(View.GONE);
            b.tvProductDetailsOldPrice.setVisibility(View.GONE);
        }
    }
}