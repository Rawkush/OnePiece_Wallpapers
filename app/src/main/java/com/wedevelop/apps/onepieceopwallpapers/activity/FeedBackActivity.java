package com.wedevelop.apps.onepieceopwallpapers.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import com.wedevelop.apps.onepieceopwallpapers.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class FeedBackActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        toolbar = findViewById(R.id.feed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");

        findViewById(R.id.btn_ContactUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent testIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "Feedback" + "&body=" + " " + "&to=" + "appswedevelop@gmail.com");
                testIntent.setData(data);
                startActivity(testIntent);

            }
        });

        findViewById(R.id.btn_RateUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Rate us Button clicked", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
