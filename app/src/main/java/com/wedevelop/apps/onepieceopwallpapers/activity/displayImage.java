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
import android.widget.LinearLayout;

import com.github.chrisbanes.photoview.PhotoView;
import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.models.Wallpaper;

public class displayImage extends AppCompatActivity {

    FloatingActionButton fab_more, fab_download, fab_set_wall, fab_share; // fab buttons on layout
    Animation OpenAnimation, CloseAnimation, clockwiseAnimation, AnticlockwiseAnimation;
    Boolean isOpen = false;
    LinearLayout LinearFabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        fab_more = (FloatingActionButton) findViewById(R.id.fab_more);
        fab_download = (FloatingActionButton) findViewById(R.id.fab_download);
        fab_set_wall = (FloatingActionButton) findViewById(R.id.fab_set_wall);
        fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        LinearFabLayout = (LinearLayout) findViewById(R.id.LinearFablayout);

        //initialising the animation variable
        OpenAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);

        CloseAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        clockwiseAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        AnticlockwiseAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        Bitmap bitmap = Wallpaper.image;

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);

        photoView.setImageBitmap(bitmap);

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
