/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.address;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.Address.AddAddressResponse;
import com.khedr.ecommerce.pojo.Address.AddressData;
import com.khedr.ecommerce.pojo.Address.GetAddressesResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddressViewModel extends ViewModel {

    public MutableLiveData<Boolean> isGetAddressesLoading = new MutableLiveData<>();
    public MutableLiveData<GetAddressesResponse> getAddressesResponseMLD = new MutableLiveData<>();

    MutableLiveData<Boolean> isAddAddressLoading = new MutableLiveData<>();
    MutableLiveData<AddAddressResponse> addAddressResponseMLD = new MutableLiveData<>();

    MutableLiveData<Boolean> isUpdateAddressLoading = new MutableLiveData<>();
    MutableLiveData<AddAddressResponse> updateAddressResponseMLD = new MutableLiveData<>();

    MutableLiveData<Boolean> isDeleteAddressLoading = new MutableLiveData<>();
    MutableLiveData<AddAddressResponse> deleteAddressResponseMLD = new MutableLiveData<>();

    void addAddress(Context context, AddressData addressData) {
        isAddAddressLoading.setValue(true);
        AddAddressResponse nullResponse = new AddAddressResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().addAddress(context, addressData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AddAddressResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull AddAddressResponse addAddressResponse) {
                        isAddAddressLoading.setValue(false);
                        addAddressResponseMLD.postValue(addAddressResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isAddAddressLoading.setValue(false);
                        addAddressResponseMLD.postValue(nullResponse);

                    }
                });
    }

    void updateAddress(Context context, int id, AddressData addressData) {
        isUpdateAddressLoading.setValue(true);
        AddAddressResponse nullResponse = new AddAddressResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().updateAddress(context,id, addressData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AddAddressResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull AddAddressResponse addAddressResponse) {
                        isUpdateAddressLoading.setValue(false);
                        updateAddressResponseMLD.postValue(addAddressResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isUpdateAddressLoading.setValue(false);
                        updateAddressResponseMLD.postValue(nullResponse);

                    }
                });

    }

    void deleteAddress(Context context, int id) {
        isDeleteAddressLoading.setValue(true);
        AddAddressResponse nullResponse = new AddAddressResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().deleteAddress(context,id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AddAddressResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull AddAddressResponse addAddressResponse) {
                        isDeleteAddressLoading.setValue(false);
                        deleteAddressResponseMLD.postValue(addAddressResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isDeleteAddressLoading.setValue(false);
                        deleteAddressResponseMLD.postValue(nullResponse);

                    }
                });
    }

    public void getAddresses(Context context) {
        isGetAddressesLoading.setValue(true);
        GetAddressesResponse nullResponse = new GetAddressesResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().getAddresses(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetAddressesResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull GetAddressesResponse getAddressesResponse) {
                        getAddressesResponseMLD.setValue(getAddressesResponse);
                        isGetAddressesLoading.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getAddressesResponseMLD.setValue(nullResponse);
                        isGetAddressesLoading.setValue(false);
                    }
                });
    }

}
