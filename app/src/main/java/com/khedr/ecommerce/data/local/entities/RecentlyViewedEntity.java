/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.data.local.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.khedr.ecommerce.data.model.product.Product;

import java.util.ArrayList;


@Entity(tableName = "recently_viewed_table")
public class RecentlyViewedEntity {
    public RecentlyViewedEntity() {
    }

    @PrimaryKey(autoGenerate = true)
    public int primaryKey;
    @Ignore
    public Product product;

    public int id;
    public double price;
    public double old_price;
    public double discount;
    public String image;
    public String name;
    public String description;
    public ArrayList<String> images;
    public boolean in_favorites;
    public boolean in_cart;

    public RecentlyViewedEntity(Product product) {
        id = product.getId();
        price = product.getPrice();
        old_price = product.getOld_price();
        discount = product.getDiscount();
        image = product.getImage();
        name = product.getName();
        description = product.getDescription();
        images = product.getImages();
        in_favorites = product.isIn_favorites();
        in_cart = product.isIn_cart();
    }

    public Product getProduct() {
        return new Product(id, price, old_price, discount, image, name, description, images, in_favorites, in_cart);
    }

}
