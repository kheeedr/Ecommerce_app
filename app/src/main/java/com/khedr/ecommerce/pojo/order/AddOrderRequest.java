/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.order;

public class AddOrderRequest {
    int address_id;
    int payment_method;
    boolean use_points;
    String promo_code;

    public AddOrderRequest(int address_id, int payment_method, boolean use_points, String promo_code) {
        this.address_id = address_id;
        this.payment_method = payment_method;
        this.use_points = use_points;
        this.promo_code = promo_code;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }

    public boolean isUse_points() {
        return use_points;
    }

    public void setUse_points(boolean use_points) {
        this.use_points = use_points;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }


}
