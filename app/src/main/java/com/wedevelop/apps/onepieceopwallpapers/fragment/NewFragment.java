package com.wedevelop.apps.onepieceopwallpapers.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.activity.DownloadsGallery;
import com.wedevelop.apps.onepieceopwallpapers.activity.FeedBackActivity;
import com.wedevelop.apps.onepieceopwallpapers.adapter.WallpaperAdapter;
import com.wedevelop.apps.onepieceopwallpapers.models.Wallpaper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewFragment extends Fragment {

    List<Wallpaper> wallpaperList;
    RecyclerView recyclerView;
    WallpaperAdapter adapter;
    DatabaseReference dbWallpapers;
    ProgressBar progressBar;
    Boolean isScrolling = false;
    GridLayoutManager manager;
    Boolean shouldScrollMore;
    String oldestpost;
    android.support.v7.widget.Toolbar mToolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.activity_wallpaper, container, false);
        mToolbar = v.findViewById(R.id.toolBar);

        if (mToolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        wallpaperList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        manager = (GridLayoutManager) recyclerView.getLayoutManager();
        adapter = new WallpaperAdapter(getActivity(), wallpaperList);
        recyclerView.setAdapter(adapter);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        shouldScrollMore = true;
        dbWallpapers = FirebaseDatabase.getInstance()
                .getReference("images")
                .child("new");

        // limit to reads data from bottom
        dbWallpapers.limitToLast(35).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                int x = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapShot : dataSnapshot.getChildren()) {
                        Wallpaper w = wallpaperSnapShot.getValue(Wallpaper.class);
                        w.id = wallpaperSnapShot.getKey();
                        wallpaperList.add(w);
                        if (x == 0) {
                            oldestpost = w.id;
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

                        // display message for loading


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
        dbWallpaper.orderByKey().endAt(oldestpost).limitToLast(35).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int x = 0;
                List<Wallpaper> wallpaperListTemp = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapShot : dataSnapshot.getChildren()) {
                        Wallpaper w = wallpaperSnapShot.getValue(Wallpaper.class);
                        String id = wallpaperSnapShot.getKey();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO  menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fav_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menuDownload) {

            downloadGallery();

        } else if (id == R.id.menuFeedback) {
            Intent feedback = new Intent(getContext(), FeedBackActivity.class);
            startActivity(feedback);
            // Toast.makeText(getActivity(), "feedback is here", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menuAdsFree) {
            FragmentManager manager = getFragmentManager();
            AdFreeDialogFragment dialogFragment = new AdFreeDialogFragment();
            dialogFragment.show(manager, "AD Free Dialog");
        }

        return super.onOptionsItemSelected(item);
    }


    private void downloadGallery() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat
                    .shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
            return;
        }

        if (checkDir()) {
            DownloadsGallery downloadsGallery = new DownloadsGallery(getActivity());
            downloadsGallery.start();
        }
    }

    private boolean checkDir() {
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/OnePiece_Wallpapers/");

        if (!folder.exists()) {

            folder.mkdirs();
            return false;

        } else {
            Log.e("Found Dir", "Found Dir  ");

            File[] contents = folder.listFiles();
// the directory file is not really a directory..
            if (contents == null) {
                Toast.makeText(getActivity(), "Nothing Downloaded yet", Toast.LENGTH_SHORT).show();

                return false;

            }
// Folder is empty
            else if (contents.length == 0) {
                Toast.makeText(getActivity(), "Nothing Downloaded yet", Toast.LENGTH_SHORT).show();

                return false;
            }
// Folder contains files
            else {
                return true;

            }
        }
    }
}
