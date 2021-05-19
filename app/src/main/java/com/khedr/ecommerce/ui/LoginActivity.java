package com.khedr.ecommerce.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.databinding.ActivityLoginBinding;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.pojo.user.UserApiResponse;
import com.khedr.ecommerce.pojo.user.UserDataForLoginRequest;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public ActivityLoginBinding b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_login);
        b.btLogin.setOnClickListener(this);
        b.btToSignup.setOnClickListener(this);
        b.btLoginBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_to_signup) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        } else if (id == R.id.bt_login) {
            String email = Objects.requireNonNull(b.etLoginEmail.getText()).toString();
            String password = Objects.requireNonNull(b.etLoginPassword.getText()).toString();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                b.etLoginEmail.setError("email is not valid");
                b.etLoginEmail.requestFocus();
            } else if (password.length() < 6) {
                b.etLoginPassword.setError("password must be 6 letters at least");
                b.etLoginPassword.requestFocus();
            } else {

                UserDataForLoginRequest user = new UserDataForLoginRequest(email, password);
                userLogin(user);
            }
        }else if (id == R.id.bt_login_back) {
            onBackPressed();
        }
    }

    void userLogin(UserDataForLoginRequest user) {
        b.btLogin.setVisibility(View.GONE);
        b.progressLogin.setVisibility(View.VISIBLE);
        UiUtils.animJumpAndFade(this,b.progressLogin);
        String lang=UiUtils.getAppLang(this);

        Call<UserApiResponse> call = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class).login(lang ,user);
        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserApiResponse> call, @NotNull Response<UserApiResponse> response) {
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        saveUserProfileToShared(response.body(), LoginActivity.this, null);
                        finish();
                    } else {
                        b.btLogin.setVisibility(View.VISIBLE);
                        b.progressLogin.clearAnimation();
                        b.progressLogin.setVisibility(View.INVISIBLE);
                        UiUtils.shortToast(LoginActivity.this, response.body().getMessage());
                    }
                }
                else {
                    UiUtils.shortToast(LoginActivity.this,  getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserApiResponse> call, @NotNull Throwable t) {
                b.btLogin.setVisibility(View.VISIBLE);
                b.progressLogin.clearAnimation();
                b.progressLogin.setVisibility(View.INVISIBLE);
                UiUtils.shortToast(LoginActivity.this,  getString(R.string.connection_error));
            }
        });

    }

    public static void saveUserProfileToShared(UserApiResponse response, Context context, String image) {

        SharedPreferences pref = context.getSharedPreferences("logined", 0);
        SharedPreferences.Editor pen = pref.edit();

        pen.putBoolean(context.getString(R.string.pref_status), true);
        pen.putInt(context.getString(R.string.pref_user_id), response.getData().getId());
        pen.putString(context.getString(R.string.pref_user_name), response.getData().getName());
        pen.putString(context.getString(R.string.pref_user_email), response.getData().getEmail());
        pen.putString(context.getString(R.string.pref_user_phone), response.getData().getPhone());
        pen.putBoolean(context.getString(R.string.pref_is_image_ready), image != null);
        pen.putString(context.getString(R.string.pref_user_image), image);
        pen.putString(context.getString(R.string.pref_user_image_url), response.getData().getImage());
        pen.putInt(context.getString(R.string.pref_user_points), response.getData().getPoints());
        pen.putString(context.getString(R.string.pref_user_credit), String.valueOf(response.getData().getCredit()));
        pen.putString(context.getString(R.string.pref_user_token), response.getData().getToken());

        pen.apply();
    }

}

