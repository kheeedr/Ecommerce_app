package com.khedr.ecommerce.ui;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;
import com.khedr.ecommerce.databinding.ActivityUpdateProfileBinding;
import com.khedr.ecommerce.model.user.UserApiResponse;
import com.khedr.ecommerce.model.user.UserDataForRegisterRequest;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.khedr.ecommerce.ui.SignUpActivity.countWordsUsingSplit;

public class UpdateProfileActivity extends AppCompatActivity implements  View.OnClickListener {
    private static final int REQ_CODE = 102;
    private static final String TAG ="UpdateProfileActivity" ;
    ActivityUpdateProfileBinding b;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        pref = getSharedPreferences("logined", 0);

        b.btUpdateProfileBack.setOnClickListener(this);
        b.layoutUpdateUserImage.setOnClickListener(this);
        b.btUpdateProfileSubmit.setOnClickListener(this);
        //set user image
        if(pref.getBoolean(getString(R.string.pref_is_image_ready),false)){
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
        int id = v.getId();
        if (id == R.id.bt_update_profile_back) {
            onBackPressed();
        }
        else if (id== R.id.layout_update_user_image){
            showImagesIntent();
        }
        else if(id== R.id.bt_update_profile_submit){

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

        String name = b.etUpdateName.getText().toString();
        String email = b.etUpdateEmail.getText().toString();
        String phone = b.etUpdatePhone.getText().toString();
        String password = b.etUpdateNewPassword.getText().toString();
        String rePassword = b.etUpdateRepassword.getText().toString();
        String image = Converters.fromBitmapToString(((BitmapDrawable) b.ivUpdateUserImage.getDrawable()).getBitmap());

        if (countWordsUsingSplit(name)<2) {
            b.etUpdateName.setError("please enter full name");
            b.etUpdateName.requestFocus();
        }else if (countWordsUsingSplit(name)>4) {
            b.etUpdateName.setError("sorry max words allowed is 4");
            b.etUpdateName.requestFocus();
        }
        else if (!Patterns.PHONE.matcher(phone).matches()) {
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
        }
        else {
            b.btUpdateProfileSubmit.setVisibility(View.GONE);
            b.progressUpdateProfile.setVisibility(View.VISIBLE);
            UserDataForRegisterRequest user = new UserDataForRegisterRequest(name, phone, email, password, image);
            String token=pref.getString(getString(R.string.pref_user_token), "");
            updateUserInfo(user,token);
            Log.d(TAG, "mkhedr"
                    + "\nname:" + user.getName()
                    + "\nphone:" + user.getPhone()
                    + "\nemail:" + user.getEmail()
                    + "\npassword:" + user.getPassword()
                    + "\nimage:" + user.getImage()
            );
        }
    }
    public void updateUserInfo(UserDataForRegisterRequest user,String token){
        Call<UserApiResponse> call= RetrofitInstance.getRetrofitInstance()
                .create(ApiInterface.class).updateProfile(token,user);
        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                if (response.body().isStatus()){

                    Toast.makeText(UpdateProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    LoginActivity.saveUserProfileToShared(response.body(),UpdateProfileActivity.this,user.getImage());
                    startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
                    finish();
                }else {
                    b.btUpdateProfileSubmit.setVisibility(View.VISIBLE);
                    b.progressUpdateProfile.setVisibility(View.INVISIBLE);
                    Toast.makeText(UpdateProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserApiResponse> call, Throwable t) {
                b.btUpdateProfileSubmit.setVisibility(View.VISIBLE);
                b.progressUpdateProfile.setVisibility(View.INVISIBLE);
                Toast.makeText(UpdateProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

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
                b.ivUpdateUserImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}