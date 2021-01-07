package com.example.stationer.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.stationer.Data.Data;
import com.example.stationer.MainActivity;
import com.example.stationer.PayActivity;
import com.example.stationer.R;
import com.example.stationer.database.MyDatabaseHelper;
import com.example.stationer.product.CarItem;
import com.example.stationer.product.CarItemAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarFragment extends Fragment {

    String Account= Data.getAccount();

    private ListView listView;
    private List<CarItem> CarItemList = new ArrayList<>();
    private List<CarItem> PayItemList = new ArrayList<>();
    private CarItemAdapter adapter;
    private MyDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.car_frag, container, false);
        dbHelper = new MyDatabaseHelper(getActivity(), "Stationer.db", null, 1);
        initCarItem();
        listView = (ListView) view.findViewById(R.id.car_list_view);
        adapter= new CarItemAdapter(getActivity(),R.layout.car_item,CarItemList, 0);
        listView.setAdapter(adapter);

        Button confirm = (Button) view.findViewById(R.id.confirm_car);
        Button delete = (Button) view.findViewById(R.id.delete_car);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean successPay = false;
                boolean notEnough = false;
                boolean noSelected = true;
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ListView CarList = (ListView) view.findViewById(R.id.car_list_view);// 结果列表

                if(CarList.getChildCount() < 1){
                    Toast.makeText(getActivity(), "目前购物车是空的~", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < CarList.getChildCount(); i++) {
                        CardView ll= (CardView)CarList.getChildAt(i);// 获得子级
                        CheckBox select = (CheckBox) ll.findViewById(R.id.select);// 从子级中获得控件
                        if(select.isChecked()){
                            noSelected = false;

                            TextView product_name = (TextView) ll.findViewById(R.id.caritem_name);
                            TextView price = (TextView) ll.findViewById(R.id.caritem_price);
                            TextView num = (TextView) ll.findViewById(R.id.caritem_num);

                            String Name = product_name.getText().toString();
                            String Price = price.getText().toString();
                            String Num = num.getText().toString();

                            boolean itemExist = false;
                            String picWeb = null;
                            String name = null;
                            Double itemPrice = null;
                            int storage = 0;
                            Cursor cursor = db.rawQuery("select * from Product where name=?", new String[]{Name});
                            if(cursor.moveToFirst()){
                                picWeb = cursor.getString(cursor.getColumnIndex("picWeb"));
                                name = cursor.getString(cursor.getColumnIndex("name"));
                                itemPrice = cursor.getDouble(cursor.getColumnIndex("price"));
                                storage = cursor.getInt(cursor.getColumnIndex("storage"));
                                itemExist = true;

                                if(Integer.parseInt(Num.substring(1, Num.length())) > storage || storage == 0){
                                    successPay = false;
                                    notEnough = true;
                                    Num = storage+"";
                                    ContentValues values = new ContentValues();
                                    values.put("number", Integer.parseInt(Num));
                                    db.update("Selected", values, "productName=? AND userName=?", new String[]{name, Account});
                                } else {
                                    successPay = true;
                                    notEnough = false;
                                }
                            }
                            cursor.close();

                            if(itemExist == false || storage == 0){
                                db.execSQL("delete from Selected where userName=? AND productName=?", new String[] {Account, Name});
                            } else {
                                if(notEnough == false) {
                                    CarItem item = new CarItem(picWeb, name, itemPrice+"", Num.substring(1, Num.length()), 1);
                                    PayItemList.add(item);
                                }
                            }
                        }
                    }
                    if(noSelected == true){
                        Toast.makeText(getActivity(), "尚未选中商品", Toast.LENGTH_SHORT).show();
                    } else {
                        if(successPay == false) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("警告");
                            if(notEnough == true) {
                                builder.setMessage("所选部分商品库存不足，已自动调整至合适数量或删除商品");
                            } else {
                                builder.setMessage("所选部分商品已失效，已自动删除购物车中失效商品");
                            }
                            builder.setCancelable(false);  //不可以点击返回键取消对话框
                            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                            initCarItem();
                            adapter.Refresh(CarItemList);
                        } else {
                            Intent intent = new Intent(getActivity(), PayActivity.class);
                            intent.putExtra("PayItemList", (Serializable)PayItemList);
                            intent.putExtra("account", Account);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ListView CarList = (ListView)view.findViewById(R.id.car_list_view);// 结果列表
                if(CarList.getChildCount() < 1){
                    Toast.makeText(getActivity(), "目前购物车是空的~", Toast.LENGTH_SHORT).show();
                } else {
                    boolean noSelected = true;
                    for (int i = 0; i < CarList.getChildCount(); i++) {
                        CardView ll= (CardView)CarList.getChildAt(i);// 获得子级
                        CheckBox select = (CheckBox) ll.findViewById(R.id.select);// 从子级中获得控件
                        if(select.isChecked()){
                            noSelected = false;
                            TextView product_name=(TextView) ll.findViewById(R.id.caritem_name);
                            String Product_name = product_name.getText().toString();
                            db.execSQL("delete from Selected where userName =? AND productName=?", new String[] {Account, Product_name });
                        }
                    }
                    if(noSelected == true) {
                        Toast.makeText(getActivity(), "请选择你想删除的商品", Toast.LENGTH_SHORT).show();
                    } else {
                        initCarItem();
                        adapter.Refresh(CarItemList);
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initCarItem();
        adapter.Refresh(CarItemList);
        Intent intent = getActivity().getIntent();
        boolean isCancel = intent.getBooleanExtra("isCancel", false);
        if(isCancel) {
            PayItemList.clear();
        }
    }

    private void initCarItem(){
        CarItemList.clear();
        dbHelper = new MyDatabaseHelper(getActivity(), "Stationer.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Selected where userName=?", new String[]{Account});
        if(cursor.moveToFirst()) {
            do{
                int productId = cursor.getInt(cursor.getColumnIndex("productId"));
                int number = cursor.getInt(cursor.getColumnIndex("number"));
                String name = cursor.getString(cursor.getColumnIndex("productName"));
                int exist = cursor.getInt(cursor.getColumnIndex("exist"));
                String picWeb = null;
                double price = 0;

                if(exist == 1){
                    Cursor cur = db.rawQuery("select * from Product where id=?", new String[]{productId+""});
                    if(cur.moveToFirst()){
                        picWeb = cur.getString(cur.getColumnIndex("picWeb"));
                        price = cur.getDouble(cur.getColumnIndex("price"));
                    }
                    cur.close();
                }

                CarItem item = new CarItem(picWeb, name, price+"", number+"", exist);
                CarItemList.add(item);
            } while(cursor.moveToNext());
        }
        cursor.close();
    }
}

