package com.khedr.ecommerce.pojo.product.cart.get;

import com.khedr.ecommerce.pojo.StatusAndMessage;

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
