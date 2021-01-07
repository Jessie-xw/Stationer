package com.example.stationer.Data;

public class Data{
    private static String account ="user";

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String a) {
        Data.account = a;
    }
}
