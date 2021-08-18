/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.order;


import com.khedr.ecommerce.pojo.StatusAndMessage;

public class EstimateOrderResponse extends StatusAndMessage {

    EstimateOrderData data;

    public EstimateOrderResponse(boolean status, String message, EstimateOrderData data) {
        super(status, message);
        this.data = data;
    }

    public EstimateOrderData getData() {
        return data;
    }

    public void setData(EstimateOrderData data) {
        this.data = data;
    }

    public static class EstimateOrderData{
        int sub_total;
        double discount;
        double points;
        double total;

        public EstimateOrderData(int sub_total, double discount, double points, double total) {
            this.sub_total = sub_total;
            this.discount = discount;
            this.points = points;
            this.total = total;
        }

        public int getSub_total() {
            return sub_total;
        }

        public void setSub_total(int sub_total) {
            this.sub_total = sub_total;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getPoints() {
            return points;
        }

        public void setPoints(double points) {
            this.points = points;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }
    }
}
