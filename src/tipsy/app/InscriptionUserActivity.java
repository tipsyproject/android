package tipsy.app;

/**
 * Created by Guillaume on 10/12/13.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import tipsy.commun.Participant;

public class InscriptionUserActivity extends Activity implements Validator.ValidationListener {

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
    }

    public void onValidationSucceeded() {
        // Création d'un nouvel Participant
        final Participant user = new Participant(
                email.getText().toString(),
                password.getText().toString(),
                nom.getText().toString()
        );
        // Sauvegarde dans la BDD
        user.save(new StackMobModelCallback() {
            // Connexion automatique en cas de réussite
            @Override
            public void success() {
                user.login(new StackMobModelCallback() {
                    @Override
                    public void success() {
                        // Tableau de bord Organisateur
                        startActivity(new Intent(InscriptionUserActivity.this, HomeActivity.class));
                    }

                    @Override
                    public void failure(StackMobException e) {
                        // Connexion
                        startActivity(new Intent(InscriptionUserActivity.this, LoginActivity.class));
                    }
                });
            }

            // En cas d'échec
            @Override
            public void failure(StackMobException e) {
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

}
