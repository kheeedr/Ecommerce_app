package com.khedr.ecommerce.ui.activities.updateProfile;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.user.UserApiResponse;
import com.khedr.ecommerce.pojo.user.UserDataForRegisterRequest;

import org.jetbrains.annotations.NotNull;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpdateProfileViewModel extends ViewModel {

    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    MutableLiveData<UserApiResponse> profileResponseMLD = new MutableLiveData<>();


    public void updateUserInfo(Context context, UserDataForRegisterRequest user) {
        isLoading.setValue(true);
        UserApiResponse nullResponse = new UserApiResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().updateProfile(context,user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<UserApiResponse>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
            }

            @Override
            public void onSuccess(@NotNull UserApiResponse userApiResponse) {
                isLoading.setValue(false);
                profileResponseMLD.setValue(userApiResponse);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                isLoading.setValue(false);
                profileResponseMLD.setValue(nullResponse);
            }
        });
    }

}





