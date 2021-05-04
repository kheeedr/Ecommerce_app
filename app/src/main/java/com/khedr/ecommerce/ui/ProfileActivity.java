package com.khedr.ecommerce.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.Converters;
import com.khedr.ecommerce.databinding.ActivityProfileBinding;
import com.khedr.ecommerce.model.user.TokenModel;
import com.khedr.ecommerce.model.user.UserApiResponse;
import com.khedr.ecommerce.network.ApiInterface;
import com.khedr.ecommerce.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityProfileBinding b;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        pref = getSharedPreferences("logined", 0);

        b.btProfileLogout.setOnClickListener(this);
        b.btProfileBack.setOnClickListener(this);
        b.btProfileEditUserInfo.setOnClickListener(this);

        if(pref.getBoolean(getString(R.string.pref_is_image_ready),false)){
            String photo=pref.getString(getString(R.string.pref_user_image),null);
            b.ivProfilePhoto.setImageBitmap(Converters.fromStringToBitmap(photo));
        }
        b.tvProfileUsername.setText(" " + pref.getString(getString(R.string.pref_user_name), null));
        b.tvProfileMobile.setText(" " + pref.getString(getString(R.string.pref_user_phone), null));
        b.tvProfileEmail.setText(" " + pref.getString(getString(R.string.pref_user_email), null));
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id== R.id.bt_profile_logout) {
            b.btProfileLogout.setVisibility(View.GONE);
            b.progressLogout.setVisibility(View.VISIBLE);
            logOut();
        }
        else if(id== R.id.bt_profile_back){
            finish();
        }
        else if(id== R.id.bt_profile_edit_user_info){
            startActivity(new Intent(v.getContext(), UpdateProfileActivity.class));
            finish();
        }
    }

    public void logOut() {
        String token = pref.getString(getString(R.string.pref_user_token), "");
        TokenModel tokenModel=new TokenModel(token);
        Call<UserApiResponse> call = RetrofitInstance.getRetrofitInstance()
                .create(ApiInterface.class).logOut(token,tokenModel);

        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {

                SharedPreferences.Editor pen = pref.edit();
                pen.putBoolean(getString(R.string.pref_status), false);
                pen.putBoolean(getString(R.string.pref_is_image_ready), false);
                pen.putString(getString(R.string.pref_user_token), "");
                pen.apply();

                Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<UserApiResponse> call, Throwable t) {
                b.btProfileLogout.setVisibility(View.VISIBLE);
                b.progressLogout.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}