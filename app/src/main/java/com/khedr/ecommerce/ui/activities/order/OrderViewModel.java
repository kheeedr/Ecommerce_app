/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.order;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.AppDatabase;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.order.AddOrderRequest;
import com.khedr.ecommerce.pojo.order.AddOrderResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderViewModel extends ViewModel {
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<AddOrderResponse> addOrderResponseMLD = new MutableLiveData<>();

    public void addOrder(Context context, AddOrderRequest orderRequest) {
        isLoading.setValue(true);
        AddOrderResponse nullResponse = new AddOrderResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().addOrder(context, orderRequest).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AddOrderResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull AddOrderResponse addOrderResponse) {
                        isLoading.setValue(false);
                        addOrderResponseMLD.setValue(addOrderResponse);
                        if (addOrderResponse.isStatus()) {
                            AppDatabase.getInstance(context).updateAllProducts();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading.setValue(false);
                        addOrderResponseMLD.setValue(nullResponse);
                    }
                });


    }


}
