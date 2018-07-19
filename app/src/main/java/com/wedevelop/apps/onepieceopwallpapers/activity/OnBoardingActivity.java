package com.wedevelop.apps.onepieceopwallpapers.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;
import com.wedevelop.apps.onepieceopwallpapers.R;

public class OnBoardingActivity extends TutorialActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(new Step.Builder().setTitle("This is header 1")
                .setContent("This is content")
                .setBackgroundColor(Color.parseColor("#FF0957")) // int background color
                .setDrawable(R.drawable.one) // int top drawable
                .setSummary("This is summary")
                .build());

        addFragment(new Step.Builder().setTitle("This is header 2")
                .setContent("This is content")
                .setBackgroundColor(Color.parseColor("#FF0957")) // int background color
                .setDrawable(R.drawable.two) // int top drawable
                .setSummary("This is summary")
                .build());

        addFragment(new Step.Builder().setTitle("This is header 3")
                .setContent("This is content")
                .setBackgroundColor(Color.parseColor("#FF0957")) // int background color
                .setDrawable(R.drawable.three) // int top drawable
                .setSummary("This is summary")
                .build());

    }
}

