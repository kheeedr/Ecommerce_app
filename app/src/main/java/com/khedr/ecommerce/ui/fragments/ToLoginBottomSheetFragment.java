/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.FragmentToLoginBinding;
import com.khedr.ecommerce.ui.activities.cart.CartActivity;
import com.khedr.ecommerce.ui.activities.favourites.FavoritesActivity;
import com.khedr.ecommerce.ui.activities.login.LoginActivity;
import com.khedr.ecommerce.ui.fragments.orders.OrdersFragment;


public class ToLoginBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    FragmentToLoginBinding b;

    public ToLoginBottomSheetFragment() {
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        b = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_to_login, null, false);
        b.actionCancelToLogin.setOnClickListener(this);
        b.actionToLogin.setOnClickListener(this);

        if (isCancelClosesParent()) {
            dialog.setCanceledOnTouchOutside(false);
        }

        dialog.setContentView(b.getRoot());

    }


    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }

    @Override
    public void onClick(View v) {
        if (v == b.actionCancelToLogin) {
            dismiss();
            closeParentAfterDismiss();

        } else if (v == b.actionToLogin) {
            startActivity(new Intent(requireContext(), LoginActivity.class));
            dismiss();
        }

    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        closeParentAfterDismiss();
    }

    boolean isCancelClosesParent() {
        assert getTag() != null;
        return getTag().equals(CartActivity.TAG) || getTag().equals(FavoritesActivity.TAG) || getTag().equals(OrdersFragment.TAG);
    }

    void closeParentAfterDismiss() {

        assert getTag() != null;

        if (getTag().equals(CartActivity.TAG) || getTag().equals(FavoritesActivity.TAG)) {
            requireActivity().finish();
        } else if (getTag().equals(OrdersFragment.TAG)) {
            requireActivity().onBackPressed();
        }
    }

}
