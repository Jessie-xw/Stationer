package com.example.stationer.product;

import java.io.Serializable;

public class CarItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String picWeb;
    private String name;
    private String price;
    private String num;

    public CarItem(String picWeb, String name, String price, String num, int i){
        this.picWeb=picWeb;
        this.name=name;
        this.price=price;
        this.num=num;
    }

    public String getPicWeb(){
        return picWeb;
    }

    public String getName(){
        return name;
    }

    public String getPrice(){return price;}

    public String getNum(){return num;}
}
