/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.updateProfile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.local.Converters;
import com.khedr.ecommerce.databinding.ActivityUpdateProfileBinding;
import com.khedr.ecommerce.data.model.user.UserDataForRegisterRequest;
import com.khedr.ecommerce.presentation.ui.profile.ProfileActivity;
import com.khedr.ecommerce.utils.Animations;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import java.io.IOException;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUpdateProfileBinding b;
    SharedPreferences pref;
    UpdateProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        pref = getSharedPreferences("logined", 0);
        viewModel = new ViewModelProvider(this).get(UpdateProfileViewModel.class);

        b.btUpdateProfileBack.setOnClickListener(this);
        b.ivUpdateUserImage.setOnClickListener(this);
        b.btUpdateProfileSubmit.setOnClickListener(this);
        manageProgressbar();
        //set user image
        if (pref.getBoolean(getString(R.string.pref_is_image_ready), false)) {
            String photo = pref.getString(getString(R.string.pref_user_image), null);
            b.ivUpdateUserImage.setImageBitmap(Converters.fromStringToBitmap(photo));
        }
        // set user name
        b.etUpdateName.setText(pref.getString(getString(R.string.pref_user_name), null));
        // set phone number
        b.etUpdatePhone.setText(pref.getString(getString(R.string.pref_user_phone), null));
        // set email
        b.etUpdateEmail.setText(pref.getString(getString(R.string.pref_user_email), null));

        viewModel.profileResponseMLD.observe(this, userApiResponse -> {

            UiUtils.shortToast(UpdateProfileActivity.this, userApiResponse.getMessage());

            if (userApiResponse.isStatus()) {
                UserUtils.saveUserProfileToShared(userApiResponse, this,null);
                startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }

    private void manageProgressbar() {
        viewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                b.btUpdateProfileSubmit.setVisibility(View.GONE);
                b.progressUpdateProfile.setVisibility(View.VISIBLE);
                Animations.animJumpAndFade(this, b.progressUpdateProfile);
            } else {
                b.btUpdateProfileSubmit.setVisibility(View.VISIBLE);
                b.progressUpdateProfile.clearAnimation();
                b.progressUpdateProfile.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == b.btUpdateProfileBack) {
            onBackPressed();
        } else if (v == b.ivUpdateUserImage) {
            chooseImageIntent();
        } else if (v == b.btUpdateProfileSubmit) {

            if (isValidUser().first) {
                viewModel.updateUserInfo(isValidUser().second);
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
        finish();
    }

    public Pair<Boolean, UserDataForRegisterRequest> isValidUser() {

        Pair<Boolean, UserDataForRegisterRequest> nullUser = new Pair<>(false, null);

        String name = Objects.requireNonNull(b.etUpdateName.getText()).toString();
        String email = Objects.requireNonNull(b.etUpdateEmail.getText()).toString();
        String phone = Objects.requireNonNull(b.etUpdatePhone.getText()).toString();
        String password = Objects.requireNonNull(b.etUpdateNewPassword.getText()).toString();
        String rePassword = Objects.requireNonNull(b.etUpdateRepassword.getText()).toString();
        String image = Converters.fromBitmapToString(((BitmapDrawable) b.ivUpdateUserImage.getDrawable()).getBitmap());

        if (UiUtils.countWordsUsingSplit(name) < 2) {
            UiUtils.textError(b.etUpdateName, getString(R.string.enter_full_name));
            return nullUser;

        } else if (UiUtils.countWordsUsingSplit(name) > 4) {
            UiUtils.textError(b.etUpdateName, getString(R.string.max_words_4));
            return nullUser;

        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            UiUtils.textError(b.etUpdatePhone, getString(R.string.Invalid_phone));
            return nullUser;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            UiUtils.textError(b.etUpdateEmail, getString(R.string.invalid_email));
            return nullUser;

        } else if (password.length() < 6) {
            UiUtils.textError(b.etUpdateNewPassword, getString(R.string.short_password));
            return nullUser;

        } else if (!password.equals(rePassword)) {
            UiUtils.textError(b.etUpdateRepassword, getString(R.string.invalid_repassword));
            return nullUser;

        } else {
            UserDataForRegisterRequest user = new UserDataForRegisterRequest(name, phone, email, password, image);
            return new Pair<>(true, user);
        }

    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uriUserImage = data.getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriUserImage);
                                b.ivUpdateUserImage.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    public void chooseImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(Intent.createChooser(intent, getString(R.string.select_profile_image)));
    }


}