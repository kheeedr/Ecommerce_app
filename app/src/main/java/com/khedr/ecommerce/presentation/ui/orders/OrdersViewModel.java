/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.orders;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.order.GetAllOrdersResponse;
import com.khedr.ecommerce.data.repository.AppRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class OrdersViewModel extends AndroidViewModel {
    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public OrdersViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    public MutableLiveData<Boolean> isGetOrdersLoading = new MutableLiveData<>();
    public MutableLiveData<GetAllOrdersResponse> getOrdersResponseMLD = new MutableLiveData<>();

    public void getOrders() {
        isGetOrdersLoading.setValue(true);
        GetAllOrdersResponse nullResponse=new GetAllOrdersResponse(false, context.getString(R.string.connection_error),null );
        appRepository.getAllOrders().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
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
