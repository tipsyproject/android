package com.tipsy.app;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
/*
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
*/

import com.tipsy.lib.TipsyUser;


/**
 * Created by Alexandre on 04/01/14.
 */
public class TypeSignUpFragment extends Fragment {//implements ConnectionCallbacks, OnConnectionFailedListener {

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    //private PlusClient mPlusClient;
    private Button mSignInButton;
    //private ConnectionResult mConnectionResult;
    private ProgressDialog mConnectionProgressDialog;
    protected TipsyApp app;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_type_signup, container, false);
        /*
        mPlusClient = new PlusClient.Builder(getActivity(), this, this)
                .setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .build();*/

        mConnectionProgressDialog = new ProgressDialog(getActivity());
        mConnectionProgressDialog.setMessage("Connexion en cours...");

        mSignInButton = (Button) view.findViewById(R.id.sign_in_button);
        mSignInButton.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*if (!mPlusClient.isConnected()) {
                    mPlusClient.connect();
                    if (mConnectionResult == null) {
                        mConnectionProgressDialog.show();
                        Log.d("TOUTAFAIT", "if connection result ");
                    } else {
                        try {
                            mConnectionResult.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLVE_ERR);
                            mConnectionProgressDialog.show();
                            Log.d("TOUTAFAIT", "resolution result ");
                        } catch (IntentSender.SendIntentException e) {
                            // Try connecting again.
                            mConnectionResult = null;
                        }
                    }
                    Log.d("TOUTAFAIT", "social connect ");
                }*/
            }
        });
        return view;
    }

    public void onStop() {
        super.onStop();
        mConnectionProgressDialog.dismiss();
        //mPlusClient.clearDefaultAccount();
        //mPlusClient.disconnect();
    }
/*
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mConnectionProgressDialog.isShowing()) {
            if (result.hasResolution()) {
                // The user clicked the sign-in button already. Start to resolve
                // connection errors. Wait until onConnected() to dismiss the
                // connection dialog.
                try {
                    result.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLVE_ERR);
                    Log.d("TOUTAFAIT", "try connection failed");
                } catch (IntentSender.SendIntentException e) {
                    mPlusClient.disconnect();
                    mPlusClient.connect();
                    Log.d("TOUTAFAIT", "catch connection failed" + e.getMessage());
                }
            }
        }
        Log.d("TOUTAFAIT", "connection failed");
        mConnectionResult = result;

    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == getActivity().RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.disconnect();
            mPlusClient.connect();
            Log.d("TOUTAFAIT", "onActivityResult");
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        final User user = new User(mPlusClient.getAccountName());
        user.fetch(new StackMobModelCallback() {
            @Override
            public void success() {
                final User user = new User(mPlusClient.getAccountName(), "social");
                user.login(new StackMobModelCallback() {
                    @Override
                    public void success() {
                        user.setSocial(true);
                        // Sauvegarde locale des identifiants pour connexion auto
                        User.rememberMe(getActivity(), user.getEmail(), "social");
                        // Redirection en fonction du type utilisateur
                        User.keepCalmAndWaitForGoingHome(getActivity(), user);
                        Log.d("TOUTAFAIT", "login social already ");
                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT", "login social " + e.getMessage());
                    }
                });

            }

            @Override
            public void failure(StackMobException e) {
                Membre membre = new Membre(
                        mPlusClient.getAccountName(),
                        "social",
                        mPlusClient.getCurrentPerson().getName().getFamilyName(),
                        mPlusClient.getCurrentPerson().getName().getGivenName()
                );
                User.rememberMe(getActivity(), mPlusClient.getAccountName(), "social");
                signUpUser(membre);
                Log.d("TOUTAFAIT", "login social membre non existant " + e.getMessage());
            }
        });
        Log.d("TOUTAFAIT", "onConnected ");
    }

    @Override
    public void onDisconnected() {
        Log.d("TOUTAFAIT", "logout ");
    }
    */
    protected void signUpUser(final TipsyUser tipsyUser) {

        /*
        tipsyUser.getUser().setSocial(true);
        // Inscription du User
        tipsyUser.getUser().save(new StackMobModelCallback() {
            //Connexion auto
            @Override
            public void success() {
                tipsyUser.getUser().login(new StackMobModelCallback() {
                    //Enregistrement du tipsyUser
                    @Override
                    public void success() {
                        tipsyUser.save(new StackMobModelCallback() {
                            // Direction page d'accueil
                            @Override
                            public void success() {
                                User.keepCalmAndWaitForGoingHome(getActivity(), tipsyUser.getUser());
                            }

                            @Override
                            public void failure(StackMobException e) {
                                Log.d("TOUTAFAIT", "save orga/membre" + e.getMessage());
                                if (!app.isOnline()){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Aucune connexion Internet !", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }

                        });
                    }

                    @Override
                    public void failure(StackMobException e) {
                        if (!app.isOnline()){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Aucune connexion Internet !", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        Log.d("TOUTAFAIT", "login" + e.getMessage());
                    }
                });
            }

            @Override
            public void failure(StackMobException e) {
                if (!app.isOnline()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Aucune connexion Internet !", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                Log.d("TOUTAFAIT", "save user" + e.getMessage());
            }

        });
        */
    }
}
