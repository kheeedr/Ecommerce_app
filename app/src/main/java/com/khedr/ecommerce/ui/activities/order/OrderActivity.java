/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityOrderBinding;
import com.khedr.ecommerce.pojo.order.AddOrderRequest;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

@SuppressLint("SetTextI18n")
public class OrderActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityOrderBinding b;
    OrderViewModel orderViewModel;
    String total;
    private int paymentMethod = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_order);
        orderViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(OrderViewModel.class);

        b.layoutCashPay.setOnClickListener(this);
        b.layoutOnlinePay.setOnClickListener(this);
        b.btAddOrderBack.setOnClickListener(this);
        b.btCompleteOrder.setOnClickListener(this);
        setView();

        orderViewModel.addOrderResponseMLD.observe(this, addOrderResponse -> UiUtils.shortToast(this, addOrderResponse.getMessage()));
        manageProgressMoto();

    }

    private void manageProgressMoto() {
        orderViewModel.isLoading.observe(this, isLoading -> UiUtils.motoProgressbar(
                this, isLoading,
                b.includedProgressMotoAddOrder.progressMoto,
                b.includedProgressMotoAddOrder.viewUnderMoto,
                b.includedProgressMotoAddOrder.getRoot()));
    }

    void setView() {

        total = String.valueOf(getIntent().getIntExtra(getString(R.string.order_total), 0));
        b.tvOrderTotal.append(total + " EGP");
        b.tvOnlinePayPersonName.setText(UserUtils.getUserName(this));
        b.tvOnlinePayCardNumber.setText(getCardNumber() + " ✲✲✲✲ ✲✲✲✲ ✲✲✲✲ ");

    }

    @Override
    public void onClick(View v) {
        if (v == b.btAddOrderBack) {
            onBackPressed();
        } else if (v == b.layoutCashPay) {
            setPaymentMethod(1);
        } else if (v == b.layoutOnlinePay) {
            setPaymentMethod(2);
        } else if (v == b.btCompleteOrder) {
            if (isOrderReady()) {
                orderViewModel.addOrder(this
                        , new AddOrderRequest(1, getPaymentMethod(), false, null));
            } else {
                UiUtils.shortToast(this, "please select payment method");
            }
        }
    }

    boolean isOrderReady() {
        return getPaymentMethod() != 0;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    private String getCardNumber() {

        String phoneNumber = UserUtils.getUserPhone(this);
        if (phoneNumber.length() > 4) {
            return phoneNumber.substring(phoneNumber.length() - 4);
        } else {
            return phoneNumber;
        }

    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
        if (paymentMethod == 1) {
            b.chkCash.setImageResource(R.drawable.ic_true);
            b.chkOnline.setImageResource(R.drawable.ic_empty_circle);
        } else if (paymentMethod == 2) {
            b.chkCash.setImageResource(R.drawable.ic_empty_circle);
            b.chkOnline.setImageResource(R.drawable.ic_true);
        }
        b.btCompleteOrder.setBackgroundResource(R.drawable.bt_back_filled);
    }
}