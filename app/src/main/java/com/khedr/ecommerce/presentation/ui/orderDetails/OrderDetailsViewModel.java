/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.orderDetails;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.order.CancelOrderResponse;
import com.khedr.ecommerce.data.model.order.GetOrderDetailsResponse;
import com.khedr.ecommerce.data.repository.AppRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class OrderDetailsViewModel extends AndroidViewModel {
    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public OrderDetailsViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    MutableLiveData<Boolean> isGetOrderDetailsLoading = new MutableLiveData<>();
    MutableLiveData<GetOrderDetailsResponse> orderDetailsResponseMLD = new MutableLiveData<>();

    MutableLiveData<Boolean> isCancelOrderLoading = new MutableLiveData<>();
    MutableLiveData<CancelOrderResponse> cancelOrderResponseMLD = new MutableLiveData<>();

    public void getOrderDetails(int id) {
        isGetOrderDetailsLoading.setValue(true);
        GetOrderDetailsResponse nullResponse = new GetOrderDetailsResponse(false, context.getString(R.string.connection_error), null);
        appRepository.getOrderDetails( id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetOrderDetailsResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull GetOrderDetailsResponse getOrderDetailsResponse) {
                        isGetOrderDetailsLoading.setValue(false);
                        orderDetailsResponseMLD.setValue(getOrderDetailsResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isGetOrderDetailsLoading.setValue(false);
                        orderDetailsResponseMLD.setValue(nullResponse);
                        Log.d("medo error",e.getMessage());
                    }
                });


    }

    public void cancelOrder( int id) {
        isCancelOrderLoading.setValue(true);
        CancelOrderResponse nullResponse = new CancelOrderResponse(false, context.getString(R.string.connection_error), null);
        appRepository.cancelOrder(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CancelOrderResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull CancelOrderResponse cancelOrderResponse) {
                        isCancelOrderLoading.setValue(true);
                        cancelOrderResponseMLD.setValue(cancelOrderResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isCancelOrderLoading.setValue(true);
                        cancelOrderResponseMLD.setValue(nullResponse);
                    }
                });


    }


}
