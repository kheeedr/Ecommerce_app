/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.fragments.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.local.AppDatabase;
import com.khedr.ecommerce.local.Converters;
import com.khedr.ecommerce.databinding.FragmentAccountBinding;
import com.khedr.ecommerce.ui.activities.aboutUs.AboutUsActivity;
import com.khedr.ecommerce.ui.activities.contactUs.ContactUsActivity;
import com.khedr.ecommerce.ui.activities.language.LanguageActivity;
import com.khedr.ecommerce.ui.activities.login.LoginActivity;
import com.khedr.ecommerce.ui.activities.profile.ProfileActivity;
import com.khedr.ecommerce.ui.activities.profile.ProfileViewModel;
import com.khedr.ecommerce.utils.Anim;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;


public class AccountFragment extends Fragment implements View.OnClickListener {

    FragmentAccountBinding b;
    ProfileViewModel profileViewModel;
    SharedPreferences pref;
    AccountFragmentViewModel accountViewModel;
    private static final String TAG = "AccountFragment";

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);


        profileViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())).get(ProfileViewModel.class);
        accountViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())).get(AccountFragmentViewModel.class);

        pref = UserUtils.getPref(requireContext());


        b.layoutAccountToProfile.setOnClickListener(this);
        b.layoutAccountToAbout.setOnClickListener(this);
        b.layoutAccountToContact.setOnClickListener(this);
        b.layoutAccountToLanguage.setOnClickListener(this);
        b.btAccountBack.setOnClickListener(this);
        b.layoutAccountToFaq.setOnClickListener(this);
        b.btLogout.setOnClickListener(this);


        observers();
        progressbar();

        return b.getRoot();
    }


    private void observers() {

        profileViewModel.profileResponseMLD.observe(requireActivity(), response -> {
            if (getActivity() != null) {
                if (response.isStatus()) {

                    refreshView();

                } else {
                    UiUtils.shortToast(requireContext(), response.getMessage());
                }
            }
        });

        accountViewModel.logoutResponseMLD.observe(requireActivity(), userApiResponse -> {

            if (userApiResponse.isStatus()) {
                AppDatabase.getInstance(requireContext()).removeAllRecentProduct();
                SharedPreferences.Editor pen = pref.edit();
                pen.putBoolean(getString(R.string.pref_status), false);
                pen.putBoolean(getString(R.string.pref_is_image_ready), false);
                pen.putString(getString(R.string.pref_user_token), "");
                pen.apply();

                UiUtils.shortToast(requireContext(), userApiResponse.getMessage());
                b.scrollAccount.post(() -> b.scrollAccount.smoothScrollTo(0, (b.layoutAccountToProfile.getTop())));
                refreshView();
            } else {
                UiUtils.shortToast(requireContext(), userApiResponse.getMessage());
            }
        });

    }

    private void progressbar() {

        accountViewModel.isLoading.observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                b.btLogout.setVisibility(View.GONE);
                b.progressLogout.setVisibility(View.VISIBLE);
                Anim.animJumpAndFade(requireContext(), b.progressLogout);

            } else {
                b.btLogout.setVisibility(View.VISIBLE);
                b.progressLogout.clearAnimation();
                b.progressLogout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "mkhedr: onResume");
        //reactivate view
        refreshView();
        profileViewModel.getUserInfo(requireContext());
    }


    @Override
    public void onClick(View v) {

        if (v == b.layoutAccountToProfile) {
            if (UserUtils.isSignedIn(requireContext())) {
                //to profile
                startActivity(new Intent(requireContext(), ProfileActivity.class));
            } else {
                //to login
                startActivity(new Intent(requireContext(), LoginActivity.class));
            }
        } else if (v == b.layoutAccountToAbout) {
            startActivity(new Intent(requireContext(), AboutUsActivity.class));
        } else if (v == b.layoutAccountToContact) {
            startActivity(new Intent(requireContext(), ContactUsActivity.class));
        } else if (v == b.layoutAccountToLanguage) {
            startActivity(new Intent(requireContext(), LanguageActivity.class));
        } else if (v == b.btAccountBack) {
            requireActivity().onBackPressed();
        } else if (v == b.layoutAccountToFaq) {
            UiUtils.shortToast(requireContext(), getString(R.string.coming_soon));
        } else if (v == b.btLogout) {
            accountViewModel.logOut(requireContext());
        }


    }


    @SuppressLint("SetTextI18n")
    public void refreshView() {
        // when refresh view
        if (UserUtils.isSignedIn(requireContext())) {
            //update username and phone
            b.tvAccountUsername.setText(pref.getString(getString(R.string.pref_user_name), "error name"));
            b.tvAccountUserPhone.setText(pref.getString(getString(R.string.pref_user_phone), "error phone"));
            b.icAccountSmartphone.setVisibility(View.VISIBLE);

            //update user image
            if (pref.getBoolean(getString(R.string.pref_is_image_ready), false)) {
                String photo = pref.getString(getString(R.string.pref_user_image), null);
                b.ivAccountUserImage.setImageBitmap(Converters.fromStringToBitmap(photo));
            } else {
                String url = pref.getString(getString(R.string.pref_user_image_url), null);
                UiUtils.getImageViaUrl(requireContext(), url, b.ivAccountUserImage, true);
            }

            //update points
            int points = pref.getInt(getString(R.string.pref_user_points), 0);
            b.tvAccountNumberOfPoints.setText(String.valueOf(points));

            //update cash
            String cash = pref.getString(getString(R.string.pref_user_credit), "0.00");
            b.tvAccountNumberOfCash.setText(cash);

            //update referral code
            int code = pref.getInt(getString(R.string.pref_user_id), 0);
            b.tvReferralCode.setText("000" + code);
            // make logout button gone
            b.layoutLogoutButton.setVisibility(View.VISIBLE);
        } else {
            // if not signed in
            b.tvAccountUsername.setText(getString(R.string.login_now));
            b.tvAccountUserPhone.setText(getString(R.string.click_here_to_login));
            b.icAccountSmartphone.setVisibility(View.GONE);
            b.ivAccountUserImage.setImageResource(R.drawable.user);
            b.tvAccountNumberOfPoints.setText("0");
            b.tvAccountNumberOfCash.setText("0.00");
            b.tvReferralCode.setText("0000000");
            // make logout button visible
            b.layoutLogoutButton.setVisibility(View.GONE);
        }

    }

}