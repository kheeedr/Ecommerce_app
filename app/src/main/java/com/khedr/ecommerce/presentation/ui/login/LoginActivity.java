/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityLoginBinding;
import com.khedr.ecommerce.data.model.user.UserDataForLoginRequest;
import com.khedr.ecommerce.presentation.ui.signup.SignUpActivity;
import com.khedr.ecommerce.utils.Animations;
import com.khedr.ecommerce.utils.MyTextWatcher;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public ActivityLoginBinding b;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        b.btLogin.setOnClickListener(this);
        b.btToSignup.setOnClickListener(this);
        b.btLoginBack.setOnClickListener(this);

        handlingInputLayoutError();
        manageKeyboardButtonDone();

        manageProgressbar();
        observers();
    }


    @Override
    public void onClick(View v) {

        if (v == b.btToSignup) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        } else if (v == b.btLogin) {
            if (getValidUser() != null) {
                viewModel.userLogin(getValidUser());
            }
        } else if (v == b.btLoginBack) {
            onBackPressed();
        }
    }

    public UserDataForLoginRequest getValidUser() {

        String email = Objects.requireNonNull(b.etLoginEmail.getText()).toString();
        String password = Objects.requireNonNull(b.etLoginPassword.getText()).toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            UiUtils.textError(b.etLoginEmailLayout, getString(R.string.invalid_email));
            return null;
        } else if (password.length() < 6) {
            UiUtils.textError(b.etLoginPasswordLayout, getString(R.string.short_password));
            return null;
        }
        return new UserDataForLoginRequest(email, password);

    }

    private void observers() {
        viewModel.responseBody.observe(this, userApiResponse -> {
            if (userApiResponse.isStatus()) {
                UserUtils.saveUserProfileToShared(userApiResponse, this, null);
                finish();
            } else {
                UiUtils.shortToast(this, userApiResponse.getMessage());
            }
        });
    }

    private void handlingInputLayoutError() {
        b.etLoginEmail.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.etLoginEmailLayout.setErrorEnabled(false);
            }
        });
        b.etLoginPassword.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.etLoginPasswordLayout.setErrorEnabled(false);
            }
        });
    }

    private void manageKeyboardButtonDone() {

        b.etLoginPassword.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                onClick(b.btLogin);
                return true;
            }
            return false;
        });
    }

    private void manageProgressbar() {

        viewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                b.layoutLoginButtons.setVisibility(View.INVISIBLE);
                b.progressLogin.setVisibility(View.VISIBLE);
                Animations.animJumpAndFade(this, b.progressLogin);
            } else {
                b.layoutLoginButtons.setVisibility(View.VISIBLE);
                b.progressLogin.clearAnimation();
                b.progressLogin.setVisibility(View.INVISIBLE);
            }
        });
    }


}

