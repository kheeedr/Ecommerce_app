package com.khedr.ecommerce.data.model.user;

import com.khedr.ecommerce.data.model.StatusAndMessage;

public class UserApiResponse extends StatusAndMessage {

    UserDataForLoginRequest data;

    public UserApiResponse(boolean status, String message, UserDataForLoginRequest data) {
        super(status, message);
        this.data = data;
    }

    public UserDataForLoginRequest getData() {
        return data;
    }

    public void setData(UserDataForLoginRequest data) {
        this.data = data;
    }
}
