package com.khedr.ecommerce.data.model.product.favorites.get;

import com.khedr.ecommerce.data.model.MultiplePagesDetails;

import java.util.ArrayList;

public class OuterData extends MultiplePagesDetails {

    ArrayList<InnerData> data;

    public OuterData(int current_page, int from, int last_page, int per_page, int to, int total,
                     String first_page_url, String last_page_url, String next_page_url, String path,
                     String prev_page_url, ArrayList<InnerData> data) {

        super(current_page, from, last_page, per_page, to, total, first_page_url, last_page_url,
                next_page_url, path, prev_page_url);

        this.data = data;
    }

    public ArrayList<InnerData> getData() {
        return data;
    }

    public void setData(ArrayList<InnerData> data) {
        this.data = data;
    }

}
