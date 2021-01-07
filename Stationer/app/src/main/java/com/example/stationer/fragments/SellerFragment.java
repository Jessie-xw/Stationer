package com.example.stationer.fragments;

import android.app.AlertDialog;
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
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stationer.AddProducerActivity;
import com.example.stationer.AdminActivity;
import com.example.stationer.R;
import com.example.stationer.database.MyDatabaseHelper;
import com.example.stationer.seller.SellerItem;
import com.example.stationer.seller.SellerItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SellerFragment extends Fragment {

    private List<SellerItem> itemList = new ArrayList<>();

    private SellerItemAdapter adapter;

    private ListView listView;

    private MyDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_frag, container, false);
        dbHelper = new MyDatabaseHelper(getActivity(), "Stationer.db", null, 1);
        initItem();

        inflater = LayoutInflater.from(getContext());
        View headView = inflater.inflate(R.layout.producer_list_header, null, false);

        listView = (ListView) view.findViewById(R.id.producer_list_view);
        listView.addHeaderView(headView);
        adapter = new SellerItemAdapter(getContext(), R.layout.seller_item, itemList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final SellerItem item = itemList.get(position-1);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Warning");
                builder.setMessage("你确定要删除这个进货商吗？\n警告：将导致相关的商品被删除。");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String sellerId = item.getId();
                        //删除相关商品
                        int productId = 0;
                        Cursor cursor = db.rawQuery("select * from Product where sellerNumber=?", new String[]{sellerId});
                        if(cursor.moveToFirst()) {
                            productId = cursor.getInt(cursor.getColumnIndex("id"));
                        }
                        db.execSQL("delete from Product where sellerNumber=?", new String[]{sellerId});
                        //修改Selected
                        db.execSQL("update Selected set exist=? where productId=?", new String[]{"0", productId+""});
                        //删除供货商
                        db.execSQL("delete from Seller where id=?", new String[]{sellerId});

                        initItem();
                        adapter.Refresh(itemList);
                        AdminActivity activity = (AdminActivity) getActivity();
                        ProductInfoFragment fragment = (ProductInfoFragment) activity.getSupportFragmentManager().getFragments().get(0);
                        fragment.Refresh();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.producer_bt);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProducerActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initItem();
        adapter.Refresh(itemList);
        Log.e("Return", "resume");
    }

    private void initItem() {
        itemList.clear();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Seller", null, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                SellerItem item = new SellerItem(""+id, name);
                itemList.add(item);
//                Log.e("DBItem", id+name);
            } while(cursor.moveToNext());
        }
        cursor.close();
    }
}
