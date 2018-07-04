package com.wedevelop.apps.onepieceopwallpapers.adapter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.fragment.CategoryFragment;
import com.wedevelop.apps.onepieceopwallpapers.fragment.FavouriteFragment;
import com.wedevelop.apps.onepieceopwallpapers.fragment.NewFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    private int[] tabIcons = {
            R.drawable.ic_tab_new,
            R.drawable.ic_random,
            R.drawable.ic_favorite
    };

    public TabLayoutAdapter(FragmentManager fm) {

        // this is constructor of the adapter
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        //TODO: return the fragment according to the position

        if(position==0)




            return new NewFragment();
        else
        if(position==1)



            return new CategoryFragment();
            //setting deafult
        else


            return new FavouriteFragment();     // for position 3
    }

    @Override
    public int getCount() {

        //return the number of fragments you will be using
        //  TODO: change the return from 0 to number of fragments
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        //  TODO: change the named  of the tabs here

        if(position==0)
            return "New";
        else
        if(position==1)
            return "Category";
            //setting deafult
        else
            return "Favourite"; // for position 3
    }

    public void setupTabIcons(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }




}
