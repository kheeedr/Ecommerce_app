package com.khedr.ecommerce.di;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.data.local.AppDao;
import com.khedr.ecommerce.data.repository.AppRepositoryImpl;
import com.khedr.ecommerce.data.romote.ApiInterface;
import com.khedr.ecommerce.data.repository.AppRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class RepositoryModule {

    @Provides
    @Singleton
    AppRepository getAppRepository(BaseApplication context, ApiInterface apiInterface, AppDao appDao) {
        return new AppRepositoryImpl(context, apiInterface, appDao);
    }

}
