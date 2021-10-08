/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.data.model.order;

public class AddOrderRequest {
    int address_id;
    int payment_method;
    boolean use_points;
    Integer promo_code_id;

    public AddOrderRequest(int address_id, int payment_method, boolean use_points,  Integer promo_code_id) {
        this.address_id = address_id;
        this.payment_method = payment_method;
        this.use_points = use_points;
        this.promo_code_id = promo_code_id;
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

    public Integer getPromo_code_id() {
        return promo_code_id;
    }

    public void setPromo_code_id( Integer promo_code_id) {
        this.promo_code_id = promo_code_id;
    }


}
