/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.data.model.Address;

import com.khedr.ecommerce.data.model.StatusAndMessage;

public class AddAddressResponse extends StatusAndMessage {
    GetAddressData data;

    public AddAddressResponse(boolean status, String message, GetAddressData data) {
        super(status, message);
        this.data = data;
    }

    public GetAddressData getData() {
        return data;
    }

    public void setData(GetAddressData data) {
        this.data = data;
    }
}
