package com.khedr.ecommerce.ui.activites.signUp;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.user.UserApiResponse;
import com.khedr.ecommerce.pojo.user.UserDataForRegisterRequest;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SignUpViewModel extends ViewModel {


    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    MutableLiveData<UserApiResponse> responseBody = new MutableLiveData<>();

    public void postNewUser(Context context, UserDataForRegisterRequest user) {
        isLoading.setValue(true);
        UserApiResponse nullResponse = new UserApiResponse(false, context.getString(R.string.connection_error), null);
        Single<UserApiResponse> responseObservable = RetrofitInstance.getRetrofitInstance().register(context, user)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        SingleObserver<UserApiResponse> responseObserver = new SingleObserver<UserApiResponse>() {
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
        };

        responseObservable.subscribe(responseObserver);
    }


}



