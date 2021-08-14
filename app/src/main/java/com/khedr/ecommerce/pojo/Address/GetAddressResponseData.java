/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.Address;

import com.khedr.ecommerce.pojo.MultiplePagesDetails;

import java.util.List;

public class GetAddressResponseData extends MultiplePagesDetails {

    List<GetAddressData>  data;

    public GetAddressResponseData(int current_page, int from, int last_page, int per_page, int to, int total, String first_page_url, String last_page_url, String next_page_url, String path, String prev_page_url, List<GetAddressData> data) {
        super(current_page, from, last_page, per_page, to, total, first_page_url, last_page_url, next_page_url, path, prev_page_url);
        this.data = data;
    }

    public List<GetAddressData> getData() {
        return data;
    }

    public void setData(List<GetAddressData> data) {
        this.data = data;
    }
}
