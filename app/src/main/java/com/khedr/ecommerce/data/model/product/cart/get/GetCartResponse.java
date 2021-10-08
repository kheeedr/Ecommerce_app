package com.khedr.ecommerce.data.model.product.cart.get;

import com.khedr.ecommerce.data.model.StatusAndMessage;

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
