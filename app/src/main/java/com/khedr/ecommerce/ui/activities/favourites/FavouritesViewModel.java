package com.khedr.ecommerce.ui.activities.favourites;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.product.ProductId;
import com.khedr.ecommerce.pojo.product.favorites.get.GetFavoritesResponse;
import com.khedr.ecommerce.pojo.product.favorites.post.PostFavoriteResponse;
import com.khedr.ecommerce.pojo.user.UserApiResponse;
import com.khedr.ecommerce.utils.UiUtils;
import com.khedr.ecommerce.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesViewModel extends ViewModel {

    private static final String TAG = "FavouritesViewModel";

    public MutableLiveData<PostFavoriteResponse> setFavoriteResponseBody = new MutableLiveData<>();

    public MutableLiveData<Boolean> isGetLoading = new MutableLiveData<>();

    public MutableLiveData<GetFavoritesResponse> getFavoriteResponseBody = new MutableLiveData<>();

    public void addProductToFavorite(Context context, int productId) {

        PostFavoriteResponse nullSetResponse = new PostFavoriteResponse(false, context.getString(R.string.connection_error), null);
        ProductId id = new ProductId(productId);
        String token = UserUtils.getUserToken(context);

        RetrofitInstance.getRetrofitInstance().addToFavorite(context, token, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PostFavoriteResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull PostFavoriteResponse postFavoriteResponse) {
                        Log.d(TAG, "mkhedr: favorite");
                        setFavoriteResponseBody.setValue(postFavoriteResponse);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        setFavoriteResponseBody.setValue(nullSetResponse);
                        Log.d(TAG, "mkhedr: " + e);
                    }
                });
    }

    void getFavorites(Context context) {
        isGetLoading.setValue(true);

        GetFavoritesResponse nullGetResponse = new GetFavoritesResponse(false, context.getString(R.string.connection_error), null);


            String token = UserUtils.getUserToken(context);

            RetrofitInstance.getRetrofitInstance().getFavorites(context, token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<GetFavoritesResponse>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NotNull GetFavoritesResponse getFavoritesResponse) {
                            isGetLoading.setValue(false);
                            getFavoriteResponseBody.setValue(getFavoritesResponse);

                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            isGetLoading.setValue(false);
                            getFavoriteResponseBody.setValue(nullGetResponse);

                        }
                    });


    }

}

