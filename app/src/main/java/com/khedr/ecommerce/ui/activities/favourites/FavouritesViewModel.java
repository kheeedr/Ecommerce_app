package com.khedr.ecommerce.ui.activities.favourites;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.product.Product;
import com.khedr.ecommerce.pojo.product.ProductDetailsResponse;
import com.khedr.ecommerce.pojo.product.ProductId;
import com.khedr.ecommerce.pojo.product.favorites.get.GetFavoritesResponse;
import com.khedr.ecommerce.pojo.product.favorites.get.InnerData;
import com.khedr.ecommerce.pojo.product.favorites.get.OuterData;
import com.khedr.ecommerce.pojo.product.favorites.post.PostFavoriteResponse;
import com.khedr.ecommerce.ui.activities.product.ProductDetailsActivity;
import com.khedr.ecommerce.ui.activities.splash.SplashActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavouritesViewModel extends ViewModel {

    private static final String TAG = "FavouritesViewModel";

    public MutableLiveData<PostFavoriteResponse> setFavoriteResponseMTL = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSetFavoriteLoading = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Product>> getFavoriteResponseBody = new MutableLiveData<>();
    public MutableLiveData<Boolean> isGetFavoriteLoading = new MutableLiveData<>();
    int pending = 0;

    ArrayList<Product> favouritesProducts;

    public void addProductToFavorite(Context context, int productId) {
        isSetFavoriteLoading.setValue(true);
        PostFavoriteResponse nullSetResponse = new PostFavoriteResponse(false, context.getString(R.string.connection_error), null);
        ProductId id = new ProductId(productId);

        RetrofitInstance.getRetrofitInstance().addToFavorite(context, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    void getFavorites(Context context) {
        isGetFavoriteLoading.setValue(true);

        GetFavoritesResponse nullGetResponse = new GetFavoritesResponse(false, context.getString(R.string.connection_error), null);


        RetrofitInstance.getRetrofitInstance().getFavorites(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetFavoritesResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull GetFavoritesResponse getFavoritesResponse) {
                        favouritesProducts = new ArrayList<>();
                        for (InnerData data : getFavoritesResponse.getData().getData()) {
                            int id = data.getProduct().getId();
                            for (int i = 0; i < SplashActivity.homeResponse.getProducts().size(); i++) {
                                Product product = SplashActivity.homeResponse.getProducts().get(i);
                                if (product.getId() == id) {
                                    favouritesProducts.add(product);
                                    break;
                                }
                                if (i == SplashActivity.homeResponse.getProducts().size() - 1) {
                                    pending++;
                                    getProductDetails(context, id);

                                }
                            }
                        }
                        if (pending == 0) {
                            isGetFavoriteLoading.setValue(false);
                            Collections.reverse(favouritesProducts);
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

    public void getProductDetails(Context context, int id) {

        RetrofitInstance.getRetrofitInstance().getProductDetails(context, id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ProductDetailsResponse>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onSuccess(@NotNull ProductDetailsResponse productDetailsResponse) {
                favouritesProducts.add(productDetailsResponse.getData());
                pending--;

                if (pending == 0) {
                    isGetFavoriteLoading.setValue(false);
                    Collections.reverse(favouritesProducts);
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

