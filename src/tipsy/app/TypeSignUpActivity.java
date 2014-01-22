package tipsy.app;

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

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import tipsy.app.membre.SignUpMembreFragment;
import tipsy.app.orga.SignUpOrgaFragment;
import tipsy.commun.Membre;
import tipsy.commun.Organisateur;
import tipsy.commun.User;

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
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback statusCallback =
            new SessionStatusCallback();

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
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
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
        Log.d("TOUTAFAIT", "validate sign up ");
        validator = new Validator(this);
        validator.setValidationListener(this);

        validator.validate();

    }

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
    }

    public void onValidationSucceeded() {
        final User user = new User(inputEmail.getText().toString());
        Log.d("TOUTAFAIT", "validation succeeded ");
        user.fetch(new StackMobModelCallback() {
            @Override
            public void success() {
                TypeSignUpActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TOUTAFAIT", "Email déjà pris ");
                        Toast.makeText(TypeSignUpActivity.this, "Email déjà pris", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void failure(StackMobException e) {
                TypeSignUpActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                /*if (!app.isOnline()){
                            Log.d("TOUTAFAIT", "Aucune connexion Internet ");
                            Toast.makeText(TypeSignUpActivity.this, "Aucune connexion Internet !", Toast.LENGTH_LONG).show();

                }
                else*/ if (pager.getCurrentItem() == 1) {
                    final Membre membre = new Membre(
                            inputEmail.getText().toString(),
                            inputPassword.getText().toString(),
                            inputNom.getText().toString(),
                            inputPrenom.getText().toString()
                    );
                    signUpUser(membre);
                } else if (pager.getCurrentItem() == 2) {
                    final Organisateur orga = new Organisateur(
                            inputEmail.getText().toString(),
                            inputPassword.getText().toString(),
                            inputNom.getText().toString()
                    );
                    signUpUser(orga);
                }
                Log.d("TOUTAFAIT", "remember me ");
                User.rememberMe(TypeSignUpActivity.this, inputEmail.getText().toString(), inputPassword.getText().toString());
                    }
                });
            }

        });

    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        final String message = failedRule.getFailureMessage();
        Log.d("TOUTAFAIT", "validation failed ");
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            TypeSignUpActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TypeSignUpActivity.this, message, Toast.LENGTH_LONG).show();
                }
            });
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

    protected void signUpUser(final User.TipsyUser tipsyUser) {
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
                                Log.d("TOUTAFAIT", "save success ");
                                User.keepCalmAndWaitForGoingHome(TypeSignUpActivity.this, tipsyUser.getUser());
                            }

                            @Override
                            public void failure(StackMobException e) {
                                Log.d("TOUTAFAIT", "save orga/membre" + e.getMessage());
                            }

                        });
                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT", "login" + e.getMessage());
                    }
                });
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "save user" + e.getMessage());
            }

        });
    }
}
