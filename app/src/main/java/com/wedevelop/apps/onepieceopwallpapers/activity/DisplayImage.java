package com.wedevelop.apps.onepieceopwallpapers.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.wedevelop.apps.onepieceopwallpapers.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DisplayImage extends AppCompatActivity {

    FloatingActionButton fab_more, fab_download, fab_set_wall, fab_share; // fab buttons on layout
    Animation OpenAnimation, CloseAnimation, clockwiseAnimation, AnticlockwiseAnimation;
    Boolean isOpen = false;
    LinearLayout LinearFabLayout;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    String url, id;

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
        url = intent.getStringExtra("wallpaper_url");
        id = intent.getStringExtra("id");

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


        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWallpaper();

            }
        });
        fab_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadWallpaper();
            }
        });


    }


    private void shareWallpaper() {

        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                              Intent intent = new Intent(Intent.ACTION_SEND);
                              intent.setType("image/*");
                              intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource));
                              startActivity(Intent.createChooser(intent, "One Piece Wallpaper"));
                          }
                      }
                );
    }

    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "OP_Wallpaper_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void downloadWallpaper() {

        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                              Intent intent = new Intent(Intent.ACTION_VIEW);

                              Uri uri = saveWallpaperAndGetUri(resource, id);
                              if (uri != null) {
                                 // intent.setDataAndType(uri, "image/*");
                                  //startActivity(Intent.createChooser(intent, "One Piece Wallpaper"));

                              }
                          }
                      }
                );
    }


    private Uri saveWallpaperAndGetUri(Bitmap bitmap, String id) {
        Toast.makeText(this, "Downloaded", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat
                    .shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
            return null;
        }

        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/OnePiece_Wallpapers");
        folder.mkdirs();

        File file = new File(folder, id  + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            return Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




}

