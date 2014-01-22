package tipsy.app;

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

    /*public void onClickFb(View view){
        // set permission list, Don't forget to add email
        view.setReadPermissions(Arrays.asList("basic_info", "email"));
        // session state call back event
        view.setSessionStatusCallback(new Session.StatusCallback() {

            @Override
            public void call(final Session session, SessionState state, Exception exception) {

                if (session.isOpened()) {
                    Log.i(TAG, "Access Token" + session.getAccessToken());
                    Request.executeMeRequestAsync(session,
                            new Request.GraphUserCallback() {
                                @Override
                                public void onCompleted(GraphUser user, Response response) {
                                    if (user != null) {
                                        final User userfb = new User(String.valueOf(user.asMap().get("email")));
                                        userfb.loginWithFacebook(session.getAccessToken(), true, String.valueOf(user.asMap().get("email")), new StackMobOptions(), new StackMobModelCallback() {
                                            @Override
                                            public void success() {
                                                User.keepCalmAndWaitForGoingHome(TypeSignUpActivity.this, userfb);

                                            }

                                            @Override
                                            public void failure(StackMobException e) {
                                            }
                                        });
                                    }
                                }
                            });
                }

            }
        });
    }*/

    public void onValidationSucceeded() {
        final User user = new User(inputEmail.getText().toString());
        user.fetch(new StackMobModelCallback() {
            @Override
            public void success() {
                TypeSignUpActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TypeSignUpActivity.this, "Email déjà pris", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void failure(StackMobException e) {
                if (!app.isOnline()){
                    TypeSignUpActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TypeSignUpActivity.this, "Aucune connexion Internet !", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if (pager.getCurrentItem() == 1) {
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
                User.rememberMe(TypeSignUpActivity.this, inputEmail.getText().toString(), inputPassword.getText().toString());
            }
        });

    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        final String message = failedRule.getFailureMessage();
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
