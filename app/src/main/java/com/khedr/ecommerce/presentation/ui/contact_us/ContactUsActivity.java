/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.contact_us;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityContactUsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityContactUsBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        b.btContactBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_contact_back) {
            onBackPressed();
            finish();
        }
    }
}