package com.khedr.ecommerce.pojo.product.search;

import com.khedr.ecommerce.pojo.categories.GetCategoriesOuterData;
import com.khedr.ecommerce.pojo.categories.GetCategoriesResponse;
import com.khedr.ecommerce.pojo.categories.item.GetCategoryItemsData;
import com.khedr.ecommerce.pojo.categories.item.GetCategoryItemsResponse;

public class SearchResponse extends GetCategoryItemsResponse {
    public SearchResponse(boolean status, String message, GetCategoryItemsData data) {
        super(status, message, data);
    }
}
