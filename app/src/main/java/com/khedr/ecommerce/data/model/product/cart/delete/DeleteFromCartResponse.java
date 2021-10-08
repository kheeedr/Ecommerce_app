package com.khedr.ecommerce.data.model.product.cart.delete;

import com.khedr.ecommerce.data.model.product.cart.update.UpdateQuantityData;
import com.khedr.ecommerce.data.model.product.cart.update.UpdateQuantityResponse;

public class DeleteFromCartResponse extends UpdateQuantityResponse {
    public DeleteFromCartResponse(boolean status, String message, UpdateQuantityData data) {
        super(status, message, data);
    }
}
