/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.order;

import com.khedr.ecommerce.pojo.StatusAndMessage;

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
