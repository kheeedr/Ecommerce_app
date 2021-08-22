package com.khedr.ecommerce.ui.activities.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;
import com.khedr.ecommerce.databinding.ActivityProfileBinding;
import com.khedr.ecommerce.ui.activities.Address.AddAddressActivity;
import com.khedr.ecommerce.ui.activities.Address.AddressViewModel;
import com.khedr.ecommerce.ui.activities.updateProfile.UpdateProfileActivity;
import com.khedr.ecommerce.ui.adapters.ShowAddressesAdapter;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, ShowAddressesAdapter.OnItemClickListener {
    ActivityProfileBinding b;
    SharedPreferences pref;
    ProfileViewModel profileViewModel;
    AddressViewModel addressViewModel;
    ShowAddressesAdapter addressesAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        pref = UserUtils.getPref(this);

        profileViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ProfileViewModel.class);
        addressViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AddressViewModel.class);
        addressesAdapter = new ShowAddressesAdapter(this);

        b.btProfileBack.setOnClickListener(this);
        b.btProfileEditUserInfo.setOnClickListener(this);
        b.tvProfileNewAddress.setOnClickListener(this);
        b.includedProgressProfile.getRoot().setOnClickListener(this);

        if (pref.getBoolean(getString(R.string.pref_is_image_ready), false)) {
            String photo = pref.getString(getString(R.string.pref_user_image), null);
            b.ivProfilePhoto.setImageBitmap(Converters.fromStringToBitmap(photo));
        } else {
            String url = pref.getString(getString(R.string.pref_user_image_url), null);
            UiUtils.getImageViaUrl(this, url, b.ivProfilePhoto, true);
        }

        b.tvProfileUsername.setText(" " + pref.getString(getString(R.string.pref_user_name), null));
        b.tvProfileMobile.setText(" " + pref.getString(getString(R.string.pref_user_phone), null));
        b.tvProfileEmail.setText(" " + pref.getString(getString(R.string.pref_user_email), null));
        b.rvProfileAddresses.setAdapter(addressesAdapter);


        manageProgressbar();

        observeOnGetAddresses();


    }

    @Override
    protected void onResume() {
        super.onResume();
        addressViewModel.getAddresses(this);
    }

    @Override
    public void onClick(View v) {
        if (v == b.btProfileBack) {
            finish();
        }else if (v==b.includedProgressProfile.getRoot()){
            UiUtils.shortToast(this,getString(R.string.wait));
        }
        else if (v == b.btProfileEditUserInfo) {
            startActivity(new Intent(v.getContext(), UpdateProfileActivity.class));
            finish();
        } else if (v == b.tvProfileNewAddress) {
            startActivity(new Intent(this, AddAddressActivity.class)
                    .putExtra("Intent_name", "Add Address"));
        }
    }

    private void observeOnGetAddresses() {
        addressViewModel.getAddressesResponseMLD.observe(this, getAddressesResponse -> {
            if (getAddressesResponse.isStatus()) {
                addressesAdapter.setAddressesList(getAddressesResponse.getData().getData());
            } else {
                UiUtils.shortToast(this, getAddressesResponse.getMessage());
            }
        });
    }



    private void manageProgressbar() {
        addressViewModel.isGetAddressesLoading.observe(this, this::showOrHideMotoProgressbar);

    }

    void showOrHideMotoProgressbar(boolean isLoading) {
        UiUtils.motoProgressbar(
                this, isLoading,
                b.includedProgressProfile.progressMoto,
                b.includedProgressProfile.viewUnderMoto,
                b.includedProgressProfile.getRoot());
    }


    @Override
    public void onAddressClick(int position) {
        Intent intent = new Intent(this, AddAddressActivity.class);
        intent.putExtra("address", addressesAdapter.getAddressesList().get(position));
        intent.putExtra("Intent_name", "Edit Address");
        startActivity(intent);
    }
}