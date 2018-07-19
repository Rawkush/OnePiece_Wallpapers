package com.wedevelop.apps.onepieceopwallpapers.models;

import android.view.View;

public class Hint {

    public View view;
    public String title;
    public String desc;
    int tabloc=-1;

    public Hint(View view, String title, String desc) {
        this.view = view;
        this.title = title;
        this.desc = desc;

    }

    public Hint(View view, String title, String desc, int tabloc) {
        this.view = view;
        this.title = title;
        this.desc = desc;
        this.tabloc = tabloc;
    }

    public int getTabloc() {
        return tabloc;
    }
}
