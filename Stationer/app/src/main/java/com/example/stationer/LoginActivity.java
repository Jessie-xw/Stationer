package com.example.stationer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.stationer.fragments.AdminFragment;
import com.example.stationer.fragments.LoginFragmentAdapter;
import com.example.stationer.fragments.CustomerFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Fragment studentFrag;
    private Fragment adminFrag;

    private List<Fragment> fragmentList = new ArrayList<>();

    private LoginFragmentAdapter adapter;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        studentFrag = new CustomerFragment();
        adminFrag = new AdminFragment();

        fragmentList.add(studentFrag);
        fragmentList.add(adminFrag);

        adapter = new LoginFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab;
        tab = tabLayout.getTabAt(0);
        tab.setCustomView(R.layout.tab_cust);
        tab.getCustomView().findViewById(R.id.cust_text);

        tab = tabLayout.getTabAt(1);
        tab.setCustomView(R.layout.tab_admin);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tab.setCustomView(R.layout.tab_cust);
                        tab.getCustomView().findViewById(R.id.cust_text).setSelected(false);
                    case 1:
                        tab.setCustomView(R.layout.tab_admin);
                        tab.getCustomView().findViewById(R.id.admin_text).setSelected(false);
                }
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tab.setCustomView(R.layout.tab_cust);
                        tab.getCustomView().findViewById(R.id.cust_text).setSelected(true);
                    case 1:
                        tab.setCustomView(R.layout.tab_admin);
                        tab.getCustomView().findViewById(R.id.admin_text).setSelected(true);
                }
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }
}