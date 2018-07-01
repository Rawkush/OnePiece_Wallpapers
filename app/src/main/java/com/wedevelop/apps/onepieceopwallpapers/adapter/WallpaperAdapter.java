package com.wedevelop.apps.onepieceopwallpapers.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.activity.DisplayImage;
import com.wedevelop.apps.onepieceopwallpapers.models.Wallpaper;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.wallpaperViewHolder> {
    private Context mCtx;
    private List<Wallpaper> wallpaperList;
    private String wallpaper;

    public WallpaperAdapter(Context mCtx, List<Wallpaper> wallpaperList) {
        this.mCtx = mCtx;
        this.wallpaperList = wallpaperList;
    }

    @NonNull
    @Override
    public wallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_wallpaper, parent, false);
        return new wallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull wallpaperViewHolder holder, int position) {
        Wallpaper w = wallpaperList.get(position);
        Glide.with(mCtx)
                .load(w.url)
                .into(holder.imageView);
        wallpaper = w.url;

    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    class wallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public wallpaperViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int p = getAdapterPosition();
            // caching the image loaded from the glide into PhotoView

            Intent intent = new Intent(mCtx, DisplayImage.class);
            intent.putExtra("wallpaper_url", wallpaperList.get(p).url);
            mCtx.startActivity(intent);
        }
    }
}
