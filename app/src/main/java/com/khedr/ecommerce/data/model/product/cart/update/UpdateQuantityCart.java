package com.khedr.ecommerce.data.model.product.cart.update;

import com.khedr.ecommerce.data.model.product.favorites.post.FavoriteProduct;

public class UpdateQuantityCart {
    int id;
    int quantity;
    FavoriteProduct product;

    public UpdateQuantityCart(int id, int quantity, FavoriteProduct product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public FavoriteProduct getProduct() {
        return product;
    }

    public void setProduct(FavoriteProduct product) {
        this.product = product;
    }
}

