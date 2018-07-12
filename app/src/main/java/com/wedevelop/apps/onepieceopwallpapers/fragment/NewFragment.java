package com.wedevelop.apps.onepieceopwallpapers.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

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

public class NewFragment extends Fragment {

    List<Wallpaper> wallpaperList;
    RecyclerView recyclerView;
    WallpaperAdapter adapter;
    DatabaseReference dbWallpapers;
    private ProgressBar progressBar;
    Boolean isScrolling=false;
    Boolean shouldScrollMore=true;
    String oldestpost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.activity_wallpaper, container, false);

        return  rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wallpaperList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new WallpaperAdapter(getActivity(), wallpaperList);
        recyclerView.setAdapter(adapter);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        dbWallpapers = FirebaseDatabase.getInstance()
                .getReference("images")
                .child("new");

        // setting up the listner in the recyclerView For chechking if it is last data if yes load more
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isScrolling&&(shouldScrollMore)){
                    //fetch the new data
                    isScrolling=false;
                    fetchData();
                }
            }
        });




        // limit to reads data from bottom
        dbWallpapers.limitToLast(25).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                int x=0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapShot : dataSnapshot.getChildren()) {
                        Wallpaper w = wallpaperSnapShot.getValue(Wallpaper.class);
                        w.id = wallpaperSnapShot.getKey();
                        wallpaperList.add(w);
                        if(x==0){
                            oldestpost=w.id;
                            x++;
                        }
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

    private void fetchData() {
        DatabaseReference dbWallpaper=dbWallpapers;
        dbWallpaper.orderByKey().endAt(oldestpost).limitToLast(25).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                int x=0;
                List<Wallpaper> wallpaperListTemp=new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapShot : dataSnapshot.getChildren()) {
                        Wallpaper w = wallpaperSnapShot.getValue(Wallpaper.class);
                        String id = wallpaperSnapShot.getKey();
                        if(x==0&&(!oldestpost.equals(id))){
                            oldestpost=id;
                            x++;
                        }else
                        if (x==0&&(oldestpost.equals(id))){
                            shouldScrollMore=false;
                            return;
                        }
                        wallpaperListTemp.add(w);

                    }
                    wallpaperListTemp.remove(wallpaperListTemp.size()-1);
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
