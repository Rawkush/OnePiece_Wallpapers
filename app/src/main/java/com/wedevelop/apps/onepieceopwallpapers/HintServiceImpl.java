package com.wedevelop.apps.onepieceopwallpapers;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.wedevelop.apps.onepieceopwallpapers.models.Hint;

import java.util.ArrayList;
import java.util.List;

public class HintServiceImpl {

    public static int count;
    View currentView;
    ArrayList<Hint> hintList;
    TabLayout tabLayout=null;

    public HintServiceImpl() {
        count = 0;
        hintList = new ArrayList<Hint>();
    }
    public HintServiceImpl(TabLayout tabLayout) {
        count = 0;
        hintList = new ArrayList<Hint>();
        this.tabLayout=tabLayout;
    }

    public void addHint(Hint hint) {

        hintList.add(hint);

    }


    public void showHint(final Activity activity) {

        if(tabLayout!=null){
            tabLayout.getTabAt(hintList.get(count).getTabloc()).select();
        }

        TapTargetView.showFor(activity,                 // `this` is an Activity
                TapTarget.forView(hintList.get(count).view, hintList.get(count).title, hintList.get(count).desc)
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.colorAccent)  // Specify the color of the description text
                        .textColor(R.color.progressBarColor)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.progressBarColor)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        //.icon(Drawable)                     // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        // doSomething();

                        count++;
                        if (count < hintList.size()) {
                            showHint(activity);
                        }
                    }
                });


    }


}
