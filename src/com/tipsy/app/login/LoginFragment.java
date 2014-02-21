package com.tipsy.app.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.app.help.HelpActivity;
import com.tipsy.app.membre.MembreActivity;
import com.tipsy.app.orga.OrgaActivity;
import com.tipsy.app.signup.TypeSignUpActivity;
import com.tipsy.lib.TipsyUser;

/**
 * Created by Alexandre on 08/01/14.
 */
public class LoginFragment extends Fragment implements Validator.ValidationListener {

    @Required(order = 1, message = "Email manquant")
    @Email(order = 2, message = "Email Incorrect")
    protected EditText inputEmail;
    @Required(order = 3, message = "Mot de passe manquant")
    protected EditText inputPassword;
    protected Validator validator;
    protected Button signin;
    protected Button signup;
    protected Button help;
    protected Button forgetpwd;
    protected static String username;
    protected static String pwd;
    protected TipsyApp app;
    private ProgressDialog mConnectionProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login, container, false);
        validator = new Validator(this);
        validator.setValidationListener(this);
        inputEmail = (EditText) view.findViewById(R.id.input_email);
        inputPassword = (EditText) view.findViewById(R.id.input_password);
        signin = (Button) view.findViewById(R.id.button_signin);
        signup = (Button) view.findViewById(R.id.button_signup);
        help = (Button) view.findViewById(R.id.button_help);
        forgetpwd = (Button) view.findViewById(R.id.forgotpass);
        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                app.hideKeyboard(getActivity());
                validator.validate();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                app.hideKeyboard(getActivity());
                startActivity(new Intent(getActivity(), TypeSignUpActivity.class));
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("Connected", true);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        forgetpwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                app.hideKeyboard(getActivity());
                LoginActivity.getPager().setCurrentItem(1, true);
            }
        });
        mConnectionProgressDialog = new ProgressDialog(getActivity());
        mConnectionProgressDialog.setMessage("Connexion en cours...");
        return view;
    }

    public void onDestroy() {
        super.onDestroy();
        mConnectionProgressDialog.dismiss();
    }

    public void onValidationSucceeded() {
        mConnectionProgressDialog.show();
        ParseUser.logInInBackground(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    TipsyUser u = (TipsyUser) user;
                    if (u.getType() == TipsyUser.MEMBRE)
                        startActivity(new Intent(getActivity(), MembreActivity.class));
                    else
                        startActivity(new Intent(getActivity(), OrgaActivity.class));
                    getActivity().finish();
                } else {
                    String message;
                    switch (e.getCode()) {
                        case 101:
                            message = getResources().getString(R.string.failed_connexion);
                            break;
                        default:
                            message = getResources().getString(R.string.erreur_connexion);
                    }
                    mConnectionProgressDialog.dismiss();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        final String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
