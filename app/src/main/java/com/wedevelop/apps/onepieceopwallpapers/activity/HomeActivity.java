package com.wedevelop.apps.onepieceopwallpapers.activity;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        displayFragment(new HomeFragment());
    }

    private void displayFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_area,fragment).commit();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {

            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_fav:
                fragment = new HomeFragment();
                break;
            case R.id.nav_set:
                fragment = new HomeFragment();
                break;
            default:
                fragment = new HomeFragment();
                break;
        }
        displayFragment(fragment);
        return true;
    }
}
