package com.khedr.ecommerce.pojo.product.cart.post;

import com.khedr.ecommerce.pojo.product.SimpleProduct;

public class PostCartData {
    int id;
    int quantity;
    SimpleProduct product;

    public PostCartData(int id, int quantity, SimpleProduct product) {
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

    public SimpleProduct getProduct() {
        return product;
    }

    public void setProduct(SimpleProduct product) {
        this.product = product;
    }
}
