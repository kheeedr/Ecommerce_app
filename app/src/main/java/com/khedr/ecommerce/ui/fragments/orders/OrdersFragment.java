/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.fragments.orders;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.FragmentOrdersBinding;
import com.khedr.ecommerce.ui.activities.cart.CartActivity;
import com.khedr.ecommerce.ui.activities.orderDetails.OrderDetailsActivity;
import com.khedr.ecommerce.ui.adapters.OrdersAdapter;
import com.khedr.ecommerce.utils.Anim;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;


public class OrdersFragment extends Fragment implements View.OnClickListener, OrdersAdapter.OnItemClickListener {
    FragmentOrdersBinding b;
    OrdersAdapter ordersAdapter;
    OrdersViewModel ordersViewModel;
    boolean isFirstResume = true;
    public static final String TAG = "OrdersFragment";

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false);
        ordersViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(OrdersViewModel.class);
        ordersAdapter = new OrdersAdapter(requireContext(), this);

        b.btOrdersBack.setOnClickListener(this);
        b.btOrdersToCart.setOnClickListener(this);
        b.rvOrdersData.setAdapter(ordersAdapter);
        b.rvOrdersData.setVisibility(View.INVISIBLE);
        observers();
        manageProgressbar();
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserUtils.isSignedIn(requireContext())) {
            ordersViewModel.getOrders(requireContext());
        } else {
            UiUtils.showLoginFragment(requireActivity(), TAG);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (UserUtils.isSignedIn(requireContext())) isFirstResume = false;
    }

    @Override
    public void onClick(View v) {
        if (v == b.btOrdersBack) {
            requireActivity().onBackPressed();
        } else if (v == b.btOrdersToCart) {
            startActivity(new Intent(getContext(), CartActivity.class));
        }
    }

    @Override
    public void onParentClicked(int position) {
        startActivity(new Intent(requireContext(), OrderDetailsActivity.class)
                .putExtra("order_id", ordersAdapter.getList().get(position).getId()));
    }

    private void observers() {
        ordersViewModel.getOrdersResponseMLD.observe(requireActivity(), getAllOrdersResponse -> {
            if (getActivity() != null) {
                if (getAllOrdersResponse.isStatus()) {

                    if (!getAllOrdersResponse.getData().getData().isEmpty()) {
                        ordersAdapter.setOrdersData(getAllOrdersResponse.getData().getData());
                        if (isFirstResume) {
                            Anim.animFadeIn(requireContext(), b.rvOrdersData);
                            Anim.animFadeOut(requireContext(), b.layoutOrdersFragmentEmpty);
                        }
                    } else {
                        Anim.animFadeIn(requireContext(), b.layoutOrdersFragmentEmpty);
                        Anim.animFadeOut(requireContext(), b.rvOrdersData);
                    }
                } else {
                    UiUtils.shortToast(requireContext(), getAllOrdersResponse.getMessage());
                }
            }
        });
    }

    private void manageProgressbar() {
        ordersViewModel.isGetOrdersLoading.observe(requireActivity(), isLoading -> {
            if (isFirstResume) showOrHideProgressMoto(isLoading);
        });
    }

    void showOrHideProgressMoto(boolean isLoading) {
        if (getActivity() != null) {
            Anim.motoProgressbar(
                    requireContext(), isLoading,
                    b.includedProgressOrders.progressMoto,
                    b.includedProgressOrders.viewUnderMoto,
                    b.includedProgressOrders.getRoot());
        }
    }
}