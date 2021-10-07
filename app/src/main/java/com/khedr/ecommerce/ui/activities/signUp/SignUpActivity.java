package com.khedr.ecommerce.ui.activities.signUp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import com.khedr.ecommerce.local.Converters;
import com.khedr.ecommerce.databinding.ActivitySignUpBinding;
import com.khedr.ecommerce.pojo.user.UserDataForRegisterRequest;
import com.khedr.ecommerce.utils.Anim;
import com.khedr.ecommerce.utils.MyTextWatcher;
import com.khedr.ecommerce.utils.UiUtils;

import java.io.IOException;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySignUpBinding b;
    SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(SignUpViewModel.class);
        b.btSign.setOnClickListener(this);
        b.ivSignUserLayout.setOnClickListener(this);
        b.btSignBack.setOnClickListener(this);

        handlingInputLayoutError();
        manageProgressbar();

        viewModel.responseBody.observe(this, userApiResponse -> {
            if (userApiResponse.isStatus()) {
                UiUtils.shortToast(this, userApiResponse.getMessage() + getString(R.string.you_can_login_now));
                finish();
            } else {
                UiUtils.shortToast(this, userApiResponse.getMessage());
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == b.ivSignUserLayout) {
            chooseImageIntent();
        } else if (v == b.btSign) {
            if (isValidUser().first) {
                viewModel.postNewUser(this, isValidUser().second);
            }
        } else if (v == b.btSignBack) {
            finish();
        }
    }

    private void manageProgressbar() {
        viewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                b.btSign.setVisibility(View.GONE);
                b.progressSign.setVisibility(View.VISIBLE);
                Anim.animJumpAndFade(this, b.progressSign);
            } else {
                b.btSign.setVisibility(View.VISIBLE);
                b.progressSign.clearAnimation();
                b.progressSign.setVisibility(View.INVISIBLE);
            }
        });
    }

    public Pair<Boolean, UserDataForRegisterRequest> isValidUser() {

        Pair<Boolean, UserDataForRegisterRequest> nullUser = new Pair<>(false, null);

        String name = Objects.requireNonNull(b.etSignName.getText()).toString();
        String email = Objects.requireNonNull(b.etSignEmail.getText()).toString();
        String phone = Objects.requireNonNull(b.etSignPhone.getText()).toString();
        String password = Objects.requireNonNull(b.etSignPassword.getText()).toString();
        String rePassword = Objects.requireNonNull(b.etSignRepassword.getText()).toString();
        String image = Converters.fromBitmapToString(((BitmapDrawable) b.ivAddUser.getDrawable()).getBitmap());

        if (UiUtils.countWordsUsingSplit(name) < 2) {
            UiUtils.textError(b.etSignNameLayout, getString(R.string.enter_full_name));
            return nullUser;

        } else if (UiUtils.countWordsUsingSplit(name) > 4) {
            UiUtils.textError(b.etSignNameLayout, getString(R.string.max_words_4));
            return nullUser;

        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            UiUtils.textError(b.etSignPhoneLayout, getString(R.string.Invalid_phone));
            return nullUser;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            UiUtils.textError(b.etSignEmailLayout, getString(R.string.invalid_email));
            return nullUser;

        } else if (password.length() < 6) {
            UiUtils.textError(b.etSignPasswordLayout, getString(R.string.short_password));
            return nullUser;

        } else if (!password.equals(rePassword)) {
            UiUtils.textError(b.etSignRepasswordLayout, getString(R.string.invalid_repassword));
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
                                b.ivAddUser.setImageBitmap(bitmap);
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

    @SuppressLint("ClickableViewAccessibility")
    private void handlingInputLayoutError() {
        b.etSignName.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.etSignNameLayout.setErrorEnabled(false);
            }
        });

        b.etSignEmail.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.etSignEmailLayout.setErrorEnabled(false);
            }
        });

        b.etSignPhone.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.etSignPhoneLayout.setErrorEnabled(false);
            }
        });

        b.etSignPassword.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.etSignPasswordLayout.setErrorEnabled(false);
            }
        });

        b.etSignRepassword.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                b.etSignRepasswordLayout.setErrorEnabled(false);
            }
        });


    }
}