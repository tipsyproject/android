package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.membre.HomeMembreActivity;
import tipsy.app.orga.HomeOrgaActivity;
import tipsy.commun.TypeUser;
import tipsy.commun.User;

/**
 * Created by valoo on 13/12/13.
 */
public abstract class SignUpActivity extends Activity implements Validator.ValidationListener {

    @Required(order = 1)
    @Email(order = 2)
    protected EditText inputEmail;
    @Required(order = 3)
    protected EditText inputPassword;
    protected Button buttonSignup;

    protected Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        buttonSignup = (Button) findViewById(R.id.button_signup);
        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    protected void signUp(final User.TipsyUser tipsyUser) {
        // Inscription du User
        tipsyUser.getUser().save(new StackMobModelCallback() {
            //Connexion auto
            @Override
            public void success() {
                tipsyUser.getUser().login(new StackMobModelCallback() {
                    //Enregistrement du tipsyUser
                    @Override
                    public void success() {
                        tipsyUser.save(new StackMobModelCallback() {
                            // Direction page d'accueil
                            @Override
                            public void success() {
                                Class dest;
                                if (tipsyUser.getType() == TypeUser.MEMBRE)
                                    dest = HomeMembreActivity.class;
                                else dest = HomeOrgaActivity.class;
                                startActivity(new Intent(SignUpActivity.this, dest));
                            }

                            @Override
                            public void failure(StackMobException e) {
                                Log.d("TOUTAFAIT", "save orga/membre" + e.getMessage());
                            }

                        });
                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT", "login" + e.getMessage());
                    }
                });
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "save user" + e.getMessage());
            }

        });
    }
}
