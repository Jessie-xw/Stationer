package com.example.stationer.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;

import com.example.stationer.AddItemActivity;
import com.example.stationer.AdminActivity;
import com.example.stationer.ChangeInfoActivity;
import com.example.stationer.R;
import com.example.stationer.database.MyDatabaseHelper;
import com.example.stationer.product.AdminItem;
import com.example.stationer.product.AdminItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoFragment extends Fragment {

    private List<AdminItem> adminItems = new ArrayList<>();

    private AdminItemAdapter adapter;

    private ListView listView;

    private MyDatabaseHelper dbHelper;

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_info_frag, container, false);
        dbHelper = new MyDatabaseHelper(getActivity(), "Stationer.db", null, 1);
        initItem();

        inflater = LayoutInflater.from(getActivity());
        View headView = inflater.inflate(R.layout.admin_list_header, null, false);

        listView = (ListView) view.findViewById(R.id.admin_list_view);
        listView.addHeaderView(headView);
        adapter = new AdminItemAdapter(getActivity(), R.layout.admin_item, adminItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdminItem item = adminItems.get(position-1);
                Intent intent = new Intent(getContext(), ChangeInfoActivity.class);
                intent.putExtra("AdminItem", item);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_bt);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        final EditText searchText = (EditText) view.findViewById(R.id.admin_search_edittext);
        ImageButton searchButton = (ImageButton) view.findViewById(R.id.admin_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchContent = searchText.getText().toString();
                if("".equals(searchContent) == false) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("select * from Product", null);
                    if(cursor.moveToFirst()) {
                        adminItems.clear();
                        do {
                            String name = cursor.getString(cursor.getColumnIndex("name"));
                            String id = cursor.getString(cursor.getColumnIndex("id"));
                            int sales = cursor.getInt(cursor.getColumnIndex("sales"));
                            int storage = cursor.getInt(cursor.getColumnIndex("storage"));
                            if(searchContent.indexOf(name) != -1 || name.indexOf(searchContent) != -1) {
                                AdminItem item = new AdminItem(id, name, ""+sales, ""+storage);
                                adminItems.add(item);
                            } else if(searchContent.indexOf(id) != -1 || id.indexOf(searchContent) != -1) {
                                AdminItem item = new AdminItem(id, name, ""+sales, ""+storage);
                                adminItems.add(item);
                            }
                        } while (cursor.moveToNext());

                        adapter.Refresh(adminItems);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initItem();
        adapter.Refresh(adminItems);
    }

    private void initItem() {
        adminItems.clear();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Product", null, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                int sales = cursor.getInt(cursor.getColumnIndex("sales"));
                int storage = cursor.getInt(cursor.getColumnIndex("storage"));
                String picWeb = cursor.getString(cursor.getColumnIndex("picWeb"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                AdminItem item = new AdminItem(id, name, ""+sales, ""+storage, ""+price, picWeb);
                adminItems.add(item);
                Log.e("DBItem", id+name);
            } while(cursor.moveToNext());
        }
        cursor.close();
    }

    public void Refresh(){
        initItem();
        adapter.Refresh(adminItems);
    }
}
