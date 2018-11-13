package com.wedevelop.apps.onepieceopwallpapers;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class AdsTimer {

    static boolean showAdsOrNot;
    CountDownTimer countDownTimer;
    int timeLeft;
    TextView timerTextView;

    public AdsTimer() {
        showAdsOrNot = true;
        timeLeft = 0;

    }

    public void stopAds(int time, TextView timerTextView) {
        this.timerTextView = timerTextView;
        showAdsOrNot = false;

        countDownTimer = new CountDownTimer(time * 1000 + 100, 1000) {  // first param is timer till which to count
            // second param is in what intervals should it count
            @Override
            public void onTick(long millisUntilFinished) {
                // after every second
                updateTime((int) (millisUntilFinished / 1000));


            }

            @Override
            public void onFinish() {
                // when conter is finished(after 10 sec)
                resetTimer();
                //TODO: display ads
                showAdsOrNot = true;

            }
        }.start();


    }


    /*

      updating time here*

    */

    private void updateTime(int secondsLeft) {
        timeLeft = secondsLeft;
        int minutes = (int) secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;
        // timerTextView.setText(Integer.toString(minutes)+":"+Integer.toString(seconds));
    }


    public void resetTimer() {
        countDownTimer.cancel();
        showAdsOrNot = true;
        // timerTextView.setVisibility(View.GONE);
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public boolean ShowAdsOrNot() {
        return showAdsOrNot;
    }
}
