package com.wedevelop.apps.onepieceopwallpapers.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;


import com.wedevelop.apps.onepieceopwallpapers.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    SharedPreferences appSettings;
    private static int SPLASH_TIME_OUT = 1500;
    private boolean InternetCheck = true;
    private ProgressBar spinner;
    boolean firstTime = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appSettings = getSharedPreferences("APP_NAME", MODE_PRIVATE);
        // Make sure you only run addShortcut() once, not to create duplicate shortcuts.
        if (!appSettings.getBoolean("shortcut", false)) {
            addShortcut();
        }

        prefs = getSharedPreferences("SharedPreferences", 0); // 0 - for private mode
        editor = prefs.edit();
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            editor.putBoolean("firstrun", false).apply();
            firstTime = true;

        }

        //progressBar
        spinner = findViewById(R.id.SplashProgressBar);
        //DoubleBounce doubleBounce = new DoubleBounce();
        //spinner.setIndeterminateDrawable(doubleBounce);
        spinner.setVisibility(View.VISIBLE);
        PostDelayedMethod();

    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "One Piece Wallpapers");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.app_icon));
        addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);

        SharedPreferences.Editor prefEditor = appSettings.edit();
        prefEditor.putBoolean("shortcut", true);
        prefEditor.commit();
    }


    public void PostDelayedMethod() {

        new Handler().postDelayed(new Runnable() {


            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                // This method will be executed once the timer is over
                // Start your app main activity

                boolean InternetResult = checkConnection();
                if (InternetResult) {

                    //open Activity when internet is connected

                    if (firstTime) {
                        Intent intent = new Intent(MainActivity.this, OnBoardingActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    finish();

                } else {


                    spinner.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);


                    //Dialog Box show when internet is not connected
                    DialogAppear();


                }
            }
        }, SPLASH_TIME_OUT);
    }


    //DialogBox Main Function
    public void DialogAppear() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);

        builder.setTitle("Network Error");   //Title
        builder.setMessage("No Internet Connectivity");//Message
        builder.setCancelable(false);


        //Negative Message
        builder.setNegativeButton("Exit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        /* close this activity
                         *  When Exit is clicked
                         */
                        finish();

                    }
                });

        //Positive Message
        builder.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        //Check internet again when click on Retry by calling function

                        //run is not working there due to runnable method
                        // run();
                        spinner.setVisibility(View.VISIBLE);

                        PostDelayedMethod();

                    }
                });
        builder.show();
    }


    //Check Internet status of the mobile
    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    //Return Internet Status of the Mobile
    public boolean checkConnection() {
        if (isOnline()) {
            return InternetCheck;
            //Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        } else {
            InternetCheck = false;
            return InternetCheck;
            // Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();

        }

    }


}
