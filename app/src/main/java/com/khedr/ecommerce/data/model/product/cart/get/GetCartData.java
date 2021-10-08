package com.khedr.ecommerce.data.model.product.cart.get;

import java.util.ArrayList;

public class GetCartData {

    ArrayList<GetCartItems> cart_items;
    double sub_total;
    double total;

    public GetCartData(ArrayList<GetCartItems> cart_items, double sub_total, double total) {
        this.cart_items = cart_items;
        this.sub_total = sub_total;
        this.total = total;
    }

    public ArrayList<GetCartItems> getCart_items() {
        return cart_items;
    }

    public void setCart_items(ArrayList<GetCartItems> cart_items) {
        this.cart_items = cart_items;
    }

    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
