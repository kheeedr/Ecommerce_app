package com.khedr.ecommerce.model.product;

import com.khedr.ecommerce.model.product.favorites.post.FavoriteProduct;

public class ProductForGet extends FavoriteProduct {
    String name;
    String description;
    public ProductForGet(int id, double price, double old_price, double discount, String image, String name, String description) {
        super(id, price, old_price, discount, image);
        this.name = name;
        this.description = description;
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
}
