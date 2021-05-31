package com.khedr.ecommerce.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;
import com.khedr.ecommerce.databinding.ActivityUpdateProfileBinding;
import com.khedr.ecommerce.pojo.user.UserApiResponse;
import com.khedr.ecommerce.pojo.user.UserDataForRegisterRequest;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.khedr.ecommerce.utils.UiUtils.countWordsUsingSplit;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQ_CODE = 102;
    private static final String TAG = "UpdateProfileActivity";
    ActivityUpdateProfileBinding b;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        pref = getSharedPreferences("logined", 0);

        b.btUpdateProfileBack.setOnClickListener(this);
        b.ivUpdateUserImage.setOnClickListener(this);
        b.btUpdateProfileSubmit.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {

        if (v == b.btUpdateProfileBack) {
            onBackPressed();
        } else if (v == b.ivUpdateUserImage) {
            chooseImageIntent();
        } else if (v == b.btUpdateProfileSubmit) {

            validateAndUpdateUser();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
        finish();
    }

    public void validateAndUpdateUser() {

        String name = Objects.requireNonNull(b.etUpdateName.getText()).toString();
        String email = Objects.requireNonNull(b.etUpdateEmail.getText()).toString();
        String phone = Objects.requireNonNull(b.etUpdatePhone.getText()).toString();
        String password = Objects.requireNonNull(b.etUpdateNewPassword.getText()).toString();
        String rePassword = Objects.requireNonNull(b.etUpdateRepassword.getText()).toString();
        String image = Converters.fromBitmapToString(((BitmapDrawable) b.ivUpdateUserImage.getDrawable()).getBitmap());

        if (countWordsUsingSplit(name) < 2) {
            b.etUpdateName.setError("please enter full name");
            b.etUpdateName.requestFocus();
        } else if (countWordsUsingSplit(name) > 4) {
            b.etUpdateName.setError("sorry max words allowed is 4");
            b.etUpdateName.requestFocus();
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            b.etUpdatePhone.setError("phone is not valid");
            b.etUpdatePhone.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            b.etUpdateEmail.setError("email is not valid");
            b.etUpdateEmail.requestFocus();
        } else if (password.length() < 6) {
            b.etUpdateNewPassword.setError("password must be 6 letters at least");
            b.etUpdateNewPassword.requestFocus();
        } else if (!password.equals(rePassword)) {
            b.etUpdateRepassword.setError("re-password is not equal the password ");
            b.etUpdateRepassword.requestFocus();
        } else {
            b.btUpdateProfileSubmit.setVisibility(View.GONE);
            b.progressUpdateProfile.setVisibility(View.VISIBLE);
            UserDataForRegisterRequest user = new UserDataForRegisterRequest(name, phone, email, password, image);
            String token = pref.getString(getString(R.string.pref_user_token), "");
            updateUserInfo(user, token);
            Log.d(TAG, "mkhedr"
                    + "\nname:" + user.getName()
                    + "\nphone:" + user.getPhone()
                    + "\nemail:" + user.getEmail()
                    + "\npassword:" + user.getPassword()
                    + "\nimage:" + user.getImage()
            );
        }
    }

    public void updateUserInfo(UserDataForRegisterRequest user, String token) {
        String lang = UiUtils.getAppLang(this);
        Call<UserApiResponse> call = RetrofitInstance.getRetrofitInstance()
                .updateProfile(this, token, user);
        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserApiResponse> call, @NotNull Response<UserApiResponse> response) {
                if (response.body() != null) {
                    if (response.body().isStatus()) {

                        UiUtils.shortToast(UpdateProfileActivity.this, response.body().getMessage());
                        LoginActivity.saveUserProfileToShared(response.body(), UpdateProfileActivity.this, user.getImage());
                        startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
                        finish();
                    } else {
                        b.btUpdateProfileSubmit.setVisibility(View.VISIBLE);
                        b.progressUpdateProfile.setVisibility(View.INVISIBLE);
                        UiUtils.shortToast(UpdateProfileActivity.this, response.body().getMessage());
                    }
                } else {
                    UiUtils.shortToast(UpdateProfileActivity.this, getString(R.string.connection_error));

                }

            }

            @Override
            public void onFailure(@NotNull Call<UserApiResponse> call, @NotNull Throwable t) {
                b.btUpdateProfileSubmit.setVisibility(View.VISIBLE);
                b.progressUpdateProfile.setVisibility(View.INVISIBLE);
                UiUtils.shortToast(UpdateProfileActivity.this, getString(R.string.connection_error));

            }
        });
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