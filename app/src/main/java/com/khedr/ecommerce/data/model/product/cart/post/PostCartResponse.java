package com.khedr.ecommerce.data.model.product.cart.post;

import com.khedr.ecommerce.data.model.StatusAndMessage;

public class PostCartResponse extends StatusAndMessage {

    PostCartData data;

    public PostCartResponse(boolean status, String message, PostCartData data) {
        super(status, message);
        this.data = data;
    }

    public PostCartData getData() {
        return data;
    }

    public void setData(PostCartData data) {
        this.data = data;
    }
}
