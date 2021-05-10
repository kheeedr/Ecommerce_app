package com.khedr.ecommerce.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;
import com.khedr.ecommerce.databinding.ActivitySignUpBinding;
import com.khedr.ecommerce.pojo.user.UserApiResponse;
import com.khedr.ecommerce.pojo.user.UserDataForRegisterRequest;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQ_CODE = 101;
    private static final String TAG = "SignUpActivity";

    ActivitySignUpBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        b.btSign.setOnClickListener(this);
        b.ivSignUserLayout.setOnClickListener(this);
        b.btSignBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_sign_user_layout) {
            showImagesIntent();
        } else if (id == R.id.bt_sign) {

            validateAndPostUser();
        }
        else if(id== R.id.bt_sign_back){
            finish();
        }
    }

    public void validateAndPostUser() {

        String name = Objects.requireNonNull(b.etSignName.getText()).toString();
        String email = Objects.requireNonNull(b.etSignEmail.getText()).toString();
        String phone = Objects.requireNonNull(b.etSignPhone.getText()).toString();
        String password = Objects.requireNonNull(b.etSignPassword.getText()).toString();
        String rePassword = Objects.requireNonNull(b.etSignRepassword.getText()).toString();
        String image = Converters.fromBitmapToString(((BitmapDrawable) b.ivAddUser.getDrawable()).getBitmap());

        if (UiUtils.countWordsUsingSplit(name) < 2) {
            b.etSignName.setError("please enter full name");
            b.etSignName.requestFocus();
        } else if (UiUtils.countWordsUsingSplit(name) > 4) {
            b.etSignName.setError("sorry max words allowed is 4");
            b.etSignName.requestFocus();
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            b.etSignPhone.setError("phone is not valid");
            b.etSignPhone.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            b.etSignEmail.setError("email is not valid");
            b.etSignEmail.requestFocus();
        } else if (password.length() < 6) {
            b.etSignPassword.setError("password must be 6 letters at least");
            b.etSignPassword.requestFocus();
        } else if (!password.equals(rePassword)) {
            b.etSignRepassword.setError("re-password is not equal the password ");
            b.etSignRepassword.requestFocus();
        } else {
            b.btSign.setVisibility(View.GONE);
            b.progressSign.setVisibility(View.VISIBLE);
            UserDataForRegisterRequest user = new UserDataForRegisterRequest(name, phone, email, password, image);
            postNewUser(user);
            Log.d(TAG, "mkhedr"
                    + "\nname:" + user.getName()
                    + "\nphone:" + user.getPhone()
                    + "\nemail:" + user.getEmail()
                    + "\npassword:" + user.getPassword()
                    + "\nimage:" + user.getImage()
            );
        }
    }

    public void postNewUser(UserDataForRegisterRequest user) {
        Call<UserApiResponse> call = RetrofitInstance.getRetrofitInstance()
                .create(ApiInterface.class).register(user);
        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserApiResponse> call, @NotNull Response<UserApiResponse> response) {

                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        UiUtils.shortToast(SignUpActivity.this, response.body().getMessage()
                                + " You can login now");
                        finish();
                    } else {
                        b.btSign.setVisibility(View.VISIBLE);
                        b.progressSign.setVisibility(View.INVISIBLE);
                        UiUtils.shortToast(SignUpActivity.this, response.body().getMessage());
                    }
                }else {
                    UiUtils.shortToast(SignUpActivity.this, "Sorry, connection error");
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserApiResponse> call, @NotNull Throwable t) {
                b.btSign.setVisibility(View.VISIBLE);
                b.progressSign.setVisibility(View.INVISIBLE);
                UiUtils.shortToast(SignUpActivity.this, "Sorry, connection error");
            }
        });
    }

    private void showImagesIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "please select an Image to upload it"), REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uriUserImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriUserImage);
                b.ivAddUser.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}