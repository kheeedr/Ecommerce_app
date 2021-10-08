package com.khedr.ecommerce.data.model.product.cart.get;

import com.khedr.ecommerce.data.model.product.Product;

public class GetCartItems {

    int id;
    int quantity;
    Product product;

    public GetCartItems(int id, int quantity, Product product) {
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
