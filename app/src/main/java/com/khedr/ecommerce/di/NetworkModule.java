/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */
package com.khedr.ecommerce.di;
import com.khedr.ecommerce.data.romote.ApiInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@InstallIn(SingletonComponent.class)
@Module
public class NetworkModule {
    private static final String BASE_URL = "https://student.valuxapps.com/api/";

    @Provides
    @Singleton
    ApiInterface getApiInterface() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiInterface.class);
    }

}
