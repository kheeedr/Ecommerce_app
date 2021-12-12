/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.favourites;

import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.product.Product;
import com.khedr.ecommerce.data.model.product.ProductDetailsResponse;
import com.khedr.ecommerce.data.model.product.ProductId;
import com.khedr.ecommerce.data.model.product.favorites.get.GetFavoritesResponse;
import com.khedr.ecommerce.data.model.product.favorites.get.InnerData;
import com.khedr.ecommerce.data.model.product.favorites.post.PostFavoriteResponse;
import com.khedr.ecommerce.data.repository.AppRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class FavouritesViewModel extends AndroidViewModel {
    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public FavouritesViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    private static final String TAG = "FavouritesViewModel";

    public MutableLiveData<PostFavoriteResponse> setFavoriteResponseMTL = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSetFavoriteLoading = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Product>> getFavoriteResponseBody = new MutableLiveData<>();
    public MutableLiveData<Boolean> isGetFavoriteLoading = new MutableLiveData<>();
    int pending = 0;

    ArrayList<Product> favouritesProducts;

    public void addProductToFavorite(int productId) {
        isSetFavoriteLoading.setValue(true);
        PostFavoriteResponse nullSetResponse = new PostFavoriteResponse(false, context.getString(R.string.connection_error), null);
        ProductId id = new ProductId(productId);

        appRepository.addToFavorite(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PostFavoriteResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull PostFavoriteResponse postFavoriteResponse) {
                        Log.d(TAG, "mkhedr: favorite");
                        isSetFavoriteLoading.setValue(false);
                        setFavoriteResponseMTL.setValue(postFavoriteResponse);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isSetFavoriteLoading.setValue(false);
                        setFavoriteResponseMTL.setValue(nullSetResponse);
                        Log.d(TAG, "mkhedr: " + e);
                    }
                });
    }

    void getFavorites() {
        isGetFavoriteLoading.setValue(true);

        appRepository.getFavorites().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetFavoritesResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull GetFavoritesResponse getFavoritesResponse) {
                        favouritesProducts = new ArrayList<>();
                        for (InnerData data : getFavoritesResponse.getData().getData()) {
                            if (data != null) {
                                pending++;
                                int id = data.getProduct().getId();
                                getProductDetails(id);
                            }
                        }

                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isGetFavoriteLoading.setValue(false);
                        getFavoriteResponseBody.setValue(null);

                    }
                });

    }

    public void getProductDetails(int id) {

        appRepository.getProductDetails(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ProductDetailsResponse>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onSuccess(@NotNull ProductDetailsResponse productDetailsResponse) {
                if (productDetailsResponse.isStatus()) {
                    favouritesProducts.add(productDetailsResponse.getData());
                }
                pending--;

                if (pending == 0) {
                    isGetFavoriteLoading.setValue(false);

                    for (int i = 0; i < favouritesProducts.size() - 1; i++) {

                        for (int j = 0; j < favouritesProducts.size() - i - 1; j++) {

                            if (favouritesProducts.get(j).getId() > favouritesProducts.get(j + 1).getId()) {
                                Product temp = favouritesProducts.get(j);
                                favouritesProducts.set(j, favouritesProducts.get(j + 1));
                                favouritesProducts.set(j + 1, temp);
                            }

                        }
                    }
                    getFavoriteResponseBody.setValue(favouritesProducts);


                }
            }

            @Override
            public void onError(@NotNull Throwable e) {
                isGetFavoriteLoading.setValue(false);
                getFavoriteResponseBody.setValue(null);
            }
        });
    }

}

