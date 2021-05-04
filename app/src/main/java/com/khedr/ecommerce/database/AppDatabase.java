package com.khedr.ecommerce.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;


public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase instance;

    public abstract AppDao appDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    ,AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration().build();
        }

        return instance;
    }
}
