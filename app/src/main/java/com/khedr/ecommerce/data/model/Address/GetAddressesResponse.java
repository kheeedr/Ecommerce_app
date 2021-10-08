/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.data.model.Address;

import com.khedr.ecommerce.data.model.StatusAndMessage;

public class GetAddressesResponse extends StatusAndMessage {

    GetAddressResponseData data;

    public GetAddressesResponse(boolean status, String message, GetAddressResponseData data) {
        super(status, message);
        this.data = data;
    }

    public GetAddressResponseData getData() {
        return data;
    }

    public void setData(GetAddressResponseData data) {
        this.data = data;
    }
}
