/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityAddAddressBinding;
import com.khedr.ecommerce.pojo.Address.AddressData;
import com.khedr.ecommerce.pojo.Address.GetAddressData;
import com.khedr.ecommerce.utils.Anim;
import com.khedr.ecommerce.utils.MyTextWatcher;
import com.khedr.ecommerce.utils.UiUtils;

import java.util.Objects;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAddAddressBinding b;
    AddressViewModel addressViewModel;
    Intent intent;
    GetAddressData address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_add_address);

        addressViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AddressViewModel.class);
        b.btDeleteAddress.setOnClickListener(this);
        b.btAddAddress.setOnClickListener(this);
        b.btAddAddressBack.setOnClickListener(this);

        b.includedProgressAddAddress.getRoot().setOnClickListener(this);
        if (getIntent() != null) {
            intent = getIntent();
            if (intent.getStringExtra("Intent_name").equals("Edit Address")) {

                setViewAsEditAddress();
            }

        }
        setAddButtonBackground();
        handlingInputLayoutError();
        observeOnDeleteAddress();
        observeOnAddAddress();
        observeOnUpdateAddress();
        manageProgressbar();
    }


    @Override
    public void onClick(View v) {
        if (v == b.btAddAddressBack) {
            onBackPressed();
        } else if (v == b.btDeleteAddress) {
            addressViewModel.deleteAddress(this, address.getId());
        } else if (v == b.includedProgressAddAddress.getRoot()) {
            UiUtils.shortToast(this, getString(R.string.wait));
        } else if (v == b.btAddAddress) {
            if (intent.getStringExtra("Intent_name").equals("Edit Address")) {
                if (validAddress() != null) {
                    addressViewModel.updateAddress(this, address.getId(), validAddress());
                }
            } else {
                if (validAddress() != null) {
                    addressViewModel.addAddress(this, validAddress());
                }
            }
        }
    }

    private void setViewAsEditAddress() {
        address = (GetAddressData) intent.getSerializableExtra("address");
        b.tvAddAddressMain.setText(R.string.edit_address);
        b.etAddAddressName.setText(address.getName());
        b.etAddAddressCity.setText(address.getCity());
        b.etAddAddressRegion.setText(address.getRegion());
        b.etAddAddressDetails.setText(address.getDetails());
        b.etAddAddressNotes.setText(address.getNotes());
        b.btAddAddress.setText(R.string.update_address);
        b.btDeleteAddress.setVisibility(View.VISIBLE);
    }

    private AddressData validAddress() {
        String city = Objects.requireNonNull(b.etAddAddressCity.getText()).toString();
        String region = Objects.requireNonNull(b.etAddAddressRegion.getText()).toString();
        String details = Objects.requireNonNull(b.etAddAddressDetails.getText()).toString();
        String name = Objects.requireNonNull(b.etAddAddressName.getText()).toString();
        String notes = Objects.requireNonNull(b.etAddAddressNotes.getText()).toString();
        if (notes.isEmpty()) {
            notes = "";
        }
        if (city.isEmpty()) {
            UiUtils.textError(b.layoutAddAddressCity, getString(R.string.city_name_error_message));
            return null;
        } else if (region.isEmpty()) {
            UiUtils.textError(b.layoutAddAddressRegion, getString(R.string.region_name_error_message));
            return null;
        } else if (details.isEmpty()) {
            UiUtils.textError(b.layoutAddAddressDetails, getString(R.string.address_details_error_message));
            return null;
        } else if (name.isEmpty()) {
            UiUtils.textError(b.layoutAddAddressName, getString(R.string.address_name_error_message));
            return null;
        } else {
            return new AddressData(name, city, region, details, notes);
        }
    }

    private boolean isAddressReady() {
        String city = Objects.requireNonNull(b.etAddAddressCity.getText()).toString();
        String region = Objects.requireNonNull(b.etAddAddressRegion.getText()).toString();
        String details = Objects.requireNonNull(b.etAddAddressDetails.getText()).toString();
        String name = Objects.requireNonNull(b.etAddAddressName.getText()).toString();

        return !city.isEmpty() && !region.isEmpty() && !details.isEmpty() && !name.isEmpty();
    }

    void setAddButtonBackground() {
        if (isAddressReady()) {
            b.btAddAddress.setBackgroundResource(R.drawable.bt_back_filled);
        } else {
            b.btAddAddress.setBackgroundResource(R.drawable.bt_back_filled_disabled);
        }
    }

    private void observeOnAddAddress() {
        addressViewModel.addAddressResponseMLD.observe(this, addAddressResponse -> {
            if (addAddressResponse.isStatus()) {
                finish();
            }
            UiUtils.shortToast(this, addAddressResponse.getMessage());
        });
    }

    private void observeOnUpdateAddress() {
        addressViewModel.updateAddressResponseMLD.observe(this, addAddressResponse -> UiUtils.shortToast(this, addAddressResponse.getMessage()));
    }

    private void observeOnDeleteAddress() {
        addressViewModel.deleteAddressResponseMLD.observe(this, deleteAddressResponse -> {
            if (deleteAddressResponse.isStatus()) {
                finish();
            }
            UiUtils.shortToast(this, deleteAddressResponse.getMessage());
        });
    }

    private void manageProgressbar() {
        addressViewModel.isDeleteAddressLoading.observe(this, this::showOrHideMotoProgressbar);
        addressViewModel.isAddAddressLoading.observe(this, this::showOrHideMotoProgressbar);
        addressViewModel.isUpdateAddressLoading.observe(this, this::showOrHideMotoProgressbar);
    }

    void showOrHideMotoProgressbar(boolean isLoading) {
        Anim.motoProgressbar(
                this, isLoading,
                b.includedProgressAddAddress.progressMoto,
                b.includedProgressAddAddress.viewUnderMoto,
                b.includedProgressAddAddress.getRoot());
    }

    private void handlingInputLayoutError() {
        b.etAddAddressCity.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.layoutAddAddressCity.setErrorEnabled(false);
                setAddButtonBackground();
            }
        });
        b.etAddAddressRegion.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.layoutAddAddressRegion.setErrorEnabled(false);
                setAddButtonBackground();
            }
        });
        b.etAddAddressDetails.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.layoutAddAddressDetails.setErrorEnabled(false);
                setAddButtonBackground();
            }
        });
        b.etAddAddressName.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.layoutAddAddressName.setErrorEnabled(false);
                setAddButtonBackground();
            }
        });
    }
}