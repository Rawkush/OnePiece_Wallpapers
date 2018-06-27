package com.wedevelop.apps.onepieceopwallpapers.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.models.Wallpaper;

public class displayImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        Bitmap bitmap = Wallpaper.image;
        ImageView img = (ImageView) findViewById(R.id.displayImageView);
        img.setImageBitmap(bitmap);
    }
}
