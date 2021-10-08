/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.addOrder;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.order.AddOrderRequest;
import com.khedr.ecommerce.data.model.order.AddOrderResponse;
import com.khedr.ecommerce.data.model.order.EstimateOrderRequest;
import com.khedr.ecommerce.data.model.order.EstimateOrderResponse;
import com.khedr.ecommerce.data.model.promocode.PromoCodeRequest;
import com.khedr.ecommerce.data.model.promocode.PromoCodeResponse;
import com.khedr.ecommerce.data.repository.AppRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class AddOrderViewModel extends AndroidViewModel {
    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public AddOrderViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    public MutableLiveData<Boolean> isAddOrderLoading = new MutableLiveData<>();
    public MutableLiveData<AddOrderResponse> addOrderResponseMLD = new MutableLiveData<>();

    public MutableLiveData<Boolean> isEstimateLoading = new MutableLiveData<>();
    public MutableLiveData<EstimateOrderResponse> estimateOrderResponseMLD = new MutableLiveData<>();

    public MutableLiveData<Boolean> isValidateLoading = new MutableLiveData<>();
    public MutableLiveData<PromoCodeResponse> validatePromoCodeResponseMLD = new MutableLiveData<>();


    public void addOrder(AddOrderRequest orderRequest) {
        isAddOrderLoading.setValue(true);
        AddOrderResponse nullResponse = new AddOrderResponse(false, context.getString(R.string.connection_error), null);
        appRepository.addOrder(orderRequest).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AddOrderResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull AddOrderResponse addOrderResponse) {
                        isAddOrderLoading.setValue(false);
                        addOrderResponseMLD.setValue(addOrderResponse);
                        if (addOrderResponse.isStatus()) {
                            appRepository.removeAllRecentProductFromCart();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isAddOrderLoading.setValue(false);
                        addOrderResponseMLD.setValue(nullResponse);
                    }
                });


    }

    public void estimateOrder( EstimateOrderRequest estimateOrderRequest) {

        isEstimateLoading.setValue(true);
        EstimateOrderResponse nullResponse = new EstimateOrderResponse(false, context.getString(R.string.connection_error), null);
        appRepository.estimateOrder(estimateOrderRequest).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<EstimateOrderResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull EstimateOrderResponse estimateOrderResponse) {
                        isEstimateLoading.setValue(false);
                        estimateOrderResponseMLD.setValue(estimateOrderResponse);
                        Log.d("medo", "\n" + estimateOrderResponse.getMessage());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isEstimateLoading.setValue(false);
                        estimateOrderResponseMLD.setValue(nullResponse);
                        Log.d("medo", e.getMessage());

                    }
                });


    }

    public void validatePromoCode( String promoCode) {
        isValidateLoading.setValue(true);
        PromoCodeResponse nullResponse = new PromoCodeResponse(false, context.getString(R.string.connection_error), null);
        PromoCodeRequest promoCodeRequest = new PromoCodeRequest(promoCode);
        appRepository.validatePromoCode(promoCodeRequest).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PromoCodeResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull PromoCodeResponse promoCodeResponse) {
                        isValidateLoading.setValue(false);
                        validatePromoCodeResponseMLD.setValue(promoCodeResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isValidateLoading.setValue(false);
                        validatePromoCodeResponseMLD.setValue(nullResponse);
                        Log.d("OrderViewModel", e.getMessage());
                    }
                });
    }

}
