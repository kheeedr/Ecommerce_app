package com.khedr.ecommerce.data.model.product.favorites.get;

import com.khedr.ecommerce.data.model.product.SimpleProduct;

public class InnerData {
    int id;
    SimpleProduct product;

    public InnerData(int id, SimpleProduct product) {
        this.id = id;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SimpleProduct getProduct() {
        return product;
    }

    public void setProduct(SimpleProduct product) {
        this.product = product;
    }
}
