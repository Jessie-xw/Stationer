package com.example.stationer.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stationer.Data.Data;
import com.example.stationer.MainActivity;
import com.example.stationer.PayActivity;
import com.example.stationer.R;
import com.example.stationer.database.MyDatabaseHelper;
import com.example.stationer.product.CarItem;
import com.example.stationer.product.CarItemAdapter;
import com.example.stationer.product.PaidItem;
import com.example.stationer.product.PaidItemAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyPageFragment extends Fragment {

    String Account= Data.getAccount();

    private MyDatabaseHelper dbHelper;
    private List<PaidItem> PaidItemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_frag, container, false);

        initPaidItem();
        PaidItemAdapter adapter = new PaidItemAdapter(getActivity(), R.layout.paid_item, PaidItemList);
        ListView listView = (ListView) view.findViewById(R.id.paid_list_view);
        listView.setAdapter(adapter);
        return view;
    }

    private void initPaidItem () {
//        ItemList.clear();
        dbHelper = new MyDatabaseHelper(getActivity(), "Stationer.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Paid where userName=?", new String[]{Account});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("productId"));
                String name = cursor.getString(cursor.getColumnIndex("productName"));
                int number = cursor.getInt(cursor.getColumnIndex("number"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                double price = 0;

                Cursor cur = db.rawQuery("select * from Product where id=?", new String[]{id+""});
                if(cur.moveToFirst()) {
                    price = cur.getDouble(cur.getColumnIndex("price"));
                    PaidItem item = new PaidItem(name, price+"", number+"", address, date);
                    PaidItemList.add(item);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}