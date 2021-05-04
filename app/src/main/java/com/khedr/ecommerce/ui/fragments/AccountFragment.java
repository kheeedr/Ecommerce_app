package com.khedr.ecommerce.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;
import com.khedr.ecommerce.databinding.FragmentAccountBinding;
import com.khedr.ecommerce.ui.AboutUsActivity;
import com.khedr.ecommerce.ui.ContactUsActivity;
import com.khedr.ecommerce.ui.LoginActivity;
import com.khedr.ecommerce.ui.ProfileActivity;
import com.khedr.ecommerce.ui.operations.UiOperations;
import com.khedr.ecommerce.ui.operations.UserOperations;

import org.jetbrains.annotations.NotNull;


public class AccountFragment extends Fragment implements View.OnClickListener {

    FragmentAccountBinding b;

  SharedPreferences pref;
    private static final String TAG = "AccountFragment";

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "mkhedr: onStart");
        refreshView();
    }




    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        b= DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);

        pref = this.getActivity().getSharedPreferences("logined", 0);
        Log.d(TAG, "mkhedr: onCreateView");


        b.layoutAccountToProfile.setOnClickListener(this);
        b.layoutAccountToAbout.setOnClickListener(this);
        b.layoutAccountToContact.setOnClickListener(this);
        b.btAccountBack.setOnClickListener(this);
        //reactive view


        return b.getRoot();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.layout_account_to_profile) {
            if (UserOperations.isSignedIn(this.getActivity())) {
                //to profile
                startActivity(new Intent(v.getContext(), ProfileActivity.class));
            } else {
                //log in
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        } else if (id == R.id.layout_account_to_about) {
            startActivity(new Intent(v.getContext(), AboutUsActivity.class));
        }else if (id == R.id.layout_account_to_contact) {
            startActivity(new Intent(v.getContext(), ContactUsActivity.class));
        } else if (id == R.id.bt_account_back) {
            getActivity().onBackPressed();
        }

    }

    public void refreshView() {
        // when refresh view
        if (UserOperations.isSignedIn(this.getActivity())) {
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
                UiOperations.getImageViaUrl(getContext(),url,  b.ivAccountUserImage,TAG);
            }

            //update points
            int points = (pref.getInt(getString(R.string.pref_user_points), 0));
            b.tvAccountNumberOfPoints.setText(String.valueOf(points)+" ");

            //update cash
            String cash = pref.getString(getString(R.string.pref_user_credit), "0.00");
            b.tvAccountNumberOfCash.setText(cash);

            //update referral code
            int code = pref.getInt(getString(R.string.pref_user_id), 0);
            b.tvReferralCode.setText("000" + String.valueOf(code));

        } else {
            // if not signed in
            b.tvAccountUsername.setText("Login now");
            b.tvAccountUserPhone.setText("click here to login");
            b.icAccountSmartphone.setVisibility(View.GONE);
            b.ivAccountUserImage.setImageResource(R.drawable.user);
            b.tvAccountNumberOfPoints.setText("0");
            b.tvAccountNumberOfCash.setText("0.00");
            b.tvReferralCode.setText("0000000");
        }

    }

}