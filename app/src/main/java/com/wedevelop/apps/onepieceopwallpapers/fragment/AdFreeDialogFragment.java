package com.wedevelop.apps.onepieceopwallpapers.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.wedevelop.apps.onepieceopwallpapers.R;

public class AdFreeDialogFragment extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Create the view to show
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.ad_free_dialog_message, null);

        //Create a button Listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //call Reward ad
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
}
