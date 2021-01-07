package com.example.stationer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stationer.database.MyDatabaseHelper;

public class AddItemActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    private EditText itemName;
    private EditText itemPrice;
    private EditText itemStorage;
    private EditText itemImage;
    private EditText itemSeller;
    private static final int sales=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        itemName = (EditText) findViewById(R.id.item_name);
        itemPrice = (EditText) findViewById(R.id.item_price);
        itemStorage = (EditText) findViewById(R.id.item_storage);
        itemImage = (EditText) findViewById(R.id.item_image);
        itemSeller = (EditText) findViewById(R.id.item_seller);
        Button yes = (Button) findViewById(R.id.add_for_sure);
        Button reset = (Button) findViewById(R.id.reset);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("添加商品");

        //创建数据库
        dbHelper = new MyDatabaseHelper(this, "Stationer.db", null, 1);
        dbHelper.getWritableDatabase();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加数据
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String nameContent = itemName.getText().toString();
                String priceContent = itemPrice.getText().toString();
                String storageContent = itemStorage.getText().toString();
                String imageContent = itemImage.getText().toString();
                String sellerContent = itemSeller.getText().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put("name", nameContent);
                contentValues.put("price", Double.parseDouble(priceContent));
                contentValues.put("storage", Integer.parseInt(storageContent));
                contentValues.put("sales", 0);
                contentValues.put("picWeb", imageContent);
                contentValues.put("sellerNumber", sellerContent);

                Cursor cursor = db.rawQuery("select * from Seller where id=?", new String[]{sellerContent});
                if(cursor.moveToFirst() == false){
                    Toast.makeText(AddItemActivity.this, "该供货商不存在！", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cur = db.rawQuery("select name, picWeb from Product where name=? AND picWeb=?", new String[]{nameContent, imageContent});
                    if(cur.moveToFirst() == false) {
                        db.insert("Product", null, contentValues);
                        Toast.makeText(AddItemActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddItemActivity.this, "该商品已存在！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName.setText("");
                itemPrice.setText("");
                itemStorage.setText("");
                itemImage.setText("");
                itemSeller.setText("");
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}