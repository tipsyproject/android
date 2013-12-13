package tipsy.app.orga;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.annotation.Required;

import tipsy.app.R;
import tipsy.app.SignUpActivity;
import tipsy.commun.Organisateur;

/**
 * Created by valoo on 13/12/13.
 */
public class SignUpOrgaActivity extends SignUpActivity {

    @Required(order = 4)
    protected EditText inputNom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_signup_orga);
        inputNom = (EditText) findViewById(R.id.input_nom);
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
