package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;

import tipsy.app.membre.HomeMembreActivity;
import tipsy.app.orga.InscriptionActivity;
import tipsy.commun.TypeUser;
import tipsy.commun.User;
import tipsy.commun.Prefs;

public class LoginActivity extends Activity implements Validator.ValidationListener {

    private SharedPreferences prefs;

    @Required(order = 1)
    @Email(order = 2)
    private EditText email;
    @Required(order = 3)
    private EditText password;
    private Button connect;
    private TextView inscription;
    private Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connect = (Button) findViewById(R.id.connect);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        inscription = (TextView) findViewById(R.id.inscription);

        validator = new Validator(this);
        validator.setValidationListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);



        // TENTATIVE DE CONNEXION
        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validator.validate();
            }
        });

        // Redirection inscription
        inscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, InscriptionActivity.class));
            }
        });
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(LoginActivity.this);
                return false;
            }
        });

    }

    public void onValidationSucceeded() {
        final User user = new User(email.getText().toString(), password.getText().toString());
        user.login(new StackMobModelCallback() {
            @Override
            public void success() {
                // Sauvegarde locale des identifiants pour connexion auto
                prefs.edit()
                    .putString(Prefs.USERNAME, email.getText().toString())
                    .putString(Prefs.PASSWORD, password.getText().toString())
                    .commit();
                // Redirection en fonction du type utilisateur
                if (user.getType() == TypeUser.ORGANISATEUR)
                    startActivity(new Intent(LoginActivity.this,tipsy.app.orga.HomeOrgaActivity.class));
                else if (user.getType() == TypeUser.MEMBRE)
                    startActivity(new Intent(LoginActivity.this,HomeMembreActivity.class));
                else Toast.makeText(LoginActivity.this,"Connexion impossible", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void failure(StackMobException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    /*

    On renvoie vers la page d'accueil correspondante si l'utilsiateur est encore connecté

    Sinon si aucun couple username/password n'a été mémorisé,
    l'utilisateur est redirigé vers la page de login.

    Sinon il est connecté automatiquement.
    Et en fonction du type d'utilisateur,
    celui-ci est redirigé vers l'activité correspondante.
    (en cas d'echec lors de la connexion auto, l'utilisateur est redirigé vers LoginActivity)
    */
    public static void rememberMe(Activity act){
        final Activity a = act;
        // Si utilisateur encore connecté
        User.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
            @Override
            public void success(List<User> list) {
                Log.d("REMEMBER", "Deja connecte");
                User user = list.get(0);
                if (user.getType() == TypeUser.ORGANISATEUR)
                    a.startActivity(new Intent(a, tipsy.app.orga.HomeOrgaActivity.class));
                else if (user.getType() == TypeUser.MEMBRE)
                    a.startActivity(new Intent(a,HomeMembreActivity.class));
            }

            @Override
            public void failure(StackMobException e) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(a);
                final String username = prefs.getString(Prefs.USERNAME, null);
                final String password = prefs.getString(Prefs.PASSWORD, null);
                if (username == null || password == null) {
                    a.startActivity(new Intent(a, LoginActivity.class));
                }else{
                    final User user = new User(username,password);
                    user.login(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            Log.d("REMEMBER", "Souvenir user");
                            if (user.getType() == TypeUser.ORGANISATEUR)
                                a.startActivity(new Intent(a, tipsy.app.orga.HomeOrgaActivity.class));
                            else if (user.getType() == TypeUser.MEMBRE)
                                a.startActivity(new Intent(a,HomeMembreActivity.class));
                            else  a.startActivity(new Intent(a, LoginActivity.class));
                        }
                        /* En cas d'echec, redirection vers LoginActivity */
                        @Override
                        public void failure(StackMobException e) {
                            a.startActivity(new Intent(a, LoginActivity.class));
                        }
                    });
                }
            }

        });
    }

}
