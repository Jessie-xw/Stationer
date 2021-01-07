package com.example.stationer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.stationer.fragments.SellerFragment;
import com.example.stationer.fragments.ProductInfoFragment;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private String[] tabText = {"商品信息", "进货商"};
    private int[] normalIcon = {R.drawable.product_info_unselected, R.drawable.producer_unselected};
    private int[] selectIcon = {R.drawable.product_info_selected, R.drawable.producer_selected};
    private List<Fragment> fragmentList = new ArrayList<>();
    private EasyNavigationBar navigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        actionBar.setTitle("管理商品");

        navigationBar = findViewById(R.id.navigationBar_admin);
        fragmentList.add(new ProductInfoFragment());
        fragmentList.add(new SellerFragment());
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragmentList)
                .fragmentManager(getSupportFragmentManager())
                .build();
    }
}