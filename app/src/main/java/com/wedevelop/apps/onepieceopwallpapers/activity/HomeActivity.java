package com.wedevelop.apps.onepieceopwallpapers.activity;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.adapter.fragmentAdapter;
import com.wedevelop.apps.onepieceopwallpapers.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    fragmentAdapter adapter;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_random,
            R.drawable.ic_favorite
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);



        // setting up the adapter, adapter tells which fragment to load
         adapter = new fragmentAdapter(getSupportFragmentManager());// call of consturctor
        setupViewPager(viewPager);



        tabLayout = (TabLayout) findViewById(R.id.tabs);


        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new fragmentAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
    }

    }
