package com.triple.astudio.onepieceopwallpapers.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.triple.astudio.onepieceopwallpapers.R;
import com.triple.astudio.onepieceopwallpapers.adapter.WallpaperAdapter;
import com.triple.astudio.onepieceopwallpapers.models.Wallpaper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WallpaperActivity extends AppCompatActivity {

    List<Wallpaper> wallpaperList;
    RecyclerView recyclerView;
    WallpaperAdapter adapter;
    DatabaseReference dbWallpapers;
    ProgressBar progressBar;
    Boolean isScrolling = false;
    GridLayoutManager manager;
    Boolean shouldScrollMore = true;
    String oldestpost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        wallpaperList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        manager = (GridLayoutManager) recyclerView.getLayoutManager();
        adapter = new WallpaperAdapter(this, wallpaperList);
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar);
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        final Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(category);
        setSupportActionBar(toolbar);
        // setting up the listner in the recyclerView For chechking if it is last data if yes load more
        dbWallpapers = FirebaseDatabase.getInstance().getReference("images")
                .child(category);
        progressBar.setVisibility(View.VISIBLE);
        dbWallpapers.orderByKey().limitToLast(25).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                //  Toast.makeText(getApplicationContext(), dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                int x = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapShot : dataSnapshot.getChildren()) {
                        String id = wallpaperSnapShot.getKey();
                        String title = wallpaperSnapShot.child("title").getValue(String.class);
                        String desc = wallpaperSnapShot.child("desc").getValue(String.class);
                        String url = wallpaperSnapShot.child("url").getValue(String.class);
                        if (x == 0) {
                            oldestpost = id;
                            x++;
                        }
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


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = manager.getChildCount();
                int totalItemCount = manager.getItemCount();
                int pastVisibleItems = manager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    if (isScrolling && (shouldScrollMore)) {
                        //fetch the new data
                        isScrolling = false;
                        fetchData();
                    }
                }
            }
        });


    }

    private void fetchData() {
        DatabaseReference dbWallpaper = dbWallpapers;

        dbWallpaper.orderByKey().endAt(oldestpost).limitToLast(20).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //  Toast.makeText(getApplicationContext(), dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                List<Wallpaper> wallpaperListTemp = new ArrayList<>();
                int x = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapShot : dataSnapshot.getChildren()) {
                        String id = wallpaperSnapShot.getKey();
                        Wallpaper w = wallpaperSnapShot.getValue(Wallpaper.class);
                        if (x == 0 && (!oldestpost.equals(id))) {
                            oldestpost = id;
                            x++;
                        } else if (x == 0 && (oldestpost.equals(id))) {
                            shouldScrollMore = false;
                            return;
                        }
                        wallpaperListTemp.add(w);

                    }
                    wallpaperListTemp.remove(wallpaperListTemp.size() - 1);
                    Collections.reverse(wallpaperList);
                    wallpaperListTemp.addAll(wallpaperList);
                    wallpaperList.clear();
                    wallpaperList.addAll(wallpaperListTemp);
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
