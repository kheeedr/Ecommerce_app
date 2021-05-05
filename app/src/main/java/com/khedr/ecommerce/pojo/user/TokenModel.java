package com.khedr.ecommerce.pojo.user;

public class TokenModel {
    String fcm_token;

    public TokenModel(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }


}
