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
import com.khedr.ecommerce.ui.activities.updateProfile.UpdateProfileActivity;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityProfileBinding b;
    SharedPreferences pref;
    ProfileViewModel viewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        pref = UserUtils.getPref(this);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ProfileViewModel.class);

        b.btProfileLogout.setOnClickListener(this);
        b.btProfileBack.setOnClickListener(this);
        b.btProfileEditUserInfo.setOnClickListener(this);

        if (pref.getBoolean(getString(R.string.pref_is_image_ready), false)) {
            String photo = pref.getString(getString(R.string.pref_user_image), null);
            b.ivProfilePhoto.setImageBitmap(Converters.fromStringToBitmap(photo));
        }
        else {
            String url =pref.getString(getString(R.string.pref_user_image_url),null);
            UiUtils.getImageViaUrl(this,url,b.ivProfilePhoto,true);
        }
        b.tvProfileUsername.setText(" " + pref.getString(getString(R.string.pref_user_name), null));
        b.tvProfileMobile.setText(" " + pref.getString(getString(R.string.pref_user_phone), null));
        b.tvProfileEmail.setText(" " + pref.getString(getString(R.string.pref_user_email), null));

        manageProgressbar();
        viewModel.responseBody.observe(this, userApiResponse -> {

            if (userApiResponse.isStatus()) {
                SharedPreferences.Editor pen = pref.edit();
                pen.putBoolean(getString(R.string.pref_status), false);
                pen.putBoolean(getString(R.string.pref_is_image_ready), false);
                pen.putString(getString(R.string.pref_user_token), "");
                pen.apply();
                UiUtils.shortToast(this, userApiResponse.getMessage());

                finish();
            } else {
                UiUtils.shortToast(ProfileActivity.this, userApiResponse.getMessage());
            }
        });
    }

    private void manageProgressbar() {
        viewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                b.btProfileLogout.setVisibility(View.GONE);
                b.progressLogout.setVisibility(View.VISIBLE);
                UiUtils.animJumpAndFade(this, b.progressLogout);

            } else {
                b.btProfileLogout.setVisibility(View.VISIBLE);
                b.progressLogout.clearAnimation();
                b.progressLogout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_profile_logout) {
            viewModel.logOut(this);
        } else if (id == R.id.bt_profile_back) {
            finish();
        } else if (id == R.id.bt_profile_edit_user_info) {
            startActivity(new Intent(v.getContext(), UpdateProfileActivity.class));
            finish();
        }
    }


}