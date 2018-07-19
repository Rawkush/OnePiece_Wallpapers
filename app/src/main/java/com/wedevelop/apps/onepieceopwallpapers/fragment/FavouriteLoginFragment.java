package com.wedevelop.apps.onepieceopwallpapers.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.wedevelop.apps.onepieceopwallpapers.models.Hint;

public class FavouriteLoginFragment extends Fragment {

    private static final int GOOGLE_SIGN_IN_CODE = 212;
    private GoogleSignInClient mGoogleSignInClient;
    private View rootView = null;
    android.support.v7.widget.Toolbar mToolbar;
    RelativeLayout loginlayout, imglayout;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    MenuItem download;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .build();

        prefs = getActivity().getSharedPreferences("MyPref3", 0); // 0 - for private mode
        editor = prefs.edit();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        loginlayout = view.findViewById(R.id.FavLogin);
        imglayout = view.findViewById(R.id.favFragment);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            loginlayout.setVisibility(View.GONE);
            imglayout.setVisibility(View.VISIBLE);
            setImglayout();
            download = view.findViewById(R.id.menuDownload);
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
            setImglayout();
        }
    }


    public void setImglayout() {
        FavouritesFragment favouritesFragment = new FavouritesFragment(rootView);
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
            Toast.makeText(getActivity(), "Download is here", Toast.LENGTH_SHORT).show();
            DownloadsGallery downloadsGallery = new DownloadsGallery(getActivity());
            downloadsGallery.start();
        } else if (id == R.id.menuFeedback) {
            Toast.makeText(getActivity(), "feedback is here", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    public void showHint() {
        HintServiceImpl hintService = new HintServiceImpl();
        hintService.addHint(new Hint((View) download, "Here You Can Search Your Favourite Character", " "));
        if (download != null)
            hintService.showHint(getActivity());
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //Write down your refresh code here, it will call every time user come to this fragment.
            if (FirebaseAuth.getInstance().getCurrentUser() != null)
                if (prefs.getBoolean("firstrun", true)) {
                    // Do first run stuff here then set 'firstrun' as false
                    // using the following line to edit/commit prefs
                    editor.putBoolean("firstrun", false).apply();
                    showHint();

                }

        }
    }

}
