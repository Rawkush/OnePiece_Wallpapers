package com.wedevelop.apps.onepieceopwallpapers.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wedevelop.apps.onepieceopwallpapers.AdsTimer;
import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.activity.WallpaperActivity;
import com.wedevelop.apps.onepieceopwallpapers.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>
        implements Filterable {

    private AdsTimer adsTimer;
    private Context mCtx;
    private List<Category> categoryList;
    private List<Category> categoryListFiltered;
    //  private CategoriesAdapterListener listener;

    private InterstitialAd mInterstitialAd;


    public CategoriesAdapter(Context mCtx, List<Category> categoryList) {
        this.mCtx = mCtx;
        this.categoryList = categoryList;
        categoryListFiltered = categoryList;
        adsTimer= new AdsTimer();
        mInterstitialAd = new InterstitialAd(mCtx);
        mInterstitialAd.setAdUnitId("ca-app-pub-1544647693026779/3641837478");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_category, parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category c = categoryListFiltered.get(position);

        holder.textView.setText(c.name);
        Glide.with(mCtx)
                .load(c.thumb)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return categoryListFiltered.size();
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    categoryListFiltered = categoryList;
                } else {
                    List<Category> filteredList = new ArrayList<>();
                    for (Category row : categoryList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    categoryListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = categoryListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                categoryListFiltered = (ArrayList<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        ImageView imageView;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view_cat_name);
            imageView = itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            gotoNextPage();
            if (mInterstitialAd.isLoaded()&&adsTimer.ShowAdsOrNot()) {
                mInterstitialAd.show();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        }

        public void gotoNextPage() {
            int p = getAdapterPosition();  // getting adapter position
            Category c = categoryListFiltered.get(p);
            Intent intent = new Intent(mCtx, WallpaperActivity.class);
            intent.putExtra("category", c.name);
            mCtx.startActivity(intent);
        }


    }

}
