package com.khedr.ecommerce.ui.activities.profile;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.user.UserApiResponse;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {

    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<UserApiResponse> profileResponseMLD = new MutableLiveData<>();

    public void getUserInfo(Context context) {
        isLoading.setValue(true);
        UserApiResponse nullResponse = new UserApiResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().getProfile(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserApiResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NotNull UserApiResponse userApiResponse) {
                        isLoading.setValue(false);
                        if (UserUtils.isSignedIn(context)) {
                            UserUtils.saveUserProfileToShared(userApiResponse, context, null);
                            profileResponseMLD.setValue(userApiResponse);
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isLoading.setValue(false);
                        profileResponseMLD.setValue(nullResponse);
                    }
                });

    }




}
