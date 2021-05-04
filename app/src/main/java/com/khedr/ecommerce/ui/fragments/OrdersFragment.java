package com.khedr.ecommerce.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.FragmentOrdersBinding;
import com.khedr.ecommerce.ui.CartActivity;


public class OrdersFragment extends Fragment implements View.OnClickListener {
    FragmentOrdersBinding b;


    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false);


        b.btOrdersBack.setOnClickListener(this);
        b.btOrdersToCart.setOnClickListener(this);


        return b.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v == b.btOrdersBack) {
            getActivity().onBackPressed();
        } else if (v == b.btOrdersToCart) {
            startActivity(new Intent(getContext(), CartActivity.class));
        }
    }
}