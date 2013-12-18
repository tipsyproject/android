package tipsy.app.orga;

import android.app.Activity;
import android.content.Intent;
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
import tipsy.app.SignUp;
import tipsy.app.membre.SignUpMembreActivity;
import tipsy.commun.Organisateur;

/**
 * Created by valoo on 13/12/13.
 */
public class SignUpOrgaActivity extends SignUp implements Validator.ValidationListener {

    @Required(order = 1)
    protected EditText inputNom;
    protected Button buttonSignupMembre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_signup_orga);
        inputNom = (EditText) findViewById(R.id.input_nom);
        buttonSignupMembre = (Button) findViewById(R.id.buttonsignupmembre);
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(SignUpOrgaActivity.this);
                return false;
            }
        });
        buttonSignupMembre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SignUpOrgaActivity.this, SignUpMembreActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
        signUp(orga);
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
