package com.example.stationer.product;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stationer.R;
import com.example.stationer.webImage.MyImageView;

import java.util.List;

public class HomeItemAdapter extends ArrayAdapter<HomeItem> {

    private List<HomeItem> itemList;
    private int resourceId;

    public HomeItemAdapter(@NonNull Context context, int resource, List<HomeItem> itemList) {
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
        HomeItem item = itemList.get(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        MyImageView image = (MyImageView) view.findViewById(R.id.item_image);
        TextView name = (TextView) view.findViewById(R.id.item_desc);
        TextView price = (TextView) view.findViewById(R.id.item_price_home);
        name.setText(item.getName());
        price.setText(item.getPrice());
        //设置图片
        image.setImageURL(item.getPicWeb());
        return view;
    }

    public void Refresh(List<HomeItem> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }
}
