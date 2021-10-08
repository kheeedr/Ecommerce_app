package com.khedr.ecommerce.data.model.product.favorites.post;

public class PostFavoriteResponse {

    boolean status;
    String message;
    FavoriteData data;

    public PostFavoriteResponse(boolean status, String message, FavoriteData data) {
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

    public FavoriteData getData() {
        return data;
    }

    public void setData(FavoriteData data) {
        this.data = data;
    }
}
