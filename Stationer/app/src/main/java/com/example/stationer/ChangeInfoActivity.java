package com.example.stationer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stationer.database.MyDatabaseHelper;
import com.example.stationer.product.AdminItem;

public class ChangeInfoActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("修改商品信息");

        final EditText itemName = (EditText) findViewById(R.id.item_name_change);
        final EditText itemPrice = (EditText) findViewById(R.id.item_price_change);
        final EditText itemStorage = (EditText) findViewById(R.id.item_storage_change);
        final EditText picWeb = (EditText) findViewById(R.id.item_image_change);

        Intent intent1 = getIntent();
        final AdminItem item = (AdminItem) intent1.getSerializableExtra("AdminItem");
        itemName.setText(item.getName());
        itemPrice.setText(item.getPrice());
        itemStorage.setText(item.getStorage());
        picWeb.setText(item.getPicWeb());

        intent = new Intent(this, AdminActivity.class);
        dbHelper = new MyDatabaseHelper(this, "Stationer.db", null, 1);
        Button yes = (Button) findViewById(R.id.change_for_sure);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String nameContent = itemName.getText().toString();
                String priceContent = itemPrice.getText().toString();
                String storageContent = itemStorage.getText().toString();
                String imageContent = picWeb.getText().toString();
                String id = item.getId();

                intent.putExtra("change", true);
                db.execSQL("update Product set name=?, price=?, storage=?, picWeb=? where id=?", new String[]{nameContent, priceContent, storageContent, imageContent, id});
                Toast.makeText(ChangeInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
            }
        });

        Button delete = (Button) findViewById(R.id.delete_item_change);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeInfoActivity.this);
                builder.setTitle("警告");
                builder.setMessage("你确定要删除该商品吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("delete from Product where id=?", new String[]{item.getId()});
                        Toast.makeText(ChangeInfoActivity.this, "商品已删除", Toast.LENGTH_SHORT).show();
                        db.execSQL("update Selected set exist=? where productId=?", new String[]{"0", item.getId()});
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}