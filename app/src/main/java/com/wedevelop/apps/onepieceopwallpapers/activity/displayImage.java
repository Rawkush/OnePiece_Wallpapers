package com.wedevelop.apps.onepieceopwallpapers.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.wedevelop.apps.onepieceopwallpapers.R;

public class displayImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        Intent intent =getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");

        ImageView img = (ImageView) findViewById(R.id.displayImageView);
        img.setImageBitmap(bitmap);
    }
}
