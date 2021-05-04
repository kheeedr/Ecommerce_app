package com.khedr.ecommerce.model.categories;

import com.khedr.ecommerce.model.StatusAndMessage;

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
