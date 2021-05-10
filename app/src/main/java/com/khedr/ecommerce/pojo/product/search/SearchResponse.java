package com.khedr.ecommerce.pojo.product.search;

import com.khedr.ecommerce.pojo.categories.GetCategoriesOuterData;
import com.khedr.ecommerce.pojo.categories.GetCategoriesResponse;

public class SearchResponse extends GetCategoriesResponse {
    public SearchResponse(boolean status, String message, GetCategoriesOuterData data) {
        super(status, message, data);
    }
}
