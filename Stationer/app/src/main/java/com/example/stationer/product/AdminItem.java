package com.example.stationer.product;

import java.io.Serializable;

public class AdminItem implements Serializable {

    private String id;

    private String name;

    private String sales;

    private String storage;

    private String price;

    private String picWeb;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicWeb() {
        return picWeb;
    }

    public void setPicWeb(String picWeb) {
        this.picWeb = picWeb;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public AdminItem(String id, String name, String sales, String storage, String price, String picWeb) {
        this.id = id;
        this.name = name;
        this.sales = sales;
        this.storage = storage;
        this.price = price;
        this.picWeb = picWeb;
    }

    public AdminItem(String id, String name, String sales, String storage) {
        this.id = id;
        this.name = name;
        this.sales = sales;
        this.storage = storage;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
