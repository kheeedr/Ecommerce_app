package com.khedr.ecommerce.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.khedr.ecommerce.database.entities.RecentlyViewedEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface AppDao {

    @Insert
    Completable insert(RecentlyViewedEntity product);

    @Query("select * from recently_viewed_table")
    Single<List<RecentlyViewedEntity>> getRecentProducts();

    @Query("select * from recently_viewed_table WHERE id = :id")
    Single<RecentlyViewedEntity> getProductById(int id);

    @Query("delete from recently_viewed_table" +
            " where  id  in (select id from recently_viewed_table order by primaryKey desc limit 1) " +
            " and primaryKey not in( select primaryKey from recently_viewed_table order by primaryKey desc limit 1)")
    void deleteDuplicates();

    @Query("UPDATE recently_viewed_table SET in_cart = :inCart WHERE id = :id")
    void updateProduct(int id, boolean inCart);

    @Query("UPDATE recently_viewed_table SET in_cart = 0")
    void removeAllRecentProductFromCart();

    @Query("delete from recently_viewed_table")
    void removeAllRecentProduct();




}
