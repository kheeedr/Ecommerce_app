/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.Address;

public class SelectableAddressModel extends GetAddressData {
    boolean selected, expanded;

    public SelectableAddressModel(GetAddressData getAddressData) {
        super(getAddressData.name, getAddressData.city, getAddressData.region, getAddressData.details, getAddressData.notes, getAddressData.id);
        selected = false;
        expanded = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
