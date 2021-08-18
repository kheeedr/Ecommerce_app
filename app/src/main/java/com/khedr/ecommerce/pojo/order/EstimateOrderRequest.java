/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.order;

public class EstimateOrderRequest {
    boolean use_points;
    int promo_code_id;

    public EstimateOrderRequest(boolean use_points, Integer promo_code_id) {
        this.use_points = use_points;
        if (promo_code_id == null) {
            this.promo_code_id = -1;
        } else {
            this.promo_code_id = promo_code_id.intValue();
        }

    }

    public boolean isUse_points() {
        return use_points;
    }

    public void setUse_points(boolean use_points) {
        this.use_points = use_points;
    }

    public int getPromo_code_id() {
        return promo_code_id;
    }

    public void setPromo_code_id(Integer promo_code_id) {
        if (promo_code_id == null) {
            this.promo_code_id = -1;
        } else {
            this.promo_code_id = promo_code_id.intValue();
        }

    }
}
