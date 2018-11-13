
package com.triple.astudio.onepieceopwallpapers.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;
import com.triple.astudio.onepieceopwallpapers.R;
public class OnBoardingActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(new Step.Builder().setTitle("Lets Start")
                .setContent("Don't miss a Top One Piece Wallpapers")
                .setBackgroundColor(Color.parseColor("#FF000000")) // int background color
                .setDrawable(R.drawable.two) // int top drawable
                .setSummary("Continue...")
                .build());

        addFragment(new Step.Builder().setTitle("Updated Daily")
                .setContent("High quality Content within the app every day")
                .setBackgroundColor(Color.parseColor("#FF000000")) // int background color
                .setDrawable(R.drawable.three) // int top drawable
                .setSummary("Continue...")
                .build());

        addFragment(new Step.Builder().setTitle("One Piece Wallpapers")
                .setContent("Find Top Wallpapers of your favourite Anime One Piece")
                .setBackgroundColor(Color.parseColor("#FF000000")) // int background color
                .setDrawable(R.drawable.one) // int top drawable
                .setSummary("Here We Go...")
                .build());

        setFinishText("Start");

    }

    @Override
    public void finishTutorial() {
        // Your implementation
        Intent intent = new Intent(OnBoardingActivity.this, HomeActivity.class);
        startActivity(intent);

        finish();
    }


}
