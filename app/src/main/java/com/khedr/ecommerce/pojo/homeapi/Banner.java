package com.khedr.ecommerce.pojo.homeapi;

import com.khedr.ecommerce.pojo.product.Product;

public class Banner {

    int id;
    String image;
    Category category;
    Product product;

    public Banner(int id, String image, Category category, Product product) {

        this.id = id;
        this.image = image;
        this.category = category;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
