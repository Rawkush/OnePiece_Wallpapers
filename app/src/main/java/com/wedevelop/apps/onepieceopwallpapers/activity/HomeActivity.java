package com.wedevelop.apps.onepieceopwallpapers.activity;


import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.wedevelop.apps.onepieceopwallpapers.HintServiceImpl;
import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.adapter.TabLayoutAdapter;
import com.wedevelop.apps.onepieceopwallpapers.models.Hint;

public class HomeActivity extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    TabLayoutAdapter adapter;

    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_tab_new,
            R.drawable.ic_random,
            R.drawable.ic_favorite
    };
    boolean doubleTap;
    View download, search, newtab, categorytab, favtab;
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

        PopUp();

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
        if (doubleTap) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Please Back again to Exit", Toast.LENGTH_SHORT).show();
            doubleTap = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleTap = false;
                }
            }, 1000);
        }
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

    public void PopUp() {
        new FancyAlertDialog.Builder(this)
                .setTitle("Rate us if you like the app")
                .setBackgroundColor(Color.parseColor("#000000"))  //Don't pass R.color.colorvalue
                .setMessage("Do you really want to Exit ?")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Rate")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        String playStoreMarketUrl = "market://details?id=";
                        String playStoreWebUrl = "https://play.google.com/store/apps/details?id=";
                        String packageName = getPackageName();
                        try {
                            Intent intent = getApplicationContext()
                                    .getPackageManager()
                                    .getLaunchIntentForPackage("com.android.vending");
                            if (intent != null) {
                                ComponentName androidComponent = new ComponentName("com.android.vending",
                                        "com.wedevelop.apps.onepieceopwallpapers.activity");
                                intent.setComponent(androidComponent);
                                intent.setData(Uri.parse(playStoreMarketUrl + packageName));
                            } else {
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreMarketUrl + packageName));
                            }
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreWebUrl + packageName));
                            startActivity(intent);
                        }
                        // Toast.makeText(getApplicationContext(),"Rate",Toast.LENGTH_SHORT).show();
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        //Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }




}
