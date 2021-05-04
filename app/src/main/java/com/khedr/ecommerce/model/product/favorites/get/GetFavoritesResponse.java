package com.khedr.ecommerce.model.product.favorites.get;

public class GetFavoritesResponse {
    boolean status ;
    String message;
    OuterData data;

    public GetFavoritesResponse(boolean status, String message, OuterData data) {
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

    public OuterData getData() {
        return data;
    }

    public void setData(OuterData data) {
        this.data = data;
    }
}
