/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.data.model.order;

public class EstimateOrderRequest {
    boolean use_points;
    Integer promo_code_id;

    public EstimateOrderRequest(boolean use_points, Integer promo_code_id) {
        this.use_points = use_points;

        this.promo_code_id = promo_code_id;


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

    public void setPromo_code_id(Integer promo_code_id) {

        this.promo_code_id = promo_code_id;

    }
}
