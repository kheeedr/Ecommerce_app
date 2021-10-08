/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.profile;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.user.UserApiResponse;
import com.khedr.ecommerce.data.repository.AppRepository;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class ProfileViewModel extends AndroidViewModel {

    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public ProfileViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<UserApiResponse> profileResponseMLD = new MutableLiveData<>();

    public void getUserInfo() {
        isLoading.setValue(true);
        UserApiResponse nullResponse = new UserApiResponse(false, context.getString(R.string.connection_error), null);
        appRepository.getProfile().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
