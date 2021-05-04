package com.khedr.ecommerce.model.product.cart.get;

import com.khedr.ecommerce.model.StatusAndMessage;

public class GetCartResponse extends StatusAndMessage {
    GetCartData data;

    public GetCartResponse(boolean status, String message, GetCartData data) {
        super(status, message);
        this.data = data;
    }

    public GetCartData getData() {
        return data;
    }

    public void setData(GetCartData data) {
        this.data = data;
    }
}
