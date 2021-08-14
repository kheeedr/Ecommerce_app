/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.Address;

import java.io.Serializable;

public class AddressData  implements Serializable {

    String name;
    String city;
    String region;
    String details;
    String notes;
    int latitude = 0;
    int longitude = 0;

    public AddressData(String name, String city, String region, String details, String notes) {
        this.name = name;
        this.city = city;
        this.region = region;
        this.details = details;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
