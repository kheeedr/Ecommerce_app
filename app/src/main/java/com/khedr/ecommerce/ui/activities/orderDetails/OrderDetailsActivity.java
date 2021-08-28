/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.orderDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityOrderDetailsBinding;
import com.khedr.ecommerce.pojo.order.GetOrderDetailsResponse;
import com.khedr.ecommerce.ui.adapters.OrderedProductsAdapter;
import com.khedr.ecommerce.utils.Anim;
import com.khedr.ecommerce.utils.UiUtils;

@SuppressLint("SetTextI18n")
public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityOrderDetailsBinding b;
    OrderDetailsViewModel orderDetailsViewModel;
    boolean addressExpanded = false;
    OrderedProductsAdapter orderedProductsAdapter;
    int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        orderDetailsViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(OrderDetailsViewModel.class);
        orderedProductsAdapter = new OrderedProductsAdapter(this);
        b.rvOrderDetails.setAdapter(orderedProductsAdapter);

        int id = getIntent().getIntExtra("order_id", 0);
        orderDetailsViewModel.getOrderDetails(this, id);

        b.layoutOrderDetailsViewAddress.setOnClickListener(this);
        b.btOrderDetailsBack.setOnClickListener(this);
        b.btCancelOrder.setOnClickListener(this);
        observers();
        progressbar();
    }


    @Override
    public void onClick(View v) {
        if (v == b.btOrderDetailsBack) {
            onBackPressed();
        } else if (v == b.layoutOrderDetailsViewAddress) {
            setAddressExpanded(!isAddressExpanded());
        } else if (v == b.btCancelOrder) {
            orderDetailsViewModel.cancelOrder(this, orderId);
        }
    }

    private void observers() {
        orderDetailsViewModel.orderDetailsResponseMLD.observe(this, getOrderDetailsResponse -> {
            if (getOrderDetailsResponse.isStatus()) {
                setOrderDetails(getOrderDetailsResponse);

            } else {
                UiUtils.shortToast(this, getOrderDetailsResponse.getMessage());
                finish();
            }

        });
        orderDetailsViewModel.cancelOrderResponseMLD.observe(this, cancelOrderResponse -> {

            if (cancelOrderResponse.isStatus()) {
                UiUtils.shortToast(this, getString(R.string.order_canceled_successfully));
                finish();
            } else {
                UiUtils.shortToast(this, cancelOrderResponse.getMessage());
            }
        });
    }

    void setOrderDetails(GetOrderDetailsResponse getOrderDetailsResponse) {

        orderId = getOrderDetailsResponse.getData().getId();
        b.tvOrderDetailsId.setText("" + orderId);
        b.tvOrderDetailsDate.setText(getOrderDetailsResponse.getData().getDate());
        b.tvOrderDetailsStatus.setText(getOrderDetailsResponse.getData().getStatus());
        b.tvOrderDetailsCost.setText("" + (int) getOrderDetailsResponse.getData().getCost() + " EGP");
        b.tvOrderDetailsVat.setText("" + (int) getOrderDetailsResponse.getData().getVat() + " EGP");
        b.tvOrderDetailsPaymentMethod.setText(getOrderDetailsResponse.getData().getPayment_method());
        b.tvOrderDetailsTotalCost.setText("" + (int) getOrderDetailsResponse.getData().getTotal() + " EGP");
        b.tvOrderDetailsAddressName.setText(" "+getOrderDetailsResponse.getData().getAddress().getName());
        b.tvExpandedCityNameOrderDetails.append(" "+getOrderDetailsResponse.getData().getAddress().getCity());
        b.tvExpandedRegionNameOrderDetails.append(" "+getOrderDetailsResponse.getData().getAddress().getRegion());
        b.tvExpandedAddressDetailsOrderDetails.append(" "+getOrderDetailsResponse.getData().getAddress().getDetails());

        int points = (int) getOrderDetailsResponse.getData().getPoints();
        if (points > 0) {
            b.tvOrderDetailsPoints.setText("" + points + " EGP");
        } else {
            b.tvOrderDetailsPoints.setVisibility(View.GONE);
            b.textView4PointsOrderDetails.setVisibility(View.GONE);
        }

        int discount = (int) getOrderDetailsResponse.getData().getDiscount();
        if (discount > 0) {
            b.tvOrderDetailsVoucher.setText("" + discount + " EGP");
        } else {
            b.tvOrderDetailsVoucher.setVisibility(View.GONE);
            b.textView5DiscountOrderDetails.setVisibility(View.GONE);
        }
        orderedProductsAdapter.setOrderedItems(getOrderDetailsResponse.getData().getProducts());
        if (isCanceledOrder(getOrderDetailsResponse.getData().getStatus())){
            b.btCancelOrder.setVisibility(View.GONE);
        }
        else {
            b.btCancelOrder.setVisibility(View.VISIBLE);
        }

    }

    private void progressbar() {
        orderDetailsViewModel.isGetOrderDetailsLoading.observe(this, this::showOrHideProgressMoto);
        orderDetailsViewModel.isCancelOrderLoading.observe(this, this::showOrHideProgressMoto);
    }

    void showOrHideProgressMoto(boolean isLoading) {
        Anim.motoProgressbar(
                this, isLoading,
                b.includedProgressOrderDetails.progressMoto,
                b.includedProgressOrderDetails.viewUnderMoto,
                b.includedProgressOrderDetails.getRoot());

    }

    // Cancelled // ملغي
    // جديد // New
    boolean isCanceledOrder(String status){
        return status.equals("Cancelled") || status.equals("ملغي");
    }
    public boolean isAddressExpanded() {
        return addressExpanded;
    }

    public void setAddressExpanded(boolean addressExpanded) {
        this.addressExpanded = addressExpanded;

        if (!addressExpanded) {
            Anim.animZoomOut(this, b.expandableLayoutOrderDetails);
            b.ivOrderDetailsExpandAddress.setRotation(90);
        } else {
            Anim.animZoomIn(this, b.expandableLayoutOrderDetails);
            b.ivOrderDetailsExpandAddress.setRotation(270);
        }


    }

}