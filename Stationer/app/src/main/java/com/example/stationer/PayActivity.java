package com.example.stationer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stationer.Data.Data;
import com.example.stationer.database.MyDatabaseHelper;
import com.example.stationer.product.CarItem;
import com.example.stationer.product.CarItemAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PayActivity extends AppCompatActivity {

    String Account= Data.getAccount();
    List<CarItem> PayItemList = new ArrayList<>();

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Intent intent = getIntent();
        PayItemList = (List<CarItem>) intent.getSerializableExtra("PayItemList");

        final CarItemAdapter adapter= new CarItemAdapter(this, R.layout.car_item, PayItemList, 1);
        final ListView PayList = (ListView) this.findViewById(R.id.pay_list_view);
        PayList.setAdapter(adapter);

        final EditText address = (EditText) findViewById(R.id.address_edit);
        final Button pay = (Button)this.findViewById(R.id.pay);
        final Button cancel = (Button)this.findViewById(R.id.cancel);


        dbHelper = new MyDatabaseHelper(this, "Stationer.db", null, 1);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String date = df.format(new Date());

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Address = address.getText().toString();
//                Toast.makeText(PayActivity.this, Address, Toast.LENGTH_SHORT).show();
                if("".equals(Address)){
                    Toast.makeText(PayActivity.this, "请填写收货地址", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    for(int i = 0;i < PayItemList.size(); i ++){
                        String name = PayItemList.get(i).getName();
                        String number = PayItemList.get(i).getNum();
                        String id = null;
                        //Selected删除
                        db.execSQL("delete from Selected where userName=? AND productName=?", new String[]{Account, name});
                        //Product修改
                        Cursor cursor = db.rawQuery("select * from Product where name=?", new String[]{name});
                        if(cursor.moveToFirst()) {
                            int sales = cursor.getInt(cursor.getColumnIndex("sales"));
                            int storage = cursor.getInt(cursor.getColumnIndex("storage"));
                            id = cursor.getString(cursor.getColumnIndex("id"));
                            sales += Integer.parseInt(number);
                            storage -= Integer.parseInt(number);
                            ContentValues values = new ContentValues();
                            values.put("sales", sales);
                            values.put("storage", storage);
                            db.update("Product", values, "id=?", new String[]{id});
                        }
                        //Paid添加
                        db.execSQL("insert into Paid(userName, productId, productName, number, address, date) values(?, ?, ?, ?, ?, ?)", new String[]{Account, id, name, number, Address, date});
                    }
                    Toast.makeText(PayActivity.this, "下单成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(PayActivity.this, MainActivity.class);
                    intent.putExtra("fragNo", 2);
                    intent.putExtra("payOk", true);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                intent.putExtra("fragNo", 1);
                intent.putExtra("isCancel", true);
                startActivity(intent);
                finish();
            }
        });
    }
}
