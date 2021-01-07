package com.example.stationer.product;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stationer.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdminItemAdapter extends ArrayAdapter<AdminItem> {

    private List<AdminItem> adminItems;
    private int resourceId;

    public AdminItemAdapter(@NonNull Context context, int resource, List<AdminItem> adminItems) {
        super(context, resource);
        this.adminItems = adminItems;
        this.resourceId = resource;
    }

    @Override
    public int getCount() {
        return adminItems == null ? 0 : adminItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdminItem item = adminItems.get(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView itemId = view.findViewById(R.id.item_id_show);
        TextView itemName = view.findViewById(R.id.item_name_show);
        TextView itemSales = view.findViewById(R.id.item_sales_show);
        TextView itemStorage = view.findViewById(R.id.item_storage_show);
        itemId.setText(item.getId());
        itemName.setText(item.getName());
        itemSales.setText(item.getSales());
        itemStorage.setText(item.getStorage());
        return view;
    }

    public void Refresh(List<AdminItem> adminItems) {
//        this.adminItems.clear();
        this.adminItems = adminItems;
        notifyDataSetChanged();
    }
}
