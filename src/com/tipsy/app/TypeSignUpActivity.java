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

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.tipsy.app.membre.MembreActivity;
import com.tipsy.app.orga.OrgaActivity;
import com.tipsy.lib.TipsyUser;

import java.util.Arrays;
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
    private ProgressDialog mConnectionProgressDialog;
    private ProgressDialog mConnectionProgressDialog_signup;
    protected boolean fb = false;

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
        mConnectionProgressDialog = new ProgressDialog(TypeSignUpActivity.this);
        mConnectionProgressDialog.setMessage("Connexion en cours...");
        mConnectionProgressDialog_signup = new ProgressDialog(TypeSignUpActivity.this);
        mConnectionProgressDialog_signup.setMessage("Inscription en cours...");

        // Check if there is a currently logged in user
        // and they are linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Fetch Facebook user info if the session is active
            Session session = ParseFacebookUtils.getSession();
            if (session != null && session.isOpened()) {
                request();
            }
        }
    }

    public void onDestroy(){
        super.onDestroy();
        mConnectionProgressDialog.dismiss();
        mConnectionProgressDialog_signup.dismiss();
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

@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fb)
            ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    public void onClickFb(View view){
        fb = true;
        mConnectionProgressDialog.show();
        List<String> permissions = Arrays.asList("basic_info", "email", "user_about_me",
                "user_relationships", "user_birthday", "user_location");
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d(app.TAG,
                            "Uh oh. The user cancelled the Facebook login.");
                    mConnectionProgressDialog.dismiss();
                    Toast.makeText(TypeSignUpActivity.this, "Erreur.", Toast.LENGTH_LONG).show();
                } else if (user.isNew()) {
                    Log.d(app.TAG,
                            "User signed up and logged in through Facebook!");
                    request();
                } else {
                    Log.d(app.TAG,
                            "User logged in through Facebook!");
                    request();
                }
            }
        });
    }

    private void request() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                                // Save the user
                                TipsyUser currentUser = TipsyUser
                                        .getCurrentUser();
                                currentUser.setPrenom(user.getFirstName());
                                currentUser.setNom(user.getLastName());
                                currentUser.setUsername(user.asMap().get("email").toString());
                                currentUser.setType(TipsyUser.MEMBRE);
                                currentUser.saveInBackground();
                                startActivity(new Intent(TypeSignUpActivity.this, MembreActivity.class));

                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
                                    || (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                                Log.d(app.TAG,
                                        "The facebook session was invalidated.");
                                ParseUser.logOut();
                            } else {
                                Log.d(app.TAG,
                                        "Some other error: "
                                                + response.getError()
                                                .getErrorMessage());
                            }
                            mConnectionProgressDialog.dismiss();
                        }
                    }
                });
        request.executeAsync();

    }

    public void onClickTwt(View view){
        mConnectionProgressDialog.show();
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d(app.TAG, "Uh oh. The user cancelled the Twitter login.");
                    mConnectionProgressDialog.dismiss();
                } else if (user.isNew()) {
                    Log.d(app.TAG, "User signed up and logged in through Twitter!");
                    // Save the user
                    TipsyUser currentUser = TipsyUser
                            .getCurrentUser();
                    currentUser.setPrenom(ParseTwitterUtils.getTwitter().getScreenName());
                    currentUser.setNom("");
                    currentUser.setUsername(ParseTwitterUtils.getTwitter().getUserId());
                    currentUser.setType(TipsyUser.MEMBRE);
                    currentUser.saveInBackground();
                    startActivity(new Intent(TypeSignUpActivity.this, MembreActivity.class));
                } else {
                    Log.d(app.TAG, "User logged in through Twitter!");
                }
            }
        });
    }

    /* INSCRIPTION */
    public void onValidationSucceeded() {
        mConnectionProgressDialog_signup.show();
        TipsyUser user = new TipsyUser();
        user.setUsername(inputEmail.getText().toString());
        user.setPassword(inputPassword.getText().toString());
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
                                mConnectionProgressDialog_signup.dismiss();
                                Toast.makeText(TypeSignUpActivity.this,
                                        getResources().getString(R.string.erreur_connexion),
                                        Toast.LENGTH_LONG).show();
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
                    mConnectionProgressDialog_signup.dismiss();
                    Toast.makeText(TypeSignUpActivity.this, message, Toast.LENGTH_LONG).show();
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
