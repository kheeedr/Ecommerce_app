/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.fragments.orders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.order.AddOrderResponse;
import com.khedr.ecommerce.pojo.order.GetAllOrdersResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrdersViewModel extends ViewModel {


    public MutableLiveData<Boolean> isGetOrdersLoading = new MutableLiveData<>();
    public MutableLiveData<GetAllOrdersResponse> getOrdersResponseMLD = new MutableLiveData<>();

    public void getOrders(Context context) {
        isGetOrdersLoading.setValue(true);
        GetAllOrdersResponse nullResponse=new GetAllOrdersResponse(false, context.getString(R.string.connection_error),null );
        RetrofitInstance.getRetrofitInstance().getAllOrders(context).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetAllOrdersResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull GetAllOrdersResponse getAllOrdersResponse) {
                        isGetOrdersLoading.setValue(false);
                        getOrdersResponseMLD.setValue(getAllOrdersResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isGetOrdersLoading.setValue(false);
                        getOrdersResponseMLD.setValue(nullResponse);

                    }
                });
    }

}
