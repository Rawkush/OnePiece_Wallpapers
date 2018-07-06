package com.wedevelop.apps.onepieceopwallpapers.activity;


import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.models.Wallpaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DisplayImage extends AppCompatActivity {

    FloatingActionButton fab_more, fab_download, fab_set_wall, fab_share; // fab buttons on layout
    Animation OpenAnimation, CloseAnimation, clockwiseAnimation, AnticlockwiseAnimation;
    Boolean isOpen = false;
    LinearLayout LinearFabLayout;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        fab_more = findViewById(R.id.fab_more);
        fab_download = findViewById(R.id.fab_download);
        fab_set_wall = findViewById(R.id.fab_set_wall);
        fab_share = findViewById(R.id.fab_share);

        LinearFabLayout = findViewById(R.id.LinearFablayout);

        //initialising the animation variable
        OpenAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        CloseAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        clockwiseAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        AnticlockwiseAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        Intent intent = getIntent();
         url= intent.getStringExtra("wallpaper_url");
        PhotoView photoView = findViewById(R.id.photo_view);
        Glide.with(this)
                .load(url)
                .into(photoView);

        fab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) { // checking if the button is already clicked or not

                    fab_download.startAnimation(CloseAnimation);
                    fab_set_wall.startAnimation(CloseAnimation);
                    fab_more.startAnimation(AnticlockwiseAnimation);
                    fab_share.startAnimation(CloseAnimation);
                    fab_share.setClickable(false);
                    fab_set_wall.setClickable(false);
                    fab_download.setClickable(false);

                    LinearFabLayout.setVisibility(View.INVISIBLE);
                    isOpen = false;


                } else {
                    fab_download.startAnimation(OpenAnimation);
                    fab_set_wall.startAnimation(OpenAnimation);
                    fab_more.startAnimation(clockwiseAnimation);
                    fab_share.startAnimation(OpenAnimation);
                    fab_share.setClickable(true);
                    fab_set_wall.setClickable(true);
                    fab_download.setClickable(true);
                    LinearFabLayout.setVisibility(View.VISIBLE);
                    isOpen = true;
                }


            }
        });


    }







}

