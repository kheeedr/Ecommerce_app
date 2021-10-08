package com.khedr.ecommerce.data.model.categories.item;

import com.khedr.ecommerce.data.model.StatusAndMessage;

public class GetCategoryItemsResponse extends StatusAndMessage {
    GetCategoryItemsData data;

    public GetCategoryItemsResponse(boolean status, String message, GetCategoryItemsData data) {
        super(status, message);
        this.data = data;
    }

    public GetCategoryItemsData getData() {
        return data;
    }

    public void setData(GetCategoryItemsData data) {
        this.data = data;
    }
}
