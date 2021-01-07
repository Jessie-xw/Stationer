package com.example.stationer.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.stationer.R;

import java.util.List;

public class PaidItemAdapter extends ArrayAdapter<PaidItem> {

    private List<PaidItem> paidItems;
    private int resourceId;

    public PaidItemAdapter(@NonNull Context context, int resource, List<PaidItem> paidItems) {
        super(context, resource);
        this.paidItems = paidItems;
        this.resourceId = resource;
    }

    @Override
    public int getCount() {
        return paidItems == null ? 0 : paidItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PaidItem item = paidItems.get(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.paid_item, parent, false);
        TextView itemName = view.findViewById(R.id.paiditem_name);
        TextView itemPrice= view.findViewById(R.id.paiditem_price);
        TextView itemNum= view.findViewById(R.id.paiditem_num);
        TextView itemAddress= view.findViewById(R.id.paiditem_address);
        TextView itemDate= view.findViewById(R.id.paiditem_date);
        itemName.setText(item.getName());
        itemPrice.setText(item.getPrice());
        itemNum.setText("x"+item.getNum());
        itemAddress.setText(item.getAddress());
        itemDate.setText(item.getDate());
        return view;
    }
}
