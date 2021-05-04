package com.khedr.ecommerce.model.product.cart.post;

import com.khedr.ecommerce.model.product.ProductForGet;

public class PostCartData {
    int id;
    int quantity;
    ProductForGet product;

    public PostCartData(int id, int quantity, ProductForGet product) {
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

    public ProductForGet getProduct() {
        return product;
    }

    public void setProduct(ProductForGet product) {
        this.product = product;
    }
}
