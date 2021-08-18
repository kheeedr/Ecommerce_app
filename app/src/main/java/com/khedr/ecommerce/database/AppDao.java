package com.khedr.ecommerce.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.khedr.ecommerce.database.entities.ProductsEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface AppDao {
    @Insert
    Completable insert(ProductsEntity product);

    @Query("select * from products_table")
    Single<List<ProductsEntity>> getRecentProducts();

    @Query("delete from products_table" +
            " where  id  in (select id from products_table order by primaryKey desc limit 1) " +
            " and primaryKey not in( select primaryKey from products_table order by primaryKey desc limit 1)")
    void deleteDuplicates();

    @Query("UPDATE products_table SET in_cart = :inCart WHERE id = :id")
    void updateProduct(int id, boolean inCart);

    @Query("UPDATE products_table SET in_cart = 0")
    void removeAllRecentProductFromCart();





}
