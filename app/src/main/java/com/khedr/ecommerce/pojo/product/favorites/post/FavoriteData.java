package com.khedr.ecommerce.pojo.product.favorites.post;

public class FavoriteData {
    int id;
    FavoriteProduct product;

    public FavoriteData(int id, FavoriteProduct product) {
        this.id = id;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FavoriteProduct getProduct() {
        return product;
    }

    public void setProduct(FavoriteProduct product) {
        this.product = product;
    }
}
