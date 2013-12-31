package tipsy.app.membre;

/**
 * Created by Guillaume on 10/12/13.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;

import tipsy.app.R;
import tipsy.app.SignUpUser;
import tipsy.commun.Membre;
import tipsy.commun.User;

public class SignUpMembreActivity extends SignUpUser {

    @Required(order = 1)
    protected EditText inputNom;
    @Required(order = 2)
    protected EditText inputPrenom;
    protected Button buttonSignupOrga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_signup_membre);
        inputNom = (EditText) findViewById(R.id.input_nom);
        inputPrenom = (EditText) findViewById(R.id.input_prenom);

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(SignUpMembreActivity.this);
                return false;
            }
        });

        super.onCreate(savedInstanceState);
    }


    public void validateSignUp(View view) {
        validator = new Validator(this);
        validator.setValidationListener(this);

        validator.validate();

    }

    public void onValidationSucceeded() {
        final Membre membre = new Membre(
                inputEmail.getText().toString(),
                inputPassword.getText().toString(),
                inputNom.getText().toString(),
                inputPrenom.getText().toString()
        );
        User.rememberMe(this, inputEmail.getText().toString(), inputPassword.getText().toString());
        signUpUser(membre);
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
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}

