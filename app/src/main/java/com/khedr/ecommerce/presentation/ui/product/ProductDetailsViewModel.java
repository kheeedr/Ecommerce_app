/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.product;

import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.data.local.entities.RecentlyViewedEntity;
import com.khedr.ecommerce.data.model.product.Product;
import com.khedr.ecommerce.data.repository.AppRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class ProductDetailsViewModel extends AndroidViewModel {
    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public ProductDetailsViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;

    }

    private static final String TAG = "ProductDetailsViewModel";
    public MutableLiveData<List<Product>> recentProductsMLD = new MutableLiveData<>();
    public MutableLiveData<Product> productMLD = new MutableLiveData<>();


    public void addRecentProduct(Product product) {
        appRepository.insert(new RecentlyViewedEntity(product))
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "product added successfully");
                appRepository.deleteDuplicates();
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.d(TAG, "Error: " + e.getMessage());

            }
        });
    }

    public void getRecentProducts() {

        appRepository.getRecentProducts().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<RecentlyViewedEntity>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull List<RecentlyViewedEntity> productDBS) {
                        ArrayList<Product> products = new ArrayList<>();
                        for (RecentlyViewedEntity item : productDBS) {
                            products.add(0, item.getProduct());
                        }

                        recentProductsMLD.setValue(products);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.d(TAG, "Error: " + e.getMessage());
                    }
                });

    }

    public void getProductById(int id) {
        appRepository.getProductById(id).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<RecentlyViewedEntity>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull RecentlyViewedEntity productDBS) {
                        productMLD.setValue(productDBS.getProduct());
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.d(TAG, "Error: " + e.getMessage());
                    }
                });
    }


}
