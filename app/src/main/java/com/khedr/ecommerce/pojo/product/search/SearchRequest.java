package com.khedr.ecommerce.pojo.product.search;

public class SearchRequest {
    String text;

    public SearchRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
