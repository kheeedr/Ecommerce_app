package com.khedr.ecommerce.data.model.product.search;

import com.khedr.ecommerce.data.model.categories.item.GetCategoryItemsData;
import com.khedr.ecommerce.data.model.categories.item.GetCategoryItemsResponse;

public class SearchResponse extends GetCategoryItemsResponse {
    public SearchResponse(boolean status, String message, GetCategoryItemsData data) {
        super(status, message, data);
    }
}
