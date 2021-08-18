/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.order;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.database.AppDatabase;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.order.AddOrderRequest;
import com.khedr.ecommerce.pojo.order.AddOrderResponse;
import com.khedr.ecommerce.pojo.order.EstimateOrderRequest;
import com.khedr.ecommerce.pojo.order.EstimateOrderResponse;
import com.khedr.ecommerce.pojo.promocode.PromoCodeRequest;
import com.khedr.ecommerce.pojo.promocode.PromoCodeResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderViewModel extends ViewModel {

    public MutableLiveData<Boolean> isAddOrderLoading = new MutableLiveData<>();
    public MutableLiveData<AddOrderResponse> addOrderResponseMLD = new MutableLiveData<>();

    public MutableLiveData<Boolean> isEstimateLoading = new MutableLiveData<>();
    public MutableLiveData<EstimateOrderResponse> estimateOrderResponseMLD = new MutableLiveData<>();

    public MutableLiveData<Boolean> isValidateLoading = new MutableLiveData<>();
    public MutableLiveData<PromoCodeResponse> validatePromoCodeResponseMLD = new MutableLiveData<>();


    public void addOrder(Context context, AddOrderRequest orderRequest) {
        isAddOrderLoading.setValue(true);
        AddOrderResponse nullResponse = new AddOrderResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().addOrder(context, orderRequest).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AddOrderResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull AddOrderResponse addOrderResponse) {
                        isAddOrderLoading.setValue(false);
                        addOrderResponseMLD.setValue(addOrderResponse);
                        if (addOrderResponse.isStatus()) {
                            AppDatabase.getInstance(context).removeAllRecentProductFromCart();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isAddOrderLoading.setValue(false);
                        addOrderResponseMLD.setValue(nullResponse);
                    }
                });


    }

    public void estimateOrder(Context context, EstimateOrderRequest estimateOrderRequest) {

        isEstimateLoading.setValue(true);
        EstimateOrderResponse nullResponse = new EstimateOrderResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().estimateOrder(context, estimateOrderRequest).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<EstimateOrderResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull EstimateOrderResponse estimateOrderResponse) {
                        isEstimateLoading.setValue(false);
                        estimateOrderResponseMLD.setValue(estimateOrderResponse);
                        Log.d("medo","\n"+estimateOrderResponse.getMessage());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isEstimateLoading.setValue(false);
                        estimateOrderResponseMLD.setValue(nullResponse);
                        Log.d("medo",e.getMessage());

                    }
                });


    }

    public void validatePromoCode(Context context,String promoCode){
        isValidateLoading.setValue(true);
        PromoCodeResponse nullResponse =new PromoCodeResponse(false, context.getString(R.string.connection_error),null );
        PromoCodeRequest promoCodeRequest =new PromoCodeRequest(promoCode);
        RetrofitInstance.getRetrofitInstance().validatePromoCode(context, promoCodeRequest).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
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
                        Log.d("OrderViewModel",e.getMessage());
                    }
                });
    }

}
