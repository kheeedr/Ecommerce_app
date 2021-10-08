/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.address;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.Address.AddAddressResponse;
import com.khedr.ecommerce.data.model.Address.AddressData;
import com.khedr.ecommerce.data.model.Address.GetAddressesResponse;
import com.khedr.ecommerce.data.repository.AppRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class AddressViewModel extends AndroidViewModel {
    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public AddressViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    public MutableLiveData<Boolean> isGetAddressesLoading = new MutableLiveData<>();
    public MutableLiveData<GetAddressesResponse> getAddressesResponseMLD = new MutableLiveData<>();

    MutableLiveData<Boolean> isAddAddressLoading = new MutableLiveData<>();
    MutableLiveData<AddAddressResponse> addAddressResponseMLD = new MutableLiveData<>();

    MutableLiveData<Boolean> isUpdateAddressLoading = new MutableLiveData<>();
    MutableLiveData<AddAddressResponse> updateAddressResponseMLD = new MutableLiveData<>();

    MutableLiveData<Boolean> isDeleteAddressLoading = new MutableLiveData<>();
    MutableLiveData<AddAddressResponse> deleteAddressResponseMLD = new MutableLiveData<>();

    void addAddress(AddressData addressData) {
        isAddAddressLoading.setValue(true);
        AddAddressResponse nullResponse = new AddAddressResponse(false, context.getString(R.string.connection_error), null);
        appRepository.addAddress(addressData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    void updateAddress(int id, AddressData addressData) {
        isUpdateAddressLoading.setValue(true);
        AddAddressResponse nullResponse = new AddAddressResponse(false, context.getString(R.string.connection_error), null);
        appRepository.updateAddress(id, addressData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    void deleteAddress(int id) {
        isDeleteAddressLoading.setValue(true);
        AddAddressResponse nullResponse = new AddAddressResponse(false, context.getString(R.string.connection_error), null);
        appRepository.deleteAddress(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    public void getAddresses() {
        isGetAddressesLoading.setValue(true);
        GetAddressesResponse nullResponse = new GetAddressesResponse(false, context.getString(R.string.connection_error), null);
        appRepository.getAddresses().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
