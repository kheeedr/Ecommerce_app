package com.khedr.ecommerce.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.khedr.ecommerce.database.entities.ProductsEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Database(entities = {ProductsEntity.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase instance;

    public abstract AppDao appDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    , AppDatabase.class, "app_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }

        return instance;
    }

    public Completable insert(ProductsEntity product) {
        return appDao().insert(product);
    }

    public Single<List<ProductsEntity>> getRecentProducts() {
        return appDao().getRecentProducts();
    }

    public void deleteDuplicates() {
        appDao().deleteDuplicates();
    }

    public void updateProduct(int id, boolean inCart) {
        appDao().updateProduct(id, inCart);
    }

    public void removeAllRecentProductFromCart() {
        appDao().removeAllRecentProductFromCart();
    }

}
