/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.categories;


import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.categories.GetCategoriesInnerData;
import com.khedr.ecommerce.data.model.categories.GetCategoriesResponse;
import com.khedr.ecommerce.data.model.categories.item.GetCategoryItemsResponse;
import com.khedr.ecommerce.data.repository.AppRepository;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class CategoriesViewModel extends AndroidViewModel {
    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public CategoriesViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    MutableLiveData<Boolean> isGetCategoriesLoading = new MutableLiveData<>();
    MutableLiveData<GetCategoriesResponse> getCategoriesResponseMLD = new MutableLiveData<>();
    MutableLiveData<Boolean> isGetCategoryProductsLoading = new MutableLiveData<>();
    MutableLiveData<GetCategoryItemsResponse> getCategoryItemsResponseMLD = new MutableLiveData<>();

    public void getCategories() {

        isGetCategoriesLoading.postValue(true);
        GetCategoriesResponse nullResponse = new GetCategoriesResponse(false, context.getString(R.string.connection_error), null);

        appRepository.getCategories().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetCategoriesResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull GetCategoriesResponse getCategoriesResponse) {
                        isGetCategoriesLoading.postValue(false);
                        getCategoriesResponseMLD.setValue(getCategoriesResponse);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isGetCategoriesLoading.postValue(false);
                        getCategoriesResponseMLD.setValue(nullResponse);
                    }
                });

    }


    public void getCategoryProducts(int id) {

        if (isGetCategoryProductsLoading.getValue() == null || !isGetCategoryProductsLoading.getValue()) {
            isGetCategoryProductsLoading.postValue(true);
        }
        GetCategoryItemsResponse nullResponse = new GetCategoryItemsResponse(false, context.getString(R.string.connection_error), null);
        appRepository.getCategoryItems(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetCategoryItemsResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull GetCategoryItemsResponse getCategoryItemsResponse) {
                        isGetCategoryProductsLoading.postValue(false);
                        getCategoryItemsResponseMLD.postValue(getCategoryItemsResponse);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isGetCategoryProductsLoading.postValue(false);
                        getCategoryItemsResponseMLD.postValue(nullResponse);
                    }
                });
    }

    public void getCategoryProductsByName(String categoryName) {

        isGetCategoryProductsLoading.postValue(true);
        GetCategoryItemsResponse nullResponse = new GetCategoryItemsResponse(false, context.getString(R.string.connection_error), null);

        getCategories();
        getCategoriesResponseMLD.observe((LifecycleOwner) context, getCategoriesResponse -> {
            if (getCategoriesResponse.isStatus()) {
                for (GetCategoriesInnerData data : getCategoriesResponse.getData().getData()) {
                    if (data.getName().equals(categoryName)) {
                        getCategoryProducts(data.getId());
                        break;
                    }
                }
            } else {
                isGetCategoryProductsLoading.postValue(false);
                getCategoryItemsResponseMLD.postValue(nullResponse);
            }
        });

    }


}
