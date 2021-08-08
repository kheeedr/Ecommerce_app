package com.khedr.ecommerce.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.khedr.ecommerce.pojo.ProductDB;
import com.khedr.ecommerce.pojo.product.Product;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface AppDao {
    @Insert
    Completable insert(ProductDB product);

    @Query("select * from products_table")
    Single<List<ProductDB>> getRecentProducts();

    @Query("delete from products_table" +
            " where  id  in (select id from products_table order by primaryKey desc limit 1) " +
            " and primaryKey not in( select primaryKey from products_table order by primaryKey desc limit 1)")
    void deleteDuplicates();

}
