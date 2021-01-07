package com.example.stationer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

public class AddProducerActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    private EditText sellerName;
    private Button yes;
    private Button reset;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producer);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("添加供货商");

        sellerName = (EditText) findViewById(R.id.seller_name);
        yes = (Button) findViewById(R.id.seller_for_sure);
        reset = (Button) findViewById(R.id.seller_reset);

        dbHelper = new MyDatabaseHelper(this, "Stationer.db", null, 1);
        dbHelper.getWritableDatabase();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String nameContent = sellerName.getText().toString();
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", nameContent);

                Cursor cursor = db.rawQuery("select * from Seller where name=?", new String[]{nameContent});
                if(cursor.moveToFirst() == false){
                    db.insert("Seller", null, contentValues);
                    Toast.makeText(AddProducerActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddProducerActivity.this, "该供货商已存在！", Toast.LENGTH_SHORT).show();
                }
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