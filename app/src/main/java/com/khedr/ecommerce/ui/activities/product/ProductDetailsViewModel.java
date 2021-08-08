/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.ui.activities.product;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.database.AppDatabase;
import com.khedr.ecommerce.pojo.ProductDB;
import com.khedr.ecommerce.pojo.product.Product;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailsViewModel extends ViewModel {

    private static final String TAG = "ProductDetailsViewModel";
    public MutableLiveData<List<Product>> recentProductsMTL =new MutableLiveData<>();



    public void addRecentProduct(Context context, Product product) {
        AppDatabase.getInstance(context).insert(new ProductDB(product))
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG,"product added successfully");
                AppDatabase.getInstance(context).deleteDuplicates();
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.d(TAG,"Error: "+e.getMessage());

            }
        });
    }

    public void getRecentProducts(Context context){

        AppDatabase.getInstance(context).getRecentProducts().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ProductDB>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull List<ProductDB> productDBS) {
                        ArrayList<Product> products=new ArrayList<>();
                        for (ProductDB item:productDBS){
                            products.add(0,item.getProduct());
                        }

                        recentProductsMTL.setValue(products);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                    Log.d(TAG,"Error: "+e.getMessage());
                    }
                });

    }

}
