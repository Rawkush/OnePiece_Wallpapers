package com.wedevelop.apps.onepieceopwallpapers.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wedevelop.apps.onepieceopwallpapers.HintServiceImpl;
import com.wedevelop.apps.onepieceopwallpapers.R;
import com.wedevelop.apps.onepieceopwallpapers.activity.DownloadsGallery;
import com.wedevelop.apps.onepieceopwallpapers.activity.FeedBackActivity;
import com.wedevelop.apps.onepieceopwallpapers.models.Hint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FavouriteLoginFragment extends Fragment {

    private static final int GOOGLE_SIGN_IN_CODE = 212;
    private GoogleSignInClient mGoogleSignInClient;
    private View rootView = null;
    android.support.v7.widget.Toolbar mToolbar;
    RelativeLayout loginlayout, imglayout;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FavouritesFragment favouritesFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(R.layout.fragment_fav_default, container, false);
        mToolbar = v.findViewById(R.id.menuToolBar);

        if (mToolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        loginlayout = view.findViewById(R.id.FavLogin);
        imglayout = view.findViewById(R.id.favFragment);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressbar);
        favouritesFragment = new FavouritesFragment(recyclerView, progressBar);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            loginlayout.setVisibility(View.GONE);
            imglayout.setVisibility(View.VISIBLE);
            setImglayout();
            rootView = view;
            setHasOptionsMenu(true);

        } else {

            loginlayout.setVisibility(View.VISIBLE);
            imglayout.setVisibility(View.GONE);
            view.findViewById(R.id.button_google_sign_in).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(intent, GOOGLE_SIGN_IN_CODE);
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                fireballAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void fireballAuthWithGoogle(GoogleSignInAccount account) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(),
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            loginlayout.setVisibility(View.GONE);
                            imglayout.setVisibility(View.VISIBLE);
                            setImglayout();
                            Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(getActivity(), "Login Failure", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // refresh after removing the image
    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        if (rootView != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            favouritesFragment.resetList();
            setImglayout();
        }
    }


    public void setImglayout() {
        favouritesFragment.setFavWalls(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO  menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fav_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menuDownload) {

            downloadGallery();

        } else if (id == R.id.menuFeedback) {
            Intent feedback = new Intent(getContext(), FeedBackActivity.class);
            startActivity(feedback);
            // Toast.makeText(getActivity(), "feedback is here", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    private void downloadGallery() {

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat
                        .shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
            }
            return;
        }

        if (checkDir()) {
            DownloadsGallery downloadsGallery = new DownloadsGallery(getActivity());
            downloadsGallery.start();
        }
    }

    private boolean checkDir() {
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/OnePiece_Wallpapers/");

        if (!folder.exists()) {
            folder.mkdirs();
            return false;

        } else {
            Log.e("Found Dir", "Found Dir  ");

            File[] contents = folder.listFiles();
// the directory file is not really a directory..
            if (contents == null) {
                Toast.makeText(getActivity(), "Nothing Downloaded yet", Toast.LENGTH_SHORT).show();

                return false;

            }
// Folder is empty
            else if (contents.length == 0) {
                Toast.makeText(getActivity(), "Nothing Downloaded yet", Toast.LENGTH_SHORT).show();

                return false;
            }
// Folder contains files
            else {
                return true;

            }
        }
    }

}
