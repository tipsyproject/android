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

    public void onDestroy(){
        super.onDestroy();
        mConnectionProgressDialog.dismiss();
    }

    public void onValidationSucceeded() {
        mConnectionProgressDialog.show();
        /*
        final User user = new User(inputForget.getText().toString());
        user.fetch(new StackMobModelCallback() {
            @Override
            public void success() {
                user.sentForgotPasswordEmail(inputForget.getText().toString(), new StackMobModelCallback() {
                    @Override
                    public void success() {
                        user.setTemp_pwd(true);
                        user.save(new StackMobModelCallback() {
                            @Override
                            public void success() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LoginActivity.getPager().setCurrentItem(0, true);
                                        Toast.makeText(getActivity(), "Mot de passe envoyé", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void failure(StackMobException e) {
                                Log.d("TOUTAFAIT", "forget " + e.getMessage());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Réinitialisation échouée", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });

                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT", "forget2" + e.getMessage());
                    }
                });
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "forget1" + e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Email non existant", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });*/
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
