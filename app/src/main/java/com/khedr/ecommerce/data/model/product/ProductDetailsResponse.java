/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.data.model.product;

import com.khedr.ecommerce.data.model.StatusAndMessage;

public class ProductDetailsResponse extends StatusAndMessage {

    Product data;

    public Product getData() {
        return data;
    }

    public void setData(Product data) {
        this.data = data;
    }

    public ProductDetailsResponse(boolean status, String message, Product data) {
        super(status, message);
        this.data = data;
    }
}
