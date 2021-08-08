package com.khedr.ecommerce.ui.activities.search;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khedr.ecommerce.R;
import com.khedr.ecommerce.network.RetrofitInstance;
import com.khedr.ecommerce.pojo.product.search.SearchRequest;
import com.khedr.ecommerce.pojo.product.search.SearchResponse;

import org.jetbrains.annotations.NotNull;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    public MutableLiveData<Boolean> isSearchLoading = new MutableLiveData<>();
    public MutableLiveData<SearchResponse> searchResponseMLD = new MutableLiveData<>();

    public void performSearch(Context context, String searchText) {
        isSearchLoading.postValue(true);
        SearchResponse nullResponse = new SearchResponse(false, context.getString(R.string.connection_error), null);
        SearchRequest searchRequest = new SearchRequest(searchText);
        RetrofitInstance.getRetrofitInstance()
                .getSearchProducts(context, searchRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SearchResponse>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull SearchResponse searchResponse) {
                        isSearchLoading.postValue(false);
                        searchResponseMLD.postValue(searchResponse);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isSearchLoading.postValue(false);
                        searchResponseMLD.postValue(nullResponse);
                    }
                });
    }
}
