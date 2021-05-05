package com.khedr.ecommerce.pojo.product.cart.update;

public class UpdateQuantityData {
    UpdateQuantityCart cart;
    double sub_total,total;

    public UpdateQuantityData(UpdateQuantityCart cart, double sub_total, double total) {
        this.cart = cart;
        this.sub_total = sub_total;
        this.total = total;
    }

    public UpdateQuantityCart getCart() {
        return cart;
    }

    public void setCart(UpdateQuantityCart cart) {
        this.cart = cart;
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
