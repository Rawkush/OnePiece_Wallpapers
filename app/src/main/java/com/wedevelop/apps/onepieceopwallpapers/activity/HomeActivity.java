package com.wedevelop.apps.onepieceopwallpapers.activity;


import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.wedevelop.apps.onepieceopwallpapers.HintServiceImpl;
import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.adapter.TabLayoutAdapter;
import com.wedevelop.apps.onepieceopwallpapers.models.Hint;

public class HomeActivity extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    TabLayoutAdapter adapter;
    boolean doubleTap;
    View download, search, newtab, categorytab, favtab;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_tab_new,
            R.drawable.ic_random,
            R.drawable.ic_favorite
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MobileAds.initialize(this,
                "ca-app-pub-1544647693026779~3122962726");

        ViewPager viewPager = findViewById(R.id.viewpager);
        download = findViewById(R.id.downloadView);
        search = findViewById(R.id.search);
        newtab = findViewById(R.id.tab1);
        categorytab = findViewById(R.id.tab2);
        favtab = findViewById(R.id.tab3);
        // setting up the adapter, adapter tells which fragment to load
        adapter = new TabLayoutAdapter(getSupportFragmentManager());// call of consturctor
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        prefs = getSharedPreferences("SharedPreference", 0); // 0 - for private mode
        editor = prefs.edit();
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            editor.putBoolean("firstrun", false).apply();
            setTabsandShowHints();


        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getBaseContext(), R.color.selectedTabColor); // change color of selected tab
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getBaseContext(), R.color.tabUnselectedColor);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new TabLayoutAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    public void onBackPressed() {
        ExitApp();
    }


    //DialogBox Main Function
    public void ExitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                HomeActivity.this);

        builder.setTitle("Exit?");//Title
        builder.setMessage("If you love the One Piece Wallpapers,please rate us");//Message
        builder.setIcon(R.drawable.app_icon);
        builder.setCancelable(false);


        //Negative Message
        builder.setNegativeButton("RATE US",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                        }


                    }
                });


        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //Positive Message
        builder.setPositiveButton("EXIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        /* close this activity
                         *  When Exit is clicked
                         */
                        finish();
                    }
                });

        builder.show();
    }

    public void setTabsandShowHints() {
        tabLayout.getTabAt(0).select();
        HintServiceImpl hintService = new HintServiceImpl(tabLayout);
        hintService.addHint(new Hint(newtab, "Get All the new Wallpapers Here", " ", 0));
        tabLayout.getTabAt(1).select();
        hintService.addHint(new Hint(categorytab, "Here Your all the Piece Character", " ", 1));
        hintService.addHint(new Hint(search, "Here Find your Favourite Character", " ", 1));
        hintService.addHint(new Hint(favtab, "Get All the favourite clicked Wallpaper Here", " ", 2));
        hintService.addHint(new Hint(download, "All the Downloads is here", " ", 2));
        hintService.showHint(this);

    }


}
