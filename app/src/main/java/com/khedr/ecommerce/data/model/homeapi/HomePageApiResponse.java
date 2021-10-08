package com.khedr.ecommerce.data.model.homeapi;

import com.khedr.ecommerce.data.model.StatusAndMessage;

public class HomePageApiResponse extends StatusAndMessage {

    BannersAndProductsModel data;

    public HomePageApiResponse(boolean status, String message, BannersAndProductsModel data) {
        super(status, message);
        this.data = data;
    }

    public BannersAndProductsModel getData() {
        return data;
    }

    public void setData(BannersAndProductsModel data) {
        this.data = data;
    }
}
