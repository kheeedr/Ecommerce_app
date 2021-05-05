package com.khedr.ecommerce.pojo.user;

public class UserApiResponse {
    boolean status;
    String message;
    UserDataForLoginRequest data;

    public UserApiResponse(boolean status, String message, UserDataForLoginRequest data) {
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

    public UserDataForLoginRequest getData() {
        return data;
    }

    public void setData(UserDataForLoginRequest data) {
        this.data = data;
    }
}
