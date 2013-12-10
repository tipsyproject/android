package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.Date;

import tipsy.commun.Organisateur;

public class LoginActivity extends Activity implements Validator.ValidationListener {

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

        // TENTATIVE DE CONNEXION
        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validator.validate();
            }
        });

        // Redirection inscription
        inscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, InscriptionOrgaActivity.class));
            }
        });
    }


    public void onValidationSucceeded() {
        // D'ABORD TENTATIVE DE CONNEXION EN TANT QU'ORGANISATEUR
        final Organisateur orga = new Organisateur(email.getText().toString(), password.getText().toString());
        orga.login(new StackMobModelCallback() {
            @Override
            public void success() {
                orga.creerEvent("Mon event",new Date());
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }

            // SINON TENTATIVE DE CONNEXION EN TANT QUE MEMBRE
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
