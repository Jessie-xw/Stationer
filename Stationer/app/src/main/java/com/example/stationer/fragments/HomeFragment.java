package com.example.stationer.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stationer.Data.Data;
import com.example.stationer.MainActivity;
import com.example.stationer.R;
import com.example.stationer.ShowProductActivity;
import com.example.stationer.database.MyDatabaseHelper;
import com.example.stationer.product.AdminItem;
import com.example.stationer.product.HomeItem;
import com.example.stationer.product.HomeItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    String account= Data.getAccount();

    private ListView listView;
    private List<HomeItem> itemList = new ArrayList<>();
    private HomeItemAdapter adapter;
    private MyDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_frag, container, false);
        dbHelper = new MyDatabaseHelper(getActivity(), "Stationer.db", null, 1);
        initItem();
        listView = (ListView) view.findViewById(R.id.home_list_view);
        adapter = new HomeItemAdapter(getActivity(), R.layout.product_item, itemList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeItem item = itemList.get(position);
                Intent intent = new Intent(getActivity(), ShowProductActivity.class);
                intent.putExtra("HomeItem", item);
                startActivity(intent);
            }
        });

        final EditText editText = (EditText) view.findViewById(R.id.home_edit_text);
        ImageButton search = (ImageButton) view.findViewById(R.id.home_search_bt);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchContent = editText.getText().toString();
                if("".equals(searchContent) == false) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("select * from Product", null);
                    if(cursor.moveToFirst()) {
                        itemList.clear();
                       do {
                            String id = cursor.getString(cursor.getColumnIndex("id"));
                            String name = cursor.getString(cursor.getColumnIndex("name"));
                            String picWeb = cursor.getString(cursor.getColumnIndex("picWeb"));
                            Double price = cursor.getDouble(cursor.getColumnIndex("price"));
                            int storage = cursor.getInt(cursor.getColumnIndex("storage"));
                            String seller = cursor.getString(cursor.getColumnIndex("sellerNumber"));
                            Cursor cur = db.rawQuery("select name from Seller where id=?", new String[]{seller});
                            if(cur.moveToFirst()){
                                seller = cur.getString(cur.getColumnIndex("name"));
                            }
                            if(searchContent.indexOf(name) != -1 || name.indexOf(searchContent) != -1) {
                                HomeItem item = new HomeItem(id, name, picWeb, "¥"+price, ""+storage, seller);
                                itemList.add(item);
                            } else if(searchContent.indexOf(id) != -1 || id.indexOf(searchContent) != -1) {
                                HomeItem item = new HomeItem(id, name, picWeb, "¥"+price, ""+storage, seller);
                                itemList.add(item);
                            }
                        } while (cursor.moveToNext());
                        adapter.Refresh(itemList);
                        if(itemList.size() == 0) {
                            Toast.makeText(getActivity(), "你要找的商品不存在！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        return view;
    }

    private void initItem(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Product", null);
        if(cursor.moveToFirst()) {
            do{
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String picWeb = cursor.getString(cursor.getColumnIndex("picWeb"));
                Double price = cursor.getDouble(cursor.getColumnIndex("price"));
                int storage = cursor.getInt(cursor.getColumnIndex("storage"));
                String seller = cursor.getString(cursor.getColumnIndex("sellerNumber"));

                Cursor cur = db.rawQuery("select name from Seller where id=?", new String[]{seller});
                if(cur.moveToFirst()){
                    seller = cur.getString(cur.getColumnIndex("name"));
                }

                HomeItem item = new HomeItem(id, name, picWeb, "¥"+price, ""+storage, seller);
                itemList.add(item);
            } while(cursor.moveToNext());
        }
        cursor.close();
    }

}
