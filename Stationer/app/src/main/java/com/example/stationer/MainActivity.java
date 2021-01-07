package com.example.stationer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.stationer.fragments.CarFragment;
import com.example.stationer.fragments.HomeFragment;
import com.example.stationer.fragments.MyPageFragment;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] tabText = {"首页", "购物车", "我的"};
    private int[] normalIcon = {R.drawable.home_unselected, R.drawable.car_unselected, R.drawable.my_unselected};
    private int[] selectIcon = {R.drawable.home_selected, R.drawable.car_selected, R.drawable.my_selected};

    private List<Fragment> fragmentList = new ArrayList<>();
    private EasyNavigationBar navigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("文具商城");

        navigationBar = findViewById(R.id.navigationBar);
        fragmentList.add(new HomeFragment());
        fragmentList.add(new CarFragment());
        fragmentList.add(new MyPageFragment());
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragmentList)
                .fragmentManager(getSupportFragmentManager())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        int id = intent.getIntExtra("fragNo", 0);
        boolean payOk = intent.getBooleanExtra("payOk", false);
        if(payOk){
            navigationBar.selectTab(2);
        } else {
            navigationBar.selectTab(id);
        }
    }

}