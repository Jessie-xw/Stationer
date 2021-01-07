package com.example.stationer.seller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stationer.R;

import java.util.List;

public class SellerItemAdapter extends ArrayAdapter<SellerItem> {

    private List<SellerItem> itemList;
    private int resourceId;

    public SellerItemAdapter(@NonNull Context context, int resource, List<SellerItem> itemList) {
        super(context, resource);
        this.itemList = itemList;
        this.resourceId = resource;
    }

    @Override
    public int getCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SellerItem item = itemList.get(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView itemId = view.findViewById(R.id.producer_id);
        TextView itemName = view.findViewById(R.id.producer_name);
        itemId.setText(item.getId());
        itemName.setText(item.getName());
        return view;
    }

    public void Refresh(List<SellerItem> itemList) {
//        this.itemList.clear();
        this.itemList = itemList;
        notifyDataSetChanged();
    }
}
