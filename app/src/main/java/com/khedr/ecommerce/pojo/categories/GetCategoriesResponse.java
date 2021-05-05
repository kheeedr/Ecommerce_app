package com.khedr.ecommerce.pojo.categories;

import com.khedr.ecommerce.pojo.StatusAndMessage;

public class GetCategoriesResponse extends StatusAndMessage {

    GetCategoriesOuterData data;

    public GetCategoriesResponse(boolean status, String message, GetCategoriesOuterData data) {
        super(status, message);
        this.data = data;
    }

    public GetCategoriesOuterData getData() {
        return data;
    }

    public void setData(GetCategoriesOuterData data) {
        this.data = data;
    }
}
