package tipsy.app.orga;

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
import tipsy.commun.Organisateur;
import tipsy.commun.User;

/**
 * Created by valoo on 13/12/13.
 */
public class SignUpOrgaActivity extends SignUpUser implements Validator.ValidationListener {

    @Required(order = 1)
    protected EditText inputNom;
    protected Button buttonSignupMembre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_signup_orga);
        inputNom = (EditText) findViewById(R.id.input_nom);
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(SignUpOrgaActivity.this);
                return false;
            }
        });

        super.onCreate(savedInstanceState);
    }

    public void onValidationSucceeded() {
        final Organisateur orga = new Organisateur(
                inputEmail.getText().toString(),
                inputPassword.getText().toString(),
                inputNom.getText().toString()
        );
        User.rememberMe(this, inputEmail.getText().toString(), inputPassword.getText().toString());
        signUpUser(orga);
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

    public void validateSignUp(View view) {
        validator = new Validator(this);
        validator.setValidationListener(this);

        validator.validate();

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
