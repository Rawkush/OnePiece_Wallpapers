package com.wedevelop.apps.onepieceopwallpapers.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class DownloadsGallery {
    File[] allFiles;
    Activity activity;

    public DownloadsGallery(Activity activity) {
        this.activity = activity;
    }

    public void start() {
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/OnePiece_Wallpapers/");
        allFiles = folder.listFiles();
        new SingleMediaScanner(activity, allFiles[0]);

    }

    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        public void onScanCompleted(String path, Uri uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            activity.startActivity(intent);
            mMs.disconnect();
        }

    }
}
