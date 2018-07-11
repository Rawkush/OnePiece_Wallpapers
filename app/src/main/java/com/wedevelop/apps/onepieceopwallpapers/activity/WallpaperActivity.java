package com.wedevelop.apps.onepieceopwallpapers.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.adapter.WallpaperAdapter;
import com.wedevelop.apps.onepieceopwallpapers.models.Wallpaper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WallpaperActivity extends AppCompatActivity {

    List<Wallpaper> wallpaperList;
    RecyclerView recyclerView;
    WallpaperAdapter adapter;

    DatabaseReference dbWallpapers;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        wallpaperList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        // GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //gridLayoutManager.setReverseLayout(true);
        //recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new WallpaperAdapter(this, wallpaperList);
        recyclerView.setAdapter(adapter);

        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(category);
        setSupportActionBar(toolbar);

        dbWallpapers = FirebaseDatabase.getInstance().getReference("images")
                .child(category);
        progressBar.setVisibility(View.VISIBLE);
        dbWallpapers.limitToLast(25).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                //  Toast.makeText(getApplicationContext(), dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapShot : dataSnapshot.getChildren()) {
                        String id = wallpaperSnapShot.getKey();
                        String title = wallpaperSnapShot.child("title").getValue(String.class);
                        String desc = wallpaperSnapShot.child("desc").getValue(String.class);
                        String url = wallpaperSnapShot.child("url").getValue(String.class);

                        Wallpaper w = new Wallpaper(id, title, desc, url);
                        w.id = dataSnapshot.getKey();
                        w.id = w.id + wallpaperSnapShot.getKey();
                        wallpaperList.add(w);
                    }
                    Collections.reverse(wallpaperList);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
