package com.example.stationer.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.stationer.R;
import com.example.stationer.seller.SellerItem;
import com.example.stationer.webImage.MyImageView;

import java.util.List;

public class CarItemAdapter extends ArrayAdapter<CarItem> {

    private List<CarItem> carItems;
    private int resourceId;
    private int type;

    public CarItemAdapter(@NonNull Context context, int resource, List<CarItem> carItems, int type) {
        super(context, resource);
        this.carItems = carItems;
        this.resourceId = resource;
        this.type = type;
    }

    @Override
    public int getCount() {
        return carItems == null ? 0 : carItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarItem item = carItems.get(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.car_item, parent, false);
        MyImageView itemPic = (MyImageView) view.findViewById(R.id.caritem_image);
        TextView itemName = view.findViewById(R.id.caritem_name);
        TextView itemPrice= view.findViewById(R.id.caritem_price);
        TextView itemNum= view.findViewById(R.id.caritem_num);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.select);
        if(type == 1){
            checkBox.setChecked(true);
            checkBox.setClickable(false);
        }
        itemNum.setText("x"+item.getNum());
        itemPic.setImageURL(item.getPicWeb());
        itemName.setText(item.getName());
        itemPrice.setText(item.getPrice());

        return view;
    }

    public void Refresh(List<CarItem> itemList) {
//        this.itemList.clear();
        this.carItems = itemList;
        notifyDataSetChanged();
    }
}
