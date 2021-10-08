/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.presentation.ui.orderDetails;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrderDetailsActivityTest {

    @Test
    public void isCanceledOrderWithFalse() {
        String s="new";
        OrderDetailsActivity orderDetailsActivity=new OrderDetailsActivity();

        boolean result=orderDetailsActivity.isCanceledOrder("skld");
        assertFalse(result);
    }
}