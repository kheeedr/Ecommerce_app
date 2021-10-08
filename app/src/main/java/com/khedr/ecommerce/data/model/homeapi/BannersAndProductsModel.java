package com.khedr.ecommerce.data.model.homeapi;

import com.khedr.ecommerce.data.model.product.Product;

import java.util.ArrayList;

public class BannersAndProductsModel {
    ArrayList<Banner> banners;
    ArrayList<Product> products;
    String ad;

    public BannersAndProductsModel(ArrayList<Banner> banners, ArrayList<Product> products, String ad) {
        this.banners = banners;
        this.products = products;
        this.ad = ad;
    }

    public ArrayList<Banner> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }
}
