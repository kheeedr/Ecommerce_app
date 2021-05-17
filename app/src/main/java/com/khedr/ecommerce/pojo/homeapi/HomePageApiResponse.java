package com.khedr.ecommerce.pojo.homeapi;

import com.khedr.ecommerce.pojo.StatusAndMessage;

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
