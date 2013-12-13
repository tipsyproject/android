package tipsy.app.membre;

/**
 * Created by Guillaume on 10/12/13.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.LoginActivity;
import tipsy.app.R;
import tipsy.app.orga.HomeOrgaActivity;
import tipsy.commun.Membre;
import tipsy.commun.Organisateur;
import tipsy.commun.Participant;
import tipsy.commun.TypeUser;
import tipsy.commun.User;

public class InscriptionMembreActivity extends Activity implements Validator.ValidationListener {

    @Required(order = 2)
    private EditText prenom;
    @Required(order = 2)
    private EditText nom;
    @Required(order = 2)
    @Email(order = 3)
    private EditText email;
    @Password(order = 3)
    @TextRule(order = 4, minLength = 6, message = "Entrez au moins 6 caractères.")
    private EditText password;
    private CheckBox afficherPassword;
    private Button inscription;

    private Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        validator = new Validator(this);
        validator.setValidationListener(this);

        prenom = (EditText) findViewById(R.id.prenom);
        nom = (EditText) findViewById(R.id.nom);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        afficherPassword = (CheckBox) findViewById(R.id.afficherPassword);
        inscription = (Button) findViewById(R.id.connect);

        // TENTATIVE D'INSCRIPTION
        inscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Validation du formulaire d'inscription
                validator.validate();
            }

            ;
        });

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(InscriptionMembreActivity.this);
                return false;
            }
        });
    }

    public void onValidationSucceeded() {
        // Création d'un nouveau user
        final User user = new User(
                email.getText().toString(),
                password.getText().toString(),
                TypeUser.MEMBRE
        );

        // Sauvegarde dans la BDD du user
        user.save(new StackMobModelCallback() {

            // Connexion automatique en cas de réussite
            @Override
            public void success() {

                user.login(new StackMobModelCallback() {
                    // Enregistrement des infos orga
                    @Override
                    public void success() {
                        Membre membre = new Membre(
                            email.getText().toString(),
                            password.getText().toString(),
                            nom.getText().toString(),
                            prenom.getText().toString()
                        );
                        membre.save(new StackMobModelCallback() {
                            // Direction accueil Organisateur
                            @Override
                            public void success() {
                                startActivity(new Intent(InscriptionMembreActivity.this, HomeMembreActivity.class));
                            }
                            @Override
                            public void failure(StackMobException e) {
                                Log.d("TOUTAFAIT", "save membre:" + e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT", "login:" + e.getMessage());
                    }
                });
            }

            // En cas d'échec
            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "signin User:" + e.getMessage());
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
}
