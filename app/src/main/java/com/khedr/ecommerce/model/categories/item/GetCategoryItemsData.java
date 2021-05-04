package com.khedr.ecommerce.model.categories.item;

import com.khedr.ecommerce.model.MultiplePagesDetails;
import com.khedr.ecommerce.model.product.Product;

import java.util.ArrayList;

public class GetCategoryItemsData extends MultiplePagesDetails {
    ArrayList<Product> data;

    public GetCategoryItemsData(int current_page, int from, int last_page, int per_page, int to, int total, String first_page_url, String last_page_url, String next_page_url, String path, String prev_page_url, ArrayList<Product> data) {
        super(current_page, from, last_page, per_page, to, total, first_page_url, last_page_url, next_page_url, path, prev_page_url);
        this.data = data;
    }

    public ArrayList<Product> getData() {
        return data;
    }

    public void setData(ArrayList<Product> data) {
        this.data = data;
    }
}
