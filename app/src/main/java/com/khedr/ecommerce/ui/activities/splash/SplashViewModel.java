package com.khedr.ecommerce.ui.activities.splash;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.homeapi.HomePageApiResponse;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashViewModel extends ViewModel {
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<HomePageApiResponse> responseBody = new MutableLiveData<>();

    public void getHomeContent(Context context) {
        isLoading.setValue(true);
        String token = UserUtils.getUserToken(context);
        HomePageApiResponse nullResponse = new HomePageApiResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().getHomePage(context, token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

