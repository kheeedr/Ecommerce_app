package com.khedr.ecommerce.ui.fragments;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;
import com.khedr.ecommerce.databinding.FragmentAccountBinding;
import com.khedr.ecommerce.pojo.user.UserApiResponse;
import com.khedr.ecommerce.ui.activities.MainPage.MainPageActivity;
import com.khedr.ecommerce.ui.activities.aboutUs.AboutUsActivity;
import com.khedr.ecommerce.ui.activities.contactUs.ContactUsActivity;
import com.khedr.ecommerce.ui.activities.language.LanguageActivity;
import com.khedr.ecommerce.ui.activities.login.LoginActivity;
import com.khedr.ecommerce.ui.activities.profile.ProfileActivity;
import com.khedr.ecommerce.ui.activities.profile.ProfileViewModel;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;


public class AccountFragment extends Fragment implements View.OnClickListener {

    FragmentAccountBinding b;
    ProfileViewModel profileViewModel;
    SharedPreferences pref;

    private static final String TAG = "AccountFragment";

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);

        profileViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())).get(ProfileViewModel.class);
        pref = UserUtils.getPref(requireContext());


        b.layoutAccountToProfile.setOnClickListener(this);
        b.layoutAccountToAbout.setOnClickListener(this);
        b.layoutAccountToContact.setOnClickListener(this);
        b.layoutAccountToLanguage.setOnClickListener(this);
        b.btAccountBack.setOnClickListener(this);
        b.layoutAccountToFaq.setOnClickListener(this);


        observeOnUserDataChanged();

        return b.getRoot();
    }

    private void observeOnUserDataChanged() {

        profileViewModel.profileResponseMLD.observe(requireActivity(), response -> {
            if (getActivity() != null) {
                if (response.isStatus()) {
                    refreshView();
                } else {
                    UiUtils.shortToast(requireContext(), response.getMessage());
                }
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

        } else {
            // if not signed in
            b.tvAccountUsername.setText(getString(R.string.login_now));
            b.tvAccountUserPhone.setText(getString(R.string.click_here_to_login));
            b.icAccountSmartphone.setVisibility(View.GONE);
            b.ivAccountUserImage.setImageResource(R.drawable.user);
            b.tvAccountNumberOfPoints.setText("0");
            b.tvAccountNumberOfCash.setText("0.00");
            b.tvReferralCode.setText("0000000");
        }

    }

}