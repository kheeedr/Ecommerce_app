package com.khedr.ecommerce.model.product.favorites.post;


public class FavoriteProduct {

    int id;
    double price;
    double old_price;
    double discount;
    String image;

    public FavoriteProduct(int id, double price, double old_price, double discount, String image) {
        this.id = id;
        this.price = price;
        this.old_price = old_price;
        this.discount = discount;
        this.image = image;
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

}
