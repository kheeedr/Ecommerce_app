/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityAddOrderBinding;
import com.khedr.ecommerce.pojo.order.AddOrderRequest;
import com.khedr.ecommerce.pojo.order.EstimateOrderRequest;
import com.khedr.ecommerce.ui.activities.Address.AddAddressActivity;
import com.khedr.ecommerce.ui.activities.Address.AddressViewModel;
import com.khedr.ecommerce.ui.activities.MainPage.MainPageActivity;
import com.khedr.ecommerce.ui.adapters.SelectAddressAdapter;
import com.khedr.ecommerce.utils.MyTextWatcher;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import java.util.Objects;

@SuppressLint("SetTextI18n")
public class AddOrderActivity extends AppCompatActivity implements View.OnClickListener, SelectAddressAdapter.OnItemClickListener {

    ActivityAddOrderBinding b;
    OrderViewModel orderViewModel;
    AddressViewModel addressViewModel;
    SelectAddressAdapter addressAdapter;
    String total;
    boolean usePoints = false;
    Integer validVoucherId = null;
    boolean useVoucher = false;
    boolean costDetailsVisible = false;
    int selectedAddress = -1;
    int paymentMethod = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_add_order);

        orderViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(OrderViewModel.class);
        addressViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AddressViewModel.class);

        addressAdapter = new SelectAddressAdapter(this);
        b.rvAddOrderSelectAddress.setAdapter(addressAdapter);

        listenToVoucherEditText();

        hideOrderCostDetailsLayoutManger();

        setView();

        observers();

        manageProgressbar();

    }


    void setView() {

        b.layoutCashPay.setOnClickListener(this);
        b.layoutOnlinePay.setOnClickListener(this);
        b.btAddOrderBack.setOnClickListener(this);
        b.btCompleteOrder.setOnClickListener(this);
        b.tvAddOrderNewAddress.setOnClickListener(this);
        b.btUsePointsTrue.setOnClickListener(this);
        b.btUsePointsFalse.setOnClickListener(this);
        b.btUseVoucherTrue.setOnClickListener(this);
        b.btUseVoucherFalse.setOnClickListener(this);
        b.tvAddOrderShowCostDetails.setOnClickListener(this);
        b.layoutCompleteAddOrder.setOnClickListener(this);
        b.btCheckVoucher.setOnClickListener(this);

        total = String.valueOf(getIntent().getIntExtra(getString(R.string.order_total), 0));
        b.tvAddOrderTotal.setText(getString(R.string.total) + " " + total + " EGP");
        b.tvOnlinePayPersonName.setText(UserUtils.getUserName(this));
        b.tvOnlinePayCardNumber.setText(getCardNumber() + " ✲✲✲✲ ✲✲✲✲ ✲✲✲✲ ");

    }

    private void listenToVoucherEditText() {
        b.etCheckVoucher.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (s.toString().isEmpty()) {
                    b.btCheckVoucher.setBackgroundTintList(getColorStateList(R.color.natural3));
                } else {
                    b.btCheckVoucher.setBackgroundTintList(getColorStateList(R.color.primary));
                }
                if (getValidVoucherId() != null) setValidVoucherId(null);

                b.etCheckVoucherLayout.setErrorEnabled(false);
                b.ivValidVoucher.setVisibility(View.GONE);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressViewModel.getAddresses(this);
        orderViewModel.estimateOrder(this, new EstimateOrderRequest(isUsePoints(), getValidVoucherId()));

    }

    @Override
    public void onBackPressed() {
        if (isCostDetailsVisible()) {
            setCostDetailsVisible(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == b.btAddOrderBack) {
            onBackPressed();
        } else if (v == b.layoutCashPay) {
            setPaymentMethod(1);
        } else if (v == b.layoutOnlinePay) {
            setPaymentMethod(2);
        } else if (v == b.tvAddOrderNewAddress) {
            startActivity(new Intent(this, AddAddressActivity.class)
                    .putExtra("Intent_name", "Add Address"));
        } else if (v == b.btCompleteOrder) {
            if (isOrderReady()) {
                int shippingAddressId = addressAdapter.getAddressesList().get(getSelectedAddress()).getId();
                String promoCode;
                if (getValidVoucherId() != null) {
                    promoCode = Objects.requireNonNull(b.etCheckVoucher.getText()).toString();
                } else {
                    promoCode = null;
                }
                orderViewModel.addOrder(this, new AddOrderRequest(shippingAddressId, getPaymentMethod(), isUsePoints(), promoCode));
            } else {
                setUncompletedOrderDataError();
            }
        } else if (v == b.btUsePointsTrue) {
            setUsePoints(true);
        } else if (v == b.btUsePointsFalse) {
            setUsePoints(false);
        } else if (v == b.tvAddOrderShowCostDetails) {
            setCostDetailsVisible(!isCostDetailsVisible());
        } else if (v == b.btUseVoucherTrue) {
            setUseVoucher(true);
        } else if (v == b.btUseVoucherFalse) {
            setUseVoucher(false);
        } else if (v == b.btCheckVoucher) {
            String promoCode = Objects.requireNonNull(b.etCheckVoucher.getText()).toString();
            if (promoCode.isEmpty()) {
                b.etCheckVoucherLayout.setErrorTextColor(getColorStateList(R.color.red));
                UiUtils.textError(b.etCheckVoucherLayout, getString(R.string.empty_promo_code));
                b.btCheckVoucher.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, b.etCheckVoucher.getHeight()));
            } else {
                orderViewModel.validatePromoCode(this, promoCode);
            }
        }

    }

    @Override
    public void onAddressClick(int position, SelectAddressAdapter.AddressesViewHolder holder) {
        if (addressAdapter.getAddressesList().get(position).isSelected()) {
            setSelectedAddress(-1);
            addressAdapter.getAddressesList().get(position).setSelected(false);
            addressAdapter.notifyItemChanged(position);
        } else {
            if (getSelectedAddress() != -1) {
                addressAdapter.getAddressesList().get(getSelectedAddress()).setSelected(false);
                addressAdapter.notifyItemChanged(getSelectedAddress());
            }
            addressAdapter.getAddressesList().get(position).setSelected(true);
            addressAdapter.notifyItemChanged(position);
            setSelectedAddress(position);
        }
        if (isOrderReady())
            b.btCompleteOrder.setBackgroundResource(R.drawable.bt_back_filled);
        else {
            b.btCompleteOrder.setBackgroundResource(R.drawable.bt_back_filled_disabled);
        }
    }

    @Override
    public void onExpandClick(int position, SelectAddressAdapter.AddressesViewHolder holder) {

        if (addressAdapter.getAddressesList().get(position).isExpanded()) {

            UiUtils.animZoomOut(this, holder.b.expandableLayoutSelectAddress);
            addressAdapter.getAddressesList().get(position).setExpanded(false);


        } else {
            UiUtils.animZoomIn(this, holder.b.expandableLayoutSelectAddress);
            addressAdapter.getAddressesList().get(position).setExpanded(true);
            b.rvAddOrderSelectAddress.scrollToPosition(position);
        }
        addressAdapter.notifyItemChanged(position);
    }

    private void manageProgressbar() {
        orderViewModel.isAddOrderLoading.observe(this, this::showOrHideProgressMoto);
        addressViewModel.isGetAddressesLoading.observe(this, this::showOrHideProgressMoto);
        orderViewModel.isEstimateLoading.observe(this, isLoading -> {
            if (isLoading) {
                b.progressEstimateOrder.setVisibility(View.VISIBLE);
            } else {
                b.progressEstimateOrder.setVisibility(View.GONE);
            }
        });
        orderViewModel.isValidateLoading.observe(this, isLoading -> {
            if (isLoading) {
                b.progressCheckVoucher.setVisibility(View.VISIBLE);
                b.ivValidVoucher.setVisibility(View.GONE);
            } else {
                b.progressCheckVoucher.setVisibility(View.GONE);

            }
        });

    }

    void showOrHideProgressMoto(boolean isLoading) {
        UiUtils.motoProgressbar(
                this, isLoading,
                b.includedProgressMotoAddOrder.progressMoto,
                b.includedProgressMotoAddOrder.viewUnderMoto,
                b.includedProgressMotoAddOrder.getRoot());
    }

    private void observers() {
        orderViewModel.addOrderResponseMLD.observe(this, addOrderResponse ->
        {
            if (addOrderResponse.isStatus()) {
                intentToOrderCompleted();
            } else {
                UiUtils.shortToast(this, addOrderResponse.getMessage());
            }
        });

        addressViewModel.getAddressesResponseMLD.observe(this, getAddressesResponse -> {
            if (getAddressesResponse.isStatus()) {
                addressAdapter.setAddressesList(getAddressesResponse.getData().getData());
            } else {
                UiUtils.shortToast(this, getAddressesResponse.getMessage());
                finish();
            }
        });

        orderViewModel.estimateOrderResponseMLD.observe(this, estimateOrderResponse -> {
            if (estimateOrderResponse.isStatus()) {

                int newTotal = (int) Math.ceil(estimateOrderResponse.getData().getTotal());
                int cost = (int) Math.ceil(estimateOrderResponse.getData().getSub_total());
                int pointsDiscount = (int) Math.ceil(estimateOrderResponse.getData().getPoints());
                int voucherDiscount = (int) Math.ceil(estimateOrderResponse.getData().getDiscount());
                int vat = newTotal - (cost - (pointsDiscount + voucherDiscount));

                b.tvAddOrderTotal.setText(getString(R.string.total) + " " + newTotal + " EGP");
                b.tvAddOrderCost.setText(getString(R.string.cost) + " " + cost + " EGP");
                b.tvAddOrderVat.setText(getString(R.string.vat) + " " + vat + " EGP");
                if (pointsDiscount != 0) {
                    b.tvAddOrderPointsDiscount.setVisibility(View.VISIBLE);
                    b.tvAddOrderPointsDiscount.setText(getString(R.string.points_discount) + " " + pointsDiscount + " EGP");
                } else {
                    b.tvAddOrderPointsDiscount.setVisibility(View.GONE);
                }
                if (voucherDiscount != 0) {
                    b.tvAddOrderVoucherDiscount.setVisibility(View.VISIBLE);
                    b.tvAddOrderVoucherDiscount.setText(getString(R.string.voucher_discount) + " " + voucherDiscount + " EGP");
                } else {
                    b.tvAddOrderVoucherDiscount.setVisibility(View.GONE);
                }


            } else {
                UiUtils.shortToast(this, estimateOrderResponse.getMessage());
            }
        });

        orderViewModel.validatePromoCodeResponseMLD.observe(this, promoCodeResponse -> {
            if (promoCodeResponse.isStatus()) {
                setValidVoucherId(promoCodeResponse.getData().getId());
                b.ivValidVoucher.setVisibility(View.VISIBLE);

                b.etCheckVoucherLayout.setErrorTextColor(getColorStateList(R.color.green));
                UiUtils.textError(b.etCheckVoucherLayout, getString(R.string.valided_promo_code));
            } else {
                b.etCheckVoucherLayout.setErrorTextColor(getColorStateList(R.color.red));
                UiUtils.textError(b.etCheckVoucherLayout, promoCodeResponse.getMessage());

            }
            b.btCheckVoucher.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, b.etCheckVoucher.getHeight()));

        });

    }


    boolean isOrderReady() {
        return getPaymentMethod() != 0 && getSelectedAddress() != -1;
    }

    void setUncompletedOrderDataError() {
        if (getPaymentMethod() == 0) {
            UiUtils.shortToast(this, getString(R.string.please_select_payment_method));
        } else if (getSelectedAddress() == -1) {
            UiUtils.shortToast(this, getString(R.string.please_select_shipping_address));
        }
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

    private void intentToOrderCompleted() {
        Intent intent = new Intent(this, OrderCompletedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finishAffinity();
        startActivity(intent);
        finish();
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
        if (isOrderReady()) {
            b.btCompleteOrder.setBackgroundResource(R.drawable.bt_back_filled);
        } else {
            b.btCompleteOrder.setBackgroundResource(R.drawable.bt_back_filled_disabled);
        }
    }

    public int getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(int selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public Integer getValidVoucherId() {
        return validVoucherId;
    }

    public void setValidVoucherId(Integer validVoucherId) {
        this.validVoucherId = validVoucherId;
        orderViewModel.estimateOrder(this, new EstimateOrderRequest(isUsePoints(), validVoucherId));
    }

    public boolean isCostDetailsVisible() {
        return costDetailsVisible;
    }

    public void setCostDetailsVisible(boolean costDetailsVisible) {
        this.costDetailsVisible = costDetailsVisible;
        if (costDetailsVisible) {
            b.layoutAddOrderShowCostDetails.setVisibility(View.VISIBLE);
            b.tvAddOrderShowCostDetails.setText(R.string.hide_details);
        } else {
            b.layoutAddOrderShowCostDetails.setVisibility(View.GONE);
            b.tvAddOrderShowCostDetails.setText(getString(R.string.show_details));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideOrderCostDetailsLayoutManger() {
        b.layoutAddOrderScrollChild.setOnTouchListener((v, event) -> {
            setCostDetailsVisible(false);
            return false;
        });
        b.scrollAddOrder.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> setCostDetailsVisible(false));
    }


    public boolean isUseVoucher() {
        return useVoucher;
    }

    public void setUseVoucher(boolean useVoucher) {
        this.useVoucher = useVoucher;
        if (useVoucher) {
            b.btUseVoucherTrue.setBackgroundResource(R.drawable.bt_back_filled);
            b.btUseVoucherTrue.setTextColor(getColor(R.color.white));
            b.btUseVoucherFalse.setBackgroundResource(R.color.trans);
            b.btUseVoucherFalse.setTextColor(getColor(R.color.secondary));
            UiUtils.animZoomIn(this, b.layoutCheckVoucher);
        } else {
            b.btUseVoucherTrue.setBackgroundResource(R.color.trans);
            b.btUseVoucherTrue.setTextColor(getColor(R.color.secondary));
            b.btUseVoucherFalse.setBackgroundResource(R.drawable.bt_back_filled);
            b.btUseVoucherFalse.setTextColor(getColor(R.color.white));
            UiUtils.animZoomOut(this, b.layoutCheckVoucher);
            b.etCheckVoucher.setText("");
            orderViewModel.estimateOrder(this, new EstimateOrderRequest(isUsePoints(), null));
        }

    }

    public boolean isUsePoints() {
        return usePoints;
    }

    public void setUsePoints(boolean usePoints) {
        this.usePoints = usePoints;
        if (usePoints) {
            b.btUsePointsTrue.setBackgroundResource(R.drawable.bt_back_filled);
            b.btUsePointsTrue.setTextColor(getColor(R.color.white));
            b.btUsePointsFalse.setBackgroundResource(R.color.trans);
            b.btUsePointsFalse.setTextColor(getColor(R.color.secondary));
            orderViewModel.estimateOrder(this, new EstimateOrderRequest(true, getValidVoucherId()));

        } else {
            b.btUsePointsTrue.setBackgroundResource(R.color.trans);
            b.btUsePointsTrue.setTextColor(getColor(R.color.secondary));
            b.btUsePointsFalse.setBackgroundResource(R.drawable.bt_back_filled);
            b.btUsePointsFalse.setTextColor(getColor(R.color.white));
            orderViewModel.estimateOrder(this, new EstimateOrderRequest(false, getValidVoucherId()));
        }
    }
}
