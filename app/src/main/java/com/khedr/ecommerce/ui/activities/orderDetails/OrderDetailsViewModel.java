/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.orderDetails;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.order.GetOrderDetailsResponse;
import com.khedr.ecommerce.pojo.order.CancelOrderResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailsViewModel extends ViewModel {


    MutableLiveData<Boolean> isGetOrderDetailsLoading = new MutableLiveData<>();
    MutableLiveData<GetOrderDetailsResponse> orderDetailsResponseMLD = new MutableLiveData<>();

    MutableLiveData<Boolean> isCancelOrderLoading = new MutableLiveData<>();
    MutableLiveData<CancelOrderResponse> cancelOrderResponseMLD = new MutableLiveData<>();

    public void getOrderDetails(Context context, int id) {
        isGetOrderDetailsLoading.setValue(true);
        GetOrderDetailsResponse nullResponse = new GetOrderDetailsResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().getOrderDetails(context, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    public void cancelOrder(Context context, int id) {
        isCancelOrderLoading.setValue(true);
        CancelOrderResponse nullResponse = new CancelOrderResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().cancelOrder(context, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
