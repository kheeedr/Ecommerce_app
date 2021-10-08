/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.splash;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.homeapi.HomePageApiResponse;
import com.khedr.ecommerce.data.repository.AppRepository;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class SplashViewModel extends AndroidViewModel {
    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public SplashViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<HomePageApiResponse> responseBody = new MutableLiveData<>();

    public void getHomeContent() {
        isLoading.setValue(true);
        HomePageApiResponse nullResponse = new HomePageApiResponse(false, context.getString(R.string.connection_error), null);
        appRepository.getHomePage().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<HomePageApiResponse>() {


                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull HomePageApiResponse homePageApiResponse) {
                        isLoading.setValue(false);
                        responseBody.setValue(homePageApiResponse);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isLoading.setValue(false);
                        responseBody.setValue(nullResponse);
                    }
                });


    }
}

