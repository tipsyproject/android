package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.commun.TypeUser;
import tipsy.commun.User;

/**
 * Created by valoo on 13/12/13.
 */
public abstract class SignUpActivity extends Activity implements Validator.ValidationListener {

    @Required(order = 10)
    @Email(order = 11)
    protected EditText inputEmail;
    @Required(order = 12)
    protected EditText inputPassword;
    protected ImageButton buttonSignup;
    protected Button buttonSignin;
    protected Button buttonLater;

    protected Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.right_to_left, R.animator.activity_close_scale);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        buttonSignup = (ImageButton) findViewById(R.id.button_signup);
        buttonSignin = (Button) findViewById(R.id.button_signin);
        buttonLater = (Button) findViewById(R.id.button_later);
        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validator.validate();
            }
        });

        buttonSignin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        buttonLater.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, HomeAnonymousActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }

    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.animator.activity_open_close, R.animator.left_to_right);
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
                                if (tipsyUser.getType() == TypeUser.MEMBRE)
                                    LoginActivity.type = 1;
                                else LoginActivity.type = 2;
                                startActivity(new Intent(SignUpActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
