package com.wedevelop.apps.onepieceopwallpapers.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.wedevelop.apps.onepieceopwallpapers.AdsTimer;
import com.wedevelop.apps.onepieceopwallpapers.R;

public class AdFreeDialogFragment extends AppCompatDialogFragment {
    AdsTimer adsTimer;
    TextView textView;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Create the view to show
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.ad_free_dialog_message, null);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                loadAds();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                int x = adsTimer.getTimeLeft();
                adsTimer.resetTimer();
                adsTimer.stopAds(x + 30, textView);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(getContext(), "LoadFailed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {


            }
        });
        loadAds();
        adsTimer = new AdsTimer();
        textView = v.findViewById(R.id.AdsRemainingTime);

        //Create a button Listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //call Reward ad
                        showAds();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //Don't do anything just end.
                        break;
                }
            }
        };

        //Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Rewarded Ad")
                .setView(v)
                .setPositiveButton(R.string.watch_video, listener)
                .setNegativeButton(R.string.cancel, listener)
                .create();

    }


    private void showAds() {

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            Toast.makeText(getContext(), "LoadFailed", Toast.LENGTH_SHORT).show();

        }
    }


    private void loadAds() {

        //TODO load the reawrding ads here
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder()
                        .build());


    }


    @Override
    public void onResume() {
        mRewardedVideoAd.resume(getContext());
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(getContext());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(getContext());
        super.onDestroy();
    }


}
