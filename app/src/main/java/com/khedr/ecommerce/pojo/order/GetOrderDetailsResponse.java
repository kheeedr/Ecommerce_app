/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.order;

import com.khedr.ecommerce.pojo.Address.GetAddressData;
import com.khedr.ecommerce.pojo.StatusAndMessage;

import java.util.List;

public class GetOrderDetailsResponse extends StatusAndMessage {
    OrderDetailsData  data;

    public GetOrderDetailsResponse(boolean status, String message, OrderDetailsData data) {
        super(status, message);
        this.data = data;
    }

    public OrderDetailsData getData() {
        return data;
    }

    public void setData(OrderDetailsData data) {
        this.data = data;
    }

    public static class OrderDetailsData {
        int id;
        double cost;
        double discount;
        double points;
        double vat;
        double total;
        double points_commission;
        String promo_code;
        String payment_method;
        String date;
        String status;

        GetAddressData address;
        List<OrderDetailsProduct> products;

        public OrderDetailsData(int id,
                                double cost,
                                double discount,
                                double points,
                                double vat,
                                double total,
                                double points_commission,
                                String promo_code,
                                String payment_method,
                                String date,
                                String status,
                                GetAddressData address,
                                List<OrderDetailsProduct> products) {
            this.id = id;
            this.cost = cost;
            this.discount = discount;
            this.points = points;
            this.vat = vat;
            this.total = total;
            this.points_commission = points_commission;
            this.promo_code = promo_code;
            this.payment_method = payment_method;
            this.date = date;
            this.status = status;
            this.address = address;
            this.products = products;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getPoints() {
            return points;
        }

        public void setPoints(double points) {
            this.points = points;
        }

        public double getVat() {
            return vat;
        }

        public void setVat(double vat) {
            this.vat = vat;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public double getPoints_commission() {
            return points_commission;
        }

        public void setPoints_commission(double points_commission) {
            this.points_commission = points_commission;
        }

        public String getPromo_code() {
            return promo_code;
        }

        public void setPromo_code(String promo_code) {
            this.promo_code = promo_code;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public GetAddressData getAddress() {
            return address;
        }

        public void setAddress(GetAddressData address) {
            this.address = address;
        }

        public List<OrderDetailsProduct> getProducts() {
            return products;
        }

        public void setProducts(List<OrderDetailsProduct> products) {
            this.products = products;
        }
    }

    public static class OrderDetailsProduct {
        int id;
        int  quantity;
        double price;
        String name;
        String image;

        public OrderDetailsProduct(int id, int quantity, double price, String name, String image) {
            this.id = id;
            this.quantity = quantity;
            this.price = price;
            this.name = name;
            this.image = image;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

}
