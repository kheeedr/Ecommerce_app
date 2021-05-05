package com.khedr.ecommerce.pojo.homeapi;

public class HomePageApiResponse {
    boolean status;
    String message;
    BannersAndProductsModel data;

    public HomePageApiResponse(boolean status, String message, BannersAndProductsModel data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BannersAndProductsModel getData() {
        return data;
    }

    public void setData(BannersAndProductsModel data) {
        this.data = data;
    }
}
