/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.Address;

import com.khedr.ecommerce.pojo.StatusAndMessage;

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
