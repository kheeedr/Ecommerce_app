/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.order;

import com.khedr.ecommerce.pojo.MultiplePagesDetails;
import com.khedr.ecommerce.pojo.StatusAndMessage;

import java.util.Date;
import java.util.List;

public class GetAllOrdersResponse  extends StatusAndMessage {
    GetAllOrdersOuterData data ;

    public GetAllOrdersResponse(boolean status, String message, GetAllOrdersOuterData data) {
        super(status, message);
        this.data = data;
    }

    public GetAllOrdersOuterData getData() {
        return data;
    }

    public void setData(GetAllOrdersOuterData data) {
        this.data = data;
    }

    public static class GetAllOrdersOuterData extends MultiplePagesDetails {

        List<GetAllOrdersInnerData> data;

        public GetAllOrdersOuterData(int current_page, int from, int last_page, int per_page, int to, int total, String first_page_url, String last_page_url, String next_page_url, String path, String prev_page_url, List<GetAllOrdersInnerData> data) {

            super(current_page, from, last_page, per_page, to, total, first_page_url, last_page_url, next_page_url, path, prev_page_url);
            this.data = data;
        }

        public List<GetAllOrdersInnerData> getData() {
            return data;
        }

        public void setData(List<GetAllOrdersInnerData> data) {
            this.data = data;
        }
    }
    public static class GetAllOrdersInnerData{
        int id;
        double total;
        String date;
        String status;

        public GetAllOrdersInnerData(int id, double total, String date, String status) {
            this.id = id;
            this.total = total;
            this.date = date;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
