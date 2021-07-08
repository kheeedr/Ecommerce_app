package com.khedr.ecommerce.ui.activities.cart;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.product.ProductId;
import com.khedr.ecommerce.pojo.product.cart.delete.DeleteFromCartResponse;
import com.khedr.ecommerce.pojo.product.cart.get.GetCartItems;
import com.khedr.ecommerce.pojo.product.cart.get.GetCartResponse;
import com.khedr.ecommerce.pojo.product.cart.post.PostCartResponse;
import com.khedr.ecommerce.pojo.product.cart.update.Quantity;
import com.khedr.ecommerce.pojo.product.cart.update.UpdateQuantityResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {

    public MutableLiveData<PostCartResponse> postToCartResponseMLD = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPostLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> isPostMultipleLoading = new MutableLiveData<>();

    public MutableLiveData<GetCartResponse> getCartResponseMLD = new MutableLiveData<>();
    public MutableLiveData<Boolean> isGetLoading = new MutableLiveData<>();

    public MutableLiveData<UpdateQuantityResponse> updateQuantityResponseMLD = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdateFromCartLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> isUpdateFromProductLoading = new MutableLiveData<>();

    public MutableLiveData<UpdateQuantityResponse> deleteProductResponseMLD = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDeleteLoading = new MutableLiveData<>();

    boolean updateFromCartLoading() {
        return isUpdateFromCartLoading.getValue() != null && isUpdateFromCartLoading.getValue();
    }

    boolean updateFromProductLoading() {
        return isUpdateFromProductLoading.getValue() != null && isUpdateFromProductLoading.getValue();
    }

    boolean postMultipleLoading() {
        return isPostMultipleLoading.getValue() != null && isPostMultipleLoading.getValue();
    }

    public void addProductToCart(Context context, int productId) {
        if (!postMultipleLoading()) {
            isPostLoading.setValue(true);
        }

        PostCartResponse nullResponse = new PostCartResponse(false, context.getString(R.string.connection_error), null);

        ProductId id = new ProductId(productId);
        RetrofitInstance.getRetrofitInstance().addToCart(context, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PostCartResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull PostCartResponse postCartResponse) {
                        if (!postMultipleLoading()) {
                            isPostLoading.setValue(false);
                        }
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

    public void addMultipleProductsToCart(Context context, int productQuantity, int productId) {
        isPostMultipleLoading.setValue(true);

        addProductToCart(context, productId);
        postToCartResponseMLD.observe((LifecycleOwner) context, postCartResponse -> {
            if (postCartResponse.isStatus()) {
                updateQuantityFromCart(context, productQuantity - 1, postCartResponse.getData().getId());
            }
        });

    }

    public void updateQuantityFromCart(Context context, int newValue, int productId) {
        if (!updateFromProductLoading() && !postMultipleLoading()) {
            isUpdateFromCartLoading.setValue(true);
        }
        UpdateQuantityResponse nullResponse = new UpdateQuantityResponse(false, context.getString(R.string.connection_error), null);
        Quantity quantity = new Quantity(newValue);

        RetrofitInstance.getRetrofitInstance().updateQuantity(context, productId, quantity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    public void updateQuantityFromProductDetails(Context context, int newValue, int productId) {

        isUpdateFromProductLoading.setValue(true);

        getCartProducts(context);
        getCartResponseMLD.observe((LifecycleOwner) context, getCartResponse -> {
            if (getCartResponse.isStatus()) {
                ArrayList<GetCartItems> cartItems = getCartResponse.getData().getCart_items();

                for (GetCartItems item : cartItems) {
                    if (item.getProduct().getId() == productId) {
                        int cartId = item.getId();
                        int oldValue = item.getQuantity();
                        int newQuantity = newValue + oldValue;
                        updateQuantityFromCart(context, newQuantity, cartId);
                        break;
                    }
                }
            }

        });


    }

    public void getCartProducts(Context context) {

        isGetLoading.setValue(true);
        GetCartResponse nullResponse = new GetCartResponse(false, context.getString(R.string.connection_error), null);

        RetrofitInstance.getRetrofitInstance().getCart(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    public void deleteCartProduct(Context context, int productId) {
        isDeleteLoading.setValue(true);
        DeleteFromCartResponse nullResponse = new DeleteFromCartResponse(false, context.getString(R.string.connection_error), null);

        RetrofitInstance.getRetrofitInstance().deleteFromCart(context, productId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DeleteFromCartResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull DeleteFromCartResponse deleteFromCartResponse) {
                        isDeleteLoading.setValue(false);
                        deleteProductResponseMLD.setValue(deleteFromCartResponse);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isDeleteLoading.setValue(false);
                        deleteProductResponseMLD.setValue(nullResponse);
                    }
                });
    }

}



