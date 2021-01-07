package com.example.stationer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stationer.Data.Data;
import com.example.stationer.database.MyDatabaseHelper;
import com.example.stationer.product.HomeItem;
import com.example.stationer.webImage.MyImageView;

public class ShowProductActivity extends AppCompatActivity {

    String Account= Data.getAccount();

    private MyImageView image;
    private TextView name;
    private TextView price;
    private TextView storage;
    private TextView seller;
    private Button checkCar;
    private Button addToCar;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        dbHelper = new MyDatabaseHelper(this, "Stationer.db", null, 1);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("查看商品");

        Intent intent = getIntent();
        final HomeItem item = (HomeItem) intent.getSerializableExtra("HomeItem");

        image = (MyImageView) findViewById(R.id.show_image);
        name = (TextView) findViewById(R.id.show_name);
        price = (TextView) findViewById(R.id.price_show);
        storage = (TextView) findViewById(R.id.storage_show);
        seller = (TextView) findViewById(R.id.seller_show);
        checkCar = (Button) findViewById(R.id.check_car);
        addToCar = (Button) findViewById(R.id.add_to_car);

        image.setImageURL(item.getPicWeb());
        name.setText(item.getName());
        price.setText(item.getPrice());
        storage.setText(item.getStorage());
        seller.setText(item.getSeller());

        checkCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ShowProductActivity.this, MainActivity.class);
                intent1.putExtra("fragNo", 1);
                startActivity(intent1);
            }
        });

        addToCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 添加到购物车
                String itemId = item.getId();
                String itemName = item.getName();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //判断该用户的商品是否存在
                boolean productExist = false;
                int originNumber = 0;
                Cursor cursor = db.rawQuery("select * from Selected where userName=? AND productId=?", new String[]{Account, itemId});
                if(cursor.moveToFirst()){
                    originNumber = cursor.getInt(cursor.getColumnIndex("number"));
                    productExist = true;
                }

                if(productExist){
                    originNumber++;
                    db.execSQL("update Selected set number=? where userName=? AND productId=?", new String[]{originNumber+"", Account, itemId});
                } else {
                    db.execSQL("insert into Selected (userName, productId, productName, number, exist) values(?, ?, ?, ?, ?)", new String[]{Account, itemId, itemName, "1", "1"});
                }
                Toast.makeText(ShowProductActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class );
                intent.putExtra("fragNo", 0);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}