package com.tipsy.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.tipsy.app.membre.MembreActivity;
import com.tipsy.app.orga.OrgaActivity;
import com.tipsy.lib.TipsyUser;

import java.util.List;
import java.util.Vector;


/**
 * Created by fulgor on 23/12/13.
 */
public class TypeSignUpActivity extends FragmentActivity implements Validator.ValidationListener {

    protected PagerAdapter mPagerAdapter;
    @Required(order = 1, message = "Champ requis")
    private EditText inputNom;
    @Required(order = 2, message = "Champ requis")
    private EditText inputPrenom;
    @Required(order = 2, message = "Champ requis")
    @Email(order = 3, message = "Email Incorrect")
    protected EditText inputEmail;
    @Required(order = 4, message = "Champ requis")
    protected EditText inputPassword;
    protected CheckBox checkbox;
    protected Validator validator;
    protected boolean signup_membre = false;
    protected TipsyApp app;

    protected ViewPager pager;
    private String TAG = "TypeSignUpActivity";
    private boolean isResumed = false;
    private ProgressDialog mConnectionProgressDialog;
    //private UiLifecycleHelper uiHelper;
    //private Session.StatusCallback statusCallback = new SessionStatusCallback();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.right_to_left, R.animator.activity_close_scale);

        setContentView(R.layout.act_typesignup);

        // Création de la liste de Fragments que fera défiler le PagerAdapter
        List fragments = new Vector();
        // Ajout des Fragments dans la liste
        fragments.add(Fragment.instantiate(this, TypeSignUpFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, SignUpMembreFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, SignUpOrgaFragment.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

        pager = (ViewPager) super.findViewById(R.id.pager_signup);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //uiHelper = new UiLifecycleHelper(this, statusCallback);
        //uiHelper.onCreate(savedInstanceState);
        mConnectionProgressDialog = new ProgressDialog(TypeSignUpActivity.this);
        mConnectionProgressDialog.setMessage("Connexion en cours...");
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() != 0)
            pager.setCurrentItem(0, true);
        else
            super.onBackPressed(); // This will pop the Activity from the stack.
    }

    public void validateSignUp(View view) {
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.validate();
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        final Session session_connected = Session.getActiveSession();
        Log.d("TOUTAFAIT", "onSessionState ");
        if (isResumed) {
            if (state.isOpened()) {
                // If the session state is open:
                // Show the authenticated fragment
                Log.d("TOUTAFAIT", "state.isOpened ");
                Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(final GraphUser user, Response response) {
                        Log.d("TOUTAFAIT", "onCompleted ");
                        // If the response is successful
                        if (session_connected == Session.getActiveSession()) {
                            if (user != null) {
                                Log.d("TOUTAFAIT", "user null ");
                                final Membre fbuser = new Membre(
                                        (String) user.getProperty("email"),
                                        session_connected.getAccessToken(),
                                        user.getFirstName(),
                                        user.getLastName()
                                );
                                fbuser.getUser().loginWithFacebook(session_connected.getAccessToken(), new StackMobModelCallback() {
                                    @Override
                                    public void success() {
                                        Log.d("TOUTAFAIT", "fb ok ");
                                        TypeSignUpActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                User.rememberMe(TypeSignUpActivity.this, (String) user.getProperty("email"), session_connected.getAccessToken());
                                                User.keepCalmAndWaitForGoingHome(TypeSignUpActivity.this, fbuser.getUser());
                                                mConnectionProgressDialog.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void failure(StackMobException e) {
                                        Log.d("TOUTAFAIT", "fb " + e.getMessage());
                                        TypeSignUpActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mConnectionProgressDialog.dismiss();
                                                session_connected.closeAndClearTokenInformation();
                                                Toast.makeText(TypeSignUpActivity.this, "Erreur.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                            TypeSignUpActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TypeSignUpActivity.this, "Erreur Facebook !", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                Request.executeBatchAsync(request);
            }else if (state.isClosed()) {
                // If the session state is closed:
                // Show the login fragment
                Log.d("TOUTAFAIT", "state.isClosed ");
                mConnectionProgressDialog.dismiss();
            }
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            // Respond to session state changes, ex: upiew
            session.closeAndClearTokenInformation();
            onSessionStateChange(session, state, exception);
        }
    }

    public void onClickFb(View view){
        //Toast.makeText(TypeSignUpActivity.this, "Fonctionnalité à venir.", Toast.LENGTH_LONG).show();
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setPermissions(Arrays.asList("basic_info","email"))
                    .setCallback(statusCallback));
        } else {
            Session.openActiveSession(TypeSignUpActivity.this, true, statusCallback);
        }
    }*/

    /* INSCRIPTION */
    public void onValidationSucceeded() {
        TipsyUser user = new TipsyUser();
        user.setUsername(inputEmail.getText().toString());
        user.setPassword(inputPassword.getText().toString());
        user.setEmail(inputEmail.getText().toString());
        user.setNom(inputNom.getText().toString());
        int type;
        if (pager.getCurrentItem() == 1){
            user.setType(TipsyUser.MEMBRE);
            user.setPrenom(inputPrenom.getText().toString());
        }
        else user.setType(TipsyUser.ORGA);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Inscription réussi = connexion auto
                    ParseUser.logInInBackground(inputEmail.getText().toString(), inputPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                TipsyUser u = (TipsyUser) user;
                                if(u.getType() == TipsyUser.MEMBRE)
                                    startActivity(new Intent(TypeSignUpActivity.this, MembreActivity.class));
                                else
                                    startActivity(new Intent(TypeSignUpActivity.this, OrgaActivity.class));
                            } else {
                                Toast.makeText(TypeSignUpActivity.this,
                                        getResources().getString(R.string.erreur_connexion),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    String message;
                    switch(e.getCode()){
                        case 202:// Email déjà pris
                            message = getResources().getString(R.string.email_arleady_taken);
                            break;
                        default:
                            Log.d("TOUTAFAIT","signup error: "+e.getMessage());
                            Log.d("TOUTAFAIT", "signup error code: " + e.getCode());
                            message = getResources().getString(R.string.erreur_interne);
                    }
                    Toast.makeText(TypeSignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        final String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(TypeSignUpActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }


    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        //On fournit à l'adapter la liste des fragments à afficher
        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    public void onClickInscription(View view) {
        signup_membre = true;
        pager.setCurrentItem(1, true);
        inputNom = (EditText) findViewById(R.id.input_nom);
        inputPrenom = (EditText) findViewById(R.id.input_prenom);
        inputPrenom.setText(null);
        inputEmail = (EditText) findViewById(R.id.input_email_signup);
        inputPassword = (EditText) findViewById(R.id.input_password_signup);
        checkbox = (CheckBox) findViewById(R.id.afficher_mdp);
    }

    public void onClickOrga(View view) {

        pager.setCurrentItem(2, true);
        inputNom = (EditText) findViewById(R.id.input_orga);
        if (signup_membre)
            inputPrenom.setText("Easy");
        inputEmail = (EditText) findViewById(R.id.input_email_orga);
        inputPassword = (EditText) findViewById(R.id.input_password_orga);
        checkbox = (CheckBox) findViewById(R.id.afficher_mdp_orga);
    }

    public void checkboxClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox) v;
        if (checkBox.isChecked())
            inputPassword.setTransformationMethod(null);
        else
            inputPassword.setTransformationMethod(new PasswordTransformationMethod());
        inputPassword.setSelection(inputPassword.getText().length());
    }
}
