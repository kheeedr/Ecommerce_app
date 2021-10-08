/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.signup;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.user.UserApiResponse;
import com.khedr.ecommerce.data.model.user.UserDataForRegisterRequest;
import com.khedr.ecommerce.data.repository.AppRepository;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class SignUpViewModel extends AndroidViewModel {

    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public SignUpViewModel( BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    MutableLiveData<UserApiResponse> responseBody = new MutableLiveData<>();

    public void postNewUser(UserDataForRegisterRequest user) {
        isLoading.setValue(true);
        UserApiResponse nullResponse = new UserApiResponse(false, context.getString(R.string.connection_error), null);
        appRepository.register(user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<UserApiResponse>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
            }

            @Override
            public void onSuccess(@NotNull UserApiResponse userApiResponse) {
                isLoading.setValue(false);
                responseBody.setValue(userApiResponse);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                isLoading.setValue(false);
                responseBody.setValue(nullResponse);
            }
        });
    }


}



