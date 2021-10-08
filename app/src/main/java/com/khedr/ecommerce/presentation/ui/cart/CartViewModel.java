/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.cart;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.product.ProductId;
import com.khedr.ecommerce.data.model.product.cart.delete.DeleteFromCartResponse;
import com.khedr.ecommerce.data.model.product.cart.get.GetCartItems;
import com.khedr.ecommerce.data.model.product.cart.get.GetCartResponse;
import com.khedr.ecommerce.data.model.product.cart.post.PostCartResponse;
import com.khedr.ecommerce.data.model.product.cart.update.Quantity;
import com.khedr.ecommerce.data.model.product.cart.update.UpdateQuantityResponse;
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
public class CartViewModel extends AndroidViewModel {
    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public CartViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    public MutableLiveData<PostCartResponse> postToCartResponseMLD = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPostLoading = new MutableLiveData<>();

    public void addProductToCart(int productId) {

        if (!postMultipleLoading()) {
            isPostLoading.setValue(true);
        }

        PostCartResponse nullResponse = new PostCartResponse(false, context.getString(R.string.connection_error), null);

        ProductId id = new ProductId(productId);
        appRepository.addToCart(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PostCartResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull PostCartResponse postCartResponse) {
                        if (!postMultipleLoading()) {
                            isPostLoading.setValue(false);
                        }
                        appRepository.updateProduct(productId, true);
                        postToCartResponseMLD.setValue(postCartResponse);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        if (postMultipleLoading()) {
                            isPostMultipleLoading.setValue(false);
                        } else {
                            isPostLoading.setValue(false);
                        }

                        postToCartResponseMLD.setValue(nullResponse);

                    }
                });

    }


    Observer<PostCartResponse> postToCartResponseObserver;
    public MutableLiveData<Boolean> isPostMultipleLoading = new MutableLiveData<>();

    boolean postMultipleLoading() {
        return isPostMultipleLoading.getValue() != null && isPostMultipleLoading.getValue();
    }

    public void addMultipleProductsToCart(int productQuantity, int productId) {
        isPostMultipleLoading.setValue(true);

        addProductToCart(productId);
        postToCartResponseObserver = postCartResponse -> {
            if (postCartResponse.isStatus()) {
                CartViewModel.this.updateQuantityFromCart(productQuantity, postCartResponse.getData().getId());
            }
        };
        postToCartResponseMLD.observeForever(postToCartResponseObserver);
    }

    public MutableLiveData<UpdateQuantityResponse> updateQuantityResponseMLD = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdateFromCartLoading = new MutableLiveData<>();
    boolean updateFromCartLoading() {
        return isUpdateFromCartLoading.getValue() != null && isUpdateFromCartLoading.getValue();
    }

    public void updateQuantityFromCart(int newValue, int productId) {
        if (!updateFromProductLoading() && !postMultipleLoading()) {
            isUpdateFromCartLoading.setValue(true);
        }
        UpdateQuantityResponse nullResponse = new UpdateQuantityResponse(false, context.getString(R.string.connection_error), null);
        Quantity quantity = new Quantity(newValue);

        appRepository.updateQuantity(productId, quantity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UpdateQuantityResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull UpdateQuantityResponse updateQuantityResponse) {
                        if (updateFromCartLoading()) {
                            isUpdateFromCartLoading.setValue(false);
                        } else if (updateFromProductLoading()) {
                            isUpdateFromProductLoading.setValue(false);
                        } else if (postMultipleLoading()) {
                            isPostMultipleLoading.setValue(false);
                        }
                        updateQuantityResponseMLD.setValue(updateQuantityResponse);

                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                        if (updateFromCartLoading()) {
                            isUpdateFromCartLoading.setValue(false);
                        } else if (updateFromProductLoading()) {
                            isUpdateFromProductLoading.setValue(false);
                        } else if (postMultipleLoading()) {
                            isPostMultipleLoading.setValue(false);
                        }
                        updateQuantityResponseMLD.setValue(nullResponse);

                    }
                });
    }

    Observer<GetCartResponse> getCartResponseObserver;
    public MutableLiveData<Boolean> isUpdateFromProductLoading = new MutableLiveData<>();

    boolean updateFromProductLoading() {
        return isUpdateFromProductLoading.getValue() != null && isUpdateFromProductLoading.getValue();
    }
    public void updateQuantityFromProductDetails(int newValue, int productId) {

        isUpdateFromProductLoading.setValue(true);

        getCartProducts();
        getCartResponseObserver = getCartResponse -> {
            if (getCartResponse.isStatus()) {
                ArrayList<GetCartItems> cartItems = getCartResponse.getData().getCart_items();

                for (GetCartItems item : cartItems) {
                    if (item.getProduct().getId() == productId) {
                        int cartId = item.getId();
                        int oldValue = item.getQuantity();
                        int newQuantity = newValue + oldValue;
                        updateQuantityFromCart(newQuantity, cartId);
                        break;
                    }
                }
            }

        };
        getCartResponseMLD.observeForever(getCartResponseObserver);


    }

    public MutableLiveData<GetCartResponse> getCartResponseMLD = new MutableLiveData<>();
    public MutableLiveData<Boolean> isGetLoading = new MutableLiveData<>();

    public void getCartProducts() {

        isGetLoading.setValue(true);
        GetCartResponse nullResponse = new GetCartResponse(false, context.getString(R.string.connection_error), null);

        appRepository.getCart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetCartResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull GetCartResponse getCartResponse) {
                        isGetLoading.setValue(false);
                        getCartResponseMLD.setValue(getCartResponse);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isGetLoading.setValue(false);
                        getCartResponseMLD.setValue(nullResponse);

                    }
                });
    }

    public MutableLiveData<UpdateQuantityResponse> deleteProductResponseMLD = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDeleteLoading = new MutableLiveData<>();

    public void deleteCartProduct(int productId) {
        isDeleteLoading.setValue(true);
        DeleteFromCartResponse nullResponse = new DeleteFromCartResponse(false, context.getString(R.string.connection_error), null);

        appRepository.deleteFromCart(productId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DeleteFromCartResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull DeleteFromCartResponse deleteFromCartResponse) {
                        isDeleteLoading.setValue(false);
                        appRepository.updateProduct(deleteFromCartResponse.getData().getCart().getProduct().getId(), false);
                        deleteProductResponseMLD.setValue(deleteFromCartResponse);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isDeleteLoading.setValue(false);
                        deleteProductResponseMLD.setValue(nullResponse);
                    }
                });
    }

    @Override
    protected void onCleared() {
        postToCartResponseMLD.removeObserver(postToCartResponseObserver);
        getCartResponseMLD.removeObserver(getCartResponseObserver);
        super.onCleared();
    }
}



