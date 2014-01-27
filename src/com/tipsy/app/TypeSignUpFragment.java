package com.tipsy.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.tipsy.app.membre.MembreActivity;
import com.tipsy.lib.TipsyUser;

import java.io.IOException;


/**
 * Created by Alexandre on 04/01/14.
 */
public class TypeSignUpFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener {

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    public static int REQUEST_CODE_TOKEN_AUTH = 9001;
    private PlusClient mPlusClient;
    private Button mSignInButton;
    private ConnectionResult mConnectionResult;
    private ProgressDialog mConnectionProgressDialog;
    protected TipsyApp app;
    private String token;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_type_signup, container, false);

        mPlusClient = new PlusClient.Builder(getActivity(), this, this)
                .setActions("http://schemas.google.com/AddActivity")
                .setScopes(Scopes.PLUS_LOGIN + Scopes.PLUS_PROFILE)
                .build();

        mConnectionProgressDialog = new ProgressDialog(getActivity());
        mConnectionProgressDialog.setMessage("Connexion en cours...");

        mSignInButton = (Button) view.findViewById(R.id.sign_in_button);
        mSignInButton.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mPlusClient.isConnected()) {
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
                }
            }
        });
        return view;
    }

    public void onStop() {
        super.onStop();
        mConnectionProgressDialog.dismiss();
        //mPlusClient.clearDefaultAccount();
        mPlusClient.disconnect();
    }


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
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == getActivity().RESULT_OK || requestCode == REQUEST_CODE_TOKEN_AUTH && responseCode == getActivity().RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.disconnect();
            mPlusClient.connect();
            Log.d("TOUTAFAIT", "onActivityResult");
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                token = null;

                try {
                    token = GoogleAuthUtil.getToken(
                            getActivity(),
                            mPlusClient.getAccountName(),
                            "oauth2:" + Scopes.PLUS_LOGIN + " " + Scopes.PLUS_PROFILE + " https://www.googleapis.com/auth/userinfo.email");
                } catch (IOException transientEx) {
                    // Network or server error, try later
                    Log.e(app.TAG, transientEx.toString());
                    mConnectionProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Erreur de connexion.", Toast.LENGTH_LONG).show();
                } catch (UserRecoverableAuthException e) {
                    // Recover (with e.getIntent())
                    Intent recover = e.getIntent();
                    startActivityForResult(recover, REQUEST_CODE_TOKEN_AUTH);
                } catch (GoogleAuthException authEx) {
                    // The call is not ever expected to succeed
                    // assuming you have already verified that
                    // Google Play services is installed.
                }

                return token;
            }

            @Override
            protected void onPostExecute(final String token) {
                Log.i(app.TAG, "Access token retrieved:" + token);
                if (token != null) {
                    final TipsyUser newUser = new TipsyUser();

                    /*newUser.becomeInBackground(token, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                // The current user is now set to user.
                                newUser.setPrenom(mPlusClient.getCurrentPerson().getName().getGivenName());
                                newUser.setNom(mPlusClient.getCurrentPerson().getName().getFamilyName());
                                newUser.setUsername(mPlusClient.getAccountName());
                                newUser.setType(TipsyUser.MEMBRE);
                                newUser.saveInBackground();
                                startActivity(new Intent(getActivity(), MembreActivity.class));
                                getActivity().finish();
                                LoginActivity.fa.finish();
                            } else {
                                // The token could not be validated.
                                Log.d(app.TAG, e.getMessage());
                                mConnectionProgressDialog.dismiss();
                                Toast.makeText(getActivity(), "Erreur de permission.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });*/
                    Log.d(app.TAG, mPlusClient.getCurrentPerson().getName().getGivenName());
                    newUser.setPrenom(mPlusClient.getCurrentPerson().getName().getGivenName());
                    newUser.setNom(mPlusClient.getCurrentPerson().getName().getFamilyName());
                    newUser.setUsername(mPlusClient.getAccountName());
                    newUser.setType(TipsyUser.MEMBRE);
                    newUser.setPassword(token);
                    newUser.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Inscription réussi = connexion auto
                                ParseUser.logInInBackground(mPlusClient.getAccountName(), token, new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        if (user != null) {
                                            startActivity(new Intent(getActivity(), MembreActivity.class));
                                            getActivity().finish();
                                            LoginActivity.fa.finish();
                                        } else {
                                            mConnectionProgressDialog.dismiss();
                                            Toast.makeText(getActivity(),
                                                    getResources().getString(R.string.erreur_connexion),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                String message;
                                switch (e.getCode()) {
                                    case 202:// Email déjà pris
                                        message = getResources().getString(R.string.email_arleady_taken);
                                        break;
                                    default:
                                        Log.d("TOUTAFAIT", "signup error: " + e.getMessage());
                                        Log.d("TOUTAFAIT", "signup error code: " + e.getCode());
                                        message = getResources().getString(R.string.erreur_interne);
                                }
                                mConnectionProgressDialog.dismiss();
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

        };
        task.execute();
    }

    @Override
    public void onDisconnected() {
        Log.d("TOUTAFAIT", "logout ");
    }
}

