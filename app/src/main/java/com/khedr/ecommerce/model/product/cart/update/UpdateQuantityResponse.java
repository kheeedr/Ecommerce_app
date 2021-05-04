package com.khedr.ecommerce.model.product.cart.update;

import com.khedr.ecommerce.model.StatusAndMessage;

public class UpdateQuantityResponse extends StatusAndMessage {
    UpdateQuantityData data;

    public UpdateQuantityResponse(boolean status, String message, UpdateQuantityData data) {
        super(status, message);
        this.data = data;
    }

    public UpdateQuantityData getData() {
        return data;
    }

    public void setData(UpdateQuantityData data) {
        this.data = data;
    }
}
