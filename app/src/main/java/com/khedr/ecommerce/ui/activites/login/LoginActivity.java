package com.khedr.ecommerce.ui.activites.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityLoginBinding;
import com.khedr.ecommerce.pojo.user.UserDataForLoginRequest;
import com.khedr.ecommerce.ui.activites.signUp.SignUpActivity;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public ActivityLoginBinding b;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(LoginViewModel.class);
        b.btLogin.setOnClickListener(this);
        b.btToSignup.setOnClickListener(this);
        b.btLoginBack.setOnClickListener(this);
        manageProgressbar();

        viewModel.responseBody.observe(this, userApiResponse -> {
            if (userApiResponse.isStatus()) {
                UserUtils.saveUserProfileToShared(userApiResponse, LoginActivity.this, null);
                finish();
            } else {
                UiUtils.shortToast(this, userApiResponse.getMessage());
            }
        });

    }

    private void manageProgressbar() {

        viewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                b.btLogin.setVisibility(View.GONE);
                b.progressLogin.setVisibility(View.VISIBLE);
                UiUtils.animJumpAndFade(this, b.progressLogin);
            } else {
                b.btLogin.setVisibility(View.VISIBLE);
                b.progressLogin.clearAnimation();
                b.progressLogin.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_to_signup) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        } else if (id == R.id.bt_login) {
            if (isValidUser().first) {
                viewModel.userLogin(this, isValidUser().second);
            }
        } else if (id == R.id.bt_login_back) {
            onBackPressed();
        }
    }

    public Pair<Boolean, UserDataForLoginRequest> isValidUser() {
        Pair<Boolean, UserDataForLoginRequest> nullUser = new Pair<>(false, null);

        String email = Objects.requireNonNull(b.etLoginEmail.getText()).toString();
        String password = Objects.requireNonNull(b.etLoginPassword.getText()).toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            UiUtils.textError(b.etLoginEmail, getString(R.string.invalid_email));
            return nullUser;
        } else if (password.length() < 6) {
            UiUtils.textError(b.etLoginPassword, getString(R.string.short_password));
            return nullUser;
        } else {
            UserDataForLoginRequest user = new UserDataForLoginRequest(email, password);
            return new Pair<>(true, user);
        }

    }


}

