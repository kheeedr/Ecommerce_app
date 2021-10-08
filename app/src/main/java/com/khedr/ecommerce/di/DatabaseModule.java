/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.di;

import androidx.room.Room;

import com.khedr.ecommerce.BaseApplication;
import com.khedr.ecommerce.data.local.AppDao;
import com.khedr.ecommerce.data.local.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public static AppDatabase provideDB(BaseApplication context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                "app_database"
        )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static AppDao provideDao(AppDatabase appDatabase) {
        return appDatabase.appDao();
    }

}
