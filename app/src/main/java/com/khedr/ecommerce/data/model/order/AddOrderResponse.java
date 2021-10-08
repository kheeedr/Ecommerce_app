/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.data.model.order;

import com.khedr.ecommerce.data.model.StatusAndMessage;

public class AddOrderResponse extends StatusAndMessage {
    AddOrderResponseData data;

    public AddOrderResponseData getData() {
        return data;
    }

    public void setData(AddOrderResponseData data) {
        this.data = data;
    }

    public AddOrderResponse(boolean status, String message, AddOrderResponseData data) {
        super(status, message);
        this.data = data;
    }
}
