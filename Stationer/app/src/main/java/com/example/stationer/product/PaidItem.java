package com.example.stationer.product;

public class PaidItem{
    private String name;
    private String price;
    private String num;
    private String address;
    private String date;

    public PaidItem(String name,String price,String num,String address,String date){
        this.name=name;
        this.price=price;
        this.num=num;
        this.address=address;
        this.date=date;
    }
    public String getName(){
        return name;
    }

    public String getPrice(){return price;}

    public String getNum(){return num;}

    public String getAddress(){return address;}

    public String getDate(){return date;}
}
