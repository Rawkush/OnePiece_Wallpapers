package com.wedevelop.apps.onepieceopwallpapers.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import com.wedevelop.apps.onepieceopwallpapers.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class FeedBackActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0.2");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("If you love the Content in this app then please Rate us")
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail("appswedevelop@gmail.com")
                .addPlayStore("com.wedevelop.apps.onepieceopwallpapers")
                .create();

        setContentView(aboutPage);

    }
}
