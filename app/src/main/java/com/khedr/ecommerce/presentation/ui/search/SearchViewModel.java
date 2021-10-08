/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.search;


import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.R;
import com.khedr.ecommerce.data.model.product.search.SearchRequest;
import com.khedr.ecommerce.data.model.product.search.SearchResponse;
import com.khedr.ecommerce.data.repository.AppRepository;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class SearchViewModel extends AndroidViewModel {

    AppRepository appRepository;
    BaseApplication context;

    @Inject
    public SearchViewModel(BaseApplication context, AppRepository appRepository) {
        super(context);
        this.context = context;
        this.appRepository = appRepository;
    }

    public MutableLiveData<Boolean> isSearchLoading = new MutableLiveData<>();
    public MutableLiveData<SearchResponse> searchResponseMLD = new MutableLiveData<>();

    public void performSearch(String searchText) {
        isSearchLoading.postValue(true);
        SearchResponse nullResponse = new SearchResponse(false, context.getString(R.string.connection_error), null);
        SearchRequest searchRequest = new SearchRequest(searchText);
        appRepository.getSearchProducts(searchRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
