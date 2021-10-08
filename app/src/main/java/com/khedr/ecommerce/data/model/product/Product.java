package com.khedr.ecommerce.data.model.product;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    int id;
    double price;
    double old_price;
    double discount;
    String image;
    String name;
    String description;
    ArrayList<String> images;
    boolean in_favorites;
    boolean in_cart;

    public Product(int id, double price, double old_price, double discount, String image, String name, String description, ArrayList<String> images, boolean in_favorites, boolean in_cart) {
        this.id = id;
        this.price = price;
        this.old_price = old_price;
        this.discount = discount;
        this.image = image;
        this.name = name;
        this.description = description;
        this.images = images;
        this.in_favorites = in_favorites;
        this.in_cart = in_cart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOld_price() {
        return old_price;
    }

    public void setOld_price(double old_price) {
        this.old_price = old_price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public boolean isIn_favorites() {
        return in_favorites;
    }

    public void setIn_favorites(boolean in_favorites) {
        this.in_favorites = in_favorites;
    }

    public boolean isIn_cart() {
        return in_cart;
    }

    public void setIn_cart(boolean in_cart) {
        this.in_cart = in_cart;
    }
}
