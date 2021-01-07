package com.example.stationer.seller;

import java.io.Serializable;

public class SellerItem implements Serializable {

    private String Id;

    private String name;

    public SellerItem(String id, String name) {
        Id = id;
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
