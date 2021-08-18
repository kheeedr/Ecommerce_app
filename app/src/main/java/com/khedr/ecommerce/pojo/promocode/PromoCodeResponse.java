/*
 * Copyright (c) 2021.
 * Created by Mohamed Khedr.
 */

package com.khedr.ecommerce.pojo.promocode;

import com.khedr.ecommerce.pojo.StatusAndMessage;

public class PromoCodeResponse extends StatusAndMessage {

    PromoCodeResponseData data;

    public PromoCodeResponse(boolean status, String message, PromoCodeResponseData data) {
        super(status, message);
        this.data = data;
    }

    public PromoCodeResponseData getData() {
        return data;
    }

    public void setData(PromoCodeResponseData data) {
        this.data = data;
    }

    public  static class PromoCodeResponseData{
        int id;
        String code;
        int value;
        int percentage;
        String start_date;
        String end_date;
        String usage_per_user;

        public PromoCodeResponseData(int id, String code, int value, int percentage, String start_date, String end_date, String usage_per_user) {
            this.id = id;
            this.code = code;
            this.value = value;
            this.percentage = percentage;
            this.start_date = start_date;
            this.end_date = end_date;
            this.usage_per_user = usage_per_user;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getUsage_per_user() {
            return usage_per_user;
        }

        public void setUsage_per_user(String usage_per_user) {
            this.usage_per_user = usage_per_user;
        }
    }
}
