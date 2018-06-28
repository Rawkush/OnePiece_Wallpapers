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

    FloatingActionButton fab_more, fab_download, fab_set_wall;
    Animation FabOpenAnimation,FabCloseAnimation,FabRclockwiseAnimatioin,FabAnticlockwiseAnimation;
    Boolean isOpen= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        fab_more=(FloatingActionButton)findViewById(R.id.fab_more);
        fab_download=(FloatingActionButton) findViewById(R.id.fab_download);
        fab_set_wall=(FloatingActionButton) findViewById(R.id.fab_set_wall);
        //initialising the animation variable
        FabOpenAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);

        FabCloseAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRclockwiseAnimatioin= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabAnticlockwiseAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        Bitmap bitmap = Wallpaper.image;
        ImageView img = (ImageView) findViewById(R.id.displayImageView);
        img.setImageBitmap(bitmap);

        fab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    fab_download.startAnimation(FabCloseAnimation);
                    fab_set_wall.startAnimation(FabCloseAnimation);
                    fab_more.startAnimation(FabAnticlockwiseAnimation);
                    fab_set_wall.setClickable(false);
                    fab_download.setClickable(false);
                    isOpen=false;
                }
                else{
                    fab_download.startAnimation(FabOpenAnimation);
                    fab_set_wall.startAnimation(FabOpenAnimation);
                    fab_more.startAnimation(FabRclockwiseAnimatioin);
                    fab_set_wall.setClickable(true);
                    fab_download.setClickable(true);
                    isOpen=true;
                }



            }
        });


    }
}
