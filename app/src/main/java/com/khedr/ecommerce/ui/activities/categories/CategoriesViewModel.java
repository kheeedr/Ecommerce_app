/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.categories;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.categories.GetCategoriesInnerData;
import com.khedr.ecommerce.pojo.categories.GetCategoriesResponse;
import com.khedr.ecommerce.pojo.categories.item.GetCategoryItemsResponse;

import org.jetbrains.annotations.NotNull;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoriesViewModel extends ViewModel {

    MutableLiveData<Boolean> isGetCategoriesLoading = new MutableLiveData<>();
    MutableLiveData<GetCategoriesResponse> getCategoriesResponseMLD = new MutableLiveData<>();
    MutableLiveData<Boolean> isGetCategoryProductsLoading = new MutableLiveData<>();
    MutableLiveData<GetCategoryItemsResponse> getCategoryItemsResponseMLD = new MutableLiveData<>();

    public void getCategories(Context context) {

            isGetCategoriesLoading.postValue(true);
        GetCategoriesResponse nullResponse = new GetCategoriesResponse(false, context.getString(R.string.connection_error), null);

        RetrofitInstance.getRetrofitInstance().getCategories(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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


    public void getCategoryProducts(Context context, int id) {

        if (isGetCategoryProductsLoading.getValue() == null || !isGetCategoryProductsLoading.getValue()) {
            isGetCategoryProductsLoading.postValue(true);
        }
        GetCategoryItemsResponse nullResponse = new GetCategoryItemsResponse(false, context.getString(R.string.connection_error), null);
        RetrofitInstance.getRetrofitInstance().getCategoryItems(context, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    public void getCategoryProductsByName(Context context, String categoryName) {

        isGetCategoryProductsLoading.postValue(true);
        GetCategoryItemsResponse nullResponse = new GetCategoryItemsResponse(false, context.getString(R.string.connection_error), null);

        getCategories(context);
        getCategoriesResponseMLD.observe((LifecycleOwner) context, getCategoriesResponse -> {
            if (getCategoriesResponse.isStatus()) {
                for (GetCategoriesInnerData data : getCategoriesResponse.getData().getData()) {
                    if (data.getName().equals(categoryName)) {
                        getCategoryProducts(context,data.getId());
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
