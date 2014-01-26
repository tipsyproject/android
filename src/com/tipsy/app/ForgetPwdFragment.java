package com.tipsy.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;


/**
 * Created by Alexandre on 08/01/14.
 */
public class ForgetPwdFragment extends Fragment implements Validator.ValidationListener {

    @Required(order = 1, message = "Email manquant")
    @Email(order = 2, message = "Email Incorrect")
    protected EditText inputForget;
    protected Button reset;
    protected Validator validator_pwd;
    protected TipsyApp app;
    private ProgressDialog mConnectionProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_forget_pwd, container, false);
        validator_pwd = new Validator(this);
        validator_pwd.setValidationListener(this);
        inputForget = (EditText) view.findViewById(R.id.input_email_forget);
        reset = (Button) view.findViewById(R.id.resetpwd);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                app.hideKeyboard(getActivity());
                validator_pwd.validate();
            }
        });
        mConnectionProgressDialog = new ProgressDialog(getActivity());
        mConnectionProgressDialog.setMessage("Chargement en cours...");
        return view;
    }

    public void onValidationSucceeded() {
        mConnectionProgressDialog.show();
        ParseUser.requestPasswordResetInBackground(inputForget.getText().toString(),
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            LoginActivity.getPager().setCurrentItem(0, true);
                            Toast.makeText(getActivity(), "Email envoyé", Toast.LENGTH_LONG).show();
                        } else if (e.getCode() == e.EMAIL_NOT_FOUND || e.getCode() == e.OBJECT_NOT_FOUND){
                            Toast.makeText(getActivity(), "Email non existant", Toast.LENGTH_LONG).show();
                        }
                        else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            Toast.makeText(getActivity(), "Réinitialisation échouée", Toast.LENGTH_LONG).show();
                        }
                        mConnectionProgressDialog.dismiss();
                    }
                });
    }

    public void onValidationFailed(View failedView, final Rule<?> failedRule) {
        final String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
