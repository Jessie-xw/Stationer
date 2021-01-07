package com.example.stationer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_SELLER = "create table Seller ("
        + "id integer primary key autoincrement, "
        + "name text)";

    public static final String CREATE_PRODUCT = "create table Product ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "price real, "
            + "storage integer, "
            + "sales integer, "
            + "picWeb text, "
            + "sellerNumber integer, "
            + "foreign key (sellerNumber) references Seller(id))";

    public static final String CREATE_CUSTOMER = "create table Customer ("
            + "account text primary key, "
            + "password text)";

    public static final String CREATE_SELECTED = "create table Selected ("
            + "userName text not null, "
            + "productId integer not null, "
            + "productName text, "
            + "number integer,"
            + "exist integer, "
            + "primary key(userName, productId))";

    public static final String CREATE_PAID = "create table Paid ("
            + "userName text not null, "
            + "productId integer not null, "
            + "productName text, "
            + "number integer, "
            + "address text, "
            + "date text, " +
            "primary key (userName, productId, date))";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SELLER);
        db.execSQL(CREATE_PRODUCT);
        db.execSQL(CREATE_CUSTOMER);
        db.execSQL(CREATE_SELECTED);
        db.execSQL(CREATE_PAID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Product");
        onCreate(db);
    }
}
