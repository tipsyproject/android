package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.*;
import com.mobsandgeeks.saripaar.annotation.*;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;
import tipsy.commun.Organisateur;

public class InscriptionOrgaActivity extends Activity implements Validator.ValidationListener {

    @Required(order = 2)
    private EditText    nom;
    @Required(order = 2)
    @Email(order = 3)
    private EditText    email;
    @Password(order = 3)
    @TextRule(order = 4, minLength = 6, message = "Entrez au moins 6 caractères.")
    private EditText    password;
    private CheckBox    afficherPassword;
    private Button      inscription;

    private Validator   validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_orga);

        validator = new Validator(this);
        validator.setValidationListener(this);

        nom                 = (EditText) findViewById(R.id.nom);
        email               = (EditText) findViewById(R.id.email);
        password            = (EditText) findViewById(R.id.password);
        afficherPassword    = (CheckBox) findViewById(R.id.afficherPassword);
        inscription         = (Button) findViewById(R.id.connect);

        // TENTATIVE D'INSCRIPTION
        inscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Validation du formulaire d'inscription
                validator.validate();
            };
        });
    }

    public void onValidationSucceeded() {
        // Création d'un nouvel organisateur
        final Organisateur orga = new Organisateur(
                email.getText().toString(),
                password.getText().toString(),
                nom.getText().toString()
        );
        // Sauvegarde dans la BDD
        orga.save(new StackMobModelCallback() {
            // Connexion automatique en cas de réussite
            @Override
            public void success() {
                orga.login(new StackMobModelCallback() {
                    @Override
                    public void success() {
                        // Tableau de bord Organisateur
                        startActivity(new Intent(InscriptionOrgaActivity.this, HomeActivity.class));
                    }
                    @Override
                    public void failure(StackMobException e) {
                        // Connexion
                        startActivity(new Intent(InscriptionOrgaActivity.this, LoginActivity.class));
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
