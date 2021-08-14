/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.Address;

import java.io.Serializable;

public class GetAddressData extends AddressData implements Serializable {

    int id;

    public GetAddressData(String name, String city, String region, String details, String notes, int id) {
        super(name, city, region, details, notes);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
