package com.wedevelop.apps.onepieceopwallpapers.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.models.Wallpaper;

public class displayImage extends AppCompatActivity {

    FloatingActionButton fab_more, fab_download, fab_set_wall,fab_share; // fab buttons on layout
    Animation OpenAnimation,CloseAnimation,clockwiseAnimatioin,AnticlockwiseAnimation;
    Boolean isOpen= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        fab_more=(FloatingActionButton)findViewById(R.id.fab_more);
        fab_download=(FloatingActionButton) findViewById(R.id.fab_download);
        fab_set_wall=(FloatingActionButton) findViewById(R.id.fab_set_wall);
        fab_share=(FloatingActionButton)findViewById(R.id.fab_set_wall);
        //initialising the animation variable
        OpenAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);

        CloseAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        clockwiseAnimatioin= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        AnticlockwiseAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        Bitmap bitmap = Wallpaper.image;
        ImageView img = (ImageView) findViewById(R.id.displayImageView);
        img.setImageBitmap(bitmap);

        fab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    fab_download.startAnimation(CloseAnimation);
                    fab_set_wall.startAnimation(CloseAnimation);
                    fab_more.startAnimation(AnticlockwiseAnimation);
                    fab_share.startAnimation(CloseAnimation);
                    fab_share.setClickable(false);
                    fab_set_wall.setClickable(false);
                    fab_download.setClickable(false);
                    isOpen=false;
                }
                else{
                    fab_download.startAnimation(OpenAnimation);
                    fab_set_wall.startAnimation(OpenAnimation);
                    fab_more.startAnimation(clockwiseAnimatioin);
                    fab_share.startAnimation(OpenAnimation);
                    fab_share.setClickable(true);
                    fab_set_wall.setClickable(true);
                    fab_download.setClickable(true);
                    isOpen=true;
                }



            }
        });


    }
}
