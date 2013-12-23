package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.membre.SignUpMembreActivity;
import tipsy.commun.User;

public class LoginActivity extends Activity implements Validator.ValidationListener {

    private SharedPreferences prefs;

    @Required(order = 1)
    @Email(order = 2)
    private EditText inputEmail;
    @Required(order = 3)
    private EditText inputPassword;
    private Button buttonSignin;
    private Button buttonSignup;
    private Validator validator;
    protected Button buttonHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.right_to_left, R.animator.activity_close_scale);
        setContentView(R.layout.act_login);
        buttonSignin = (Button) findViewById(R.id.button_signin);
        buttonSignup = (Button) findViewById(R.id.button_signup);
        buttonHelp = (Button) findViewById(R.id.button_help);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);

        validator = new Validator(this);
        validator.setValidationListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        // TENTATIVE DE CONNEXION
        buttonSignin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard(LoginActivity.this);
                validator.validate();
            }
        });

        // Redirection inscription
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, TypeSignUpActivity.class));
            }
        });

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(LoginActivity.this);
                return false;
            }
        });
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HelpActivity.class));
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onValidationSucceeded() {
        final User user = new User(inputEmail.getText().toString(), inputPassword.getText().toString());
        user.login(new StackMobModelCallback() {
            @Override
            public void success() {
                // Sauvegarde locale des identifiants pour connexion auto
                User.rememberMe(LoginActivity.this, inputEmail.getText().toString(), inputPassword.getText().toString());
                // Redirection en fonction du type utilisateur
                User.keepCalmAndWaitForGoingHome(LoginActivity.this, user);
            }

            @Override
            public void failure(StackMobException e) {
                Toast.makeText(LoginActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
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
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
