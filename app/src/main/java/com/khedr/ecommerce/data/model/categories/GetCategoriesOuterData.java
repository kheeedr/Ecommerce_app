package com.khedr.ecommerce.data.model.categories;

import com.khedr.ecommerce.data.model.MultiplePagesDetails;

import java.util.ArrayList;

public class GetCategoriesOuterData extends MultiplePagesDetails {

    ArrayList<GetCategoriesInnerData> data;

    public GetCategoriesOuterData(int current_page, int from, int last_page, int per_page, int to, int total, String first_page_url, String last_page_url, String next_page_url, String path, String prev_page_url, ArrayList<GetCategoriesInnerData> data) {
        super(current_page, from, last_page, per_page, to, total, first_page_url, last_page_url, next_page_url, path, prev_page_url);
        this.data = data;
    }

    public ArrayList<GetCategoriesInnerData> getData() {
        return data;
    }

    public void setData(ArrayList<GetCategoriesInnerData> data) {
        this.data = data;
    }
}
