/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityOrderCompletedBinding;
import com.khedr.ecommerce.ui.activities.MainPage.MainPageActivity;

public class OrderCompletedActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityOrderCompletedBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_order_completed);
        b.btToOrders.setOnClickListener(this);
        b.btContinueShopping.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == b.btContinueShopping) {
            intentToHome();
        } else if (v == b.btToOrders) {
            intentToOrders();
        }
    }

    @Override
    public void onBackPressed() {
        intentToHome();
    }

    void intentToHome() {
        startActivity(new Intent(this, MainPageActivity.class)
                .putExtra(getString(R.string.intent_name), "null"));
        finish();
    }

    void intentToOrders() {
        startActivity(new Intent(this, MainPageActivity.class)
                .putExtra(getString(R.string.intent_name), getString(R.string.intent_to_orders)));
        finish();
    }
}