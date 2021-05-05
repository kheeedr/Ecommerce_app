package com.khedr.ecommerce.pojo.product.favorites.get;

import com.khedr.ecommerce.pojo.product.ProductForGet;

public class InnerData {
    int id;
    ProductForGet product;

    public InnerData(int id, ProductForGet product) {
        this.id = id;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductForGet getProduct() {
        return product;
    }

    public void setProduct(ProductForGet product) {
        this.product = product;
    }
}
