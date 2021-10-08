/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.data.model.order;

import com.khedr.ecommerce.data.model.StatusAndMessage;

public class CancelOrderResponse extends StatusAndMessage {
    CancelOrderData data;

    public CancelOrderResponse(boolean status, String message, CancelOrderData data) {
        super(status, message);
        this.data = data;
    }

    public CancelOrderData getData() {
        return data;
    }

    public void setData(CancelOrderData data) {
        this.data = data;
    }

    public static class CancelOrderData {
        int id;
        double cost;
        double discount;
        double points;
        double vat;
        double total;
        double points_commission;
        String promo_code;
        String payment_method;
        String date;
        String status;

        public CancelOrderData(int id,
                               double cost,
                               double discount,
                               double points,
                               double vat,
                               double total,
                               double points_commission,
                               String promo_code,
                               String payment_method,
                               String date,
                               String status) {
            this.id = id;
            this.cost = cost;
            this.discount = discount;
            this.points = points;
            this.vat = vat;
            this.total = total;
            this.points_commission = points_commission;
            this.promo_code = promo_code;
            this.payment_method = payment_method;
            this.date = date;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
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

        public double getVat() {
            return vat;
        }

        public void setVat(double vat) {
            this.vat = vat;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public double getPoints_commission() {
            return points_commission;
        }

        public void setPoints_commission(double points_commission) {
            this.points_commission = points_commission;
        }

        public String getPromo_code() {
            return promo_code;
        }

        public void setPromo_code(String promo_code) {
            this.promo_code = promo_code;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
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
