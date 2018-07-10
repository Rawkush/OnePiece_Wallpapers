package com.wedevelop.apps.onepieceopwallpapers.models;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

public class Wallpaper {

    @Exclude
    public String id;

    public String title, desc, url;

    public Wallpaper() {

    }

    public Wallpaper(String id, String title, String desc, String url) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
    }
}
