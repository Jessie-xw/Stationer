package com.example.stationer.product;

import java.io.Serializable;

public class HomeItem implements Serializable {

    private String id;

    private String name;

    private String picWeb;

    private String price;

    private String storage;

    private String seller;

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public HomeItem(String id, String name, String picWeb, String price, String storage, String seller) {
        this.id = id;
        this.name = name;
        this.picWeb = picWeb;
        this.price = price;
        this.storage = storage;
        this.seller = seller;
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

    public String getPicWeb() {
        return picWeb;
    }

    public void setPicWeb(String picWeb) {
        this.picWeb = picWeb;
    }
}
