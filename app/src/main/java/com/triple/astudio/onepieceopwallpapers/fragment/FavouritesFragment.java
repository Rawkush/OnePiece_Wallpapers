package com.triple.astudio.onepieceopwallpapers.fragment;

import android.app.Activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.triple.astudio.onepieceopwallpapers.adapter.WallpaperAdapter;
import com.triple.astudio.onepieceopwallpapers.models.Wallpaper;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment {


    List<Wallpaper> favWalls;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    WallpaperAdapter adapter;
    private DatabaseReference dbFavs;


    public FavouritesFragment(RecyclerView recyclerView, ProgressBar progressBar) {

        favWalls = new ArrayList<>();
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
    }

    public void resetList() {
        favWalls.clear();
    }

    public void setFavWalls(final Activity activity) {
        adapter = new WallpaperAdapter(activity, favWalls);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerView.setAdapter(adapter);

        dbFavs = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites");
        progressBar.setVisibility(View.VISIBLE);

        dbFavs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    int x = 0;
                    favWalls.clear();
                    for (DataSnapshot wallpaperSnapShot : dataSnapshot.getChildren()) {
                        String id = wallpaperSnapShot.getKey();
                        String title = wallpaperSnapShot.child("title").getValue(String.class);
                        String desc = wallpaperSnapShot.child("desc").getValue(String.class);
                        String url = wallpaperSnapShot.child("url").getValue(String.class);
                        Wallpaper w = new Wallpaper(id, title, desc, url);
                        favWalls.add(w);
                        x++;
                    }


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
