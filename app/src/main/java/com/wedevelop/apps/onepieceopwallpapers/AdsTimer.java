package com.wedevelop.apps.onepieceopwallpapers;

import android.os.CountDownTimer;
import android.view.View;

public class AdsTimer {

    CountDownTimer countDownTimer;
    boolean showAdsOrNot;
    int timeLeft;

    public AdsTimer(){
        showAdsOrNot=true;
        timeLeft=0;

    }

    public void controlTimer(int time){

            countDownTimer=  new CountDownTimer( time*1000 + 100, 1000) {  // first param is timer till which to count
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
                    showAdsOrNot=true;
                    //TODO: display ads

                }
            }.start();


    }


    /*

      updating time here*

    */

  public void updateTime(int secondsLeft){

        timeLeft=secondsLeft;
        int minutes =(int) secondsLeft/ 60;
        int seconds= secondsLeft-minutes*60;
        //timerTextView.setText(Integer.toString(minutes)+":"+Integer.toString(seconds));
    }

    public int getTimeLeft(){
      return timeLeft;
    }

    public void resetTimer(){
        countDownTimer.cancel();
      //  timerTextView.setVisibility(View.GONE);
    }

    public boolean ShowAdsOrNot() {
        return showAdsOrNot;
    }
}
