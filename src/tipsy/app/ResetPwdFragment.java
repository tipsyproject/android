package tipsy.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.commun.User;

/**
 * Created by Alexandre on 20/01/14.
 */
public class ResetPwdFragment extends Fragment implements Validator.ValidationListener {

    protected Button ok;
    protected CheckBox checkbox;
    @Required(order = 1, message = "Mot de passe manquant")
    protected EditText newPassword;
    protected Validator validator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_reset_pwd, container, false);
        validator = new Validator(this);
        validator.setValidationListener(this);
        newPassword = (EditText) view.findViewById(R.id.new_pwd);
        ok = (Button) view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard(getActivity());
                validator.validate();
            }
        });
        checkbox = (CheckBox) view.findViewById(R.id.display_pwd);
        checkbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //code to check if this checkbox is checked!
                CheckBox checkBox = (CheckBox) v;
                if (checkBox.isChecked())
                    newPassword.setTransformationMethod(null);
                else
                    newPassword.setTransformationMethod(new PasswordTransformationMethod());
                newPassword.setSelection(newPassword.getText().length());
            }
        });
        return view;
    }

    @Override
    public void onValidationSucceeded() {
        final User user = new User(LoginFragment.username, LoginFragment.pwd);
        user.fetch(new StackMobModelCallback() {

            @Override
            public void success() {
                user.loginResettingTemporaryPassword(newPassword.getText().toString(), new StackMobModelCallback() {

                    @Override
                    public void success() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                user.setTemp_pwd(false);
                                user.save(new StackMobModelCallback() {
                                    @Override
                                    public void success() {
                                        // Sauvegarde locale des identifiants pour connexion auto
                                        User.rememberMe(getActivity(), LoginFragment.username, newPassword.getText().toString());
                                        // Redirection en fonction du type utilisateur
                                        User.keepCalmAndWaitForGoingHome(getActivity(), user);
                                    }

                                    @Override
                                    public void failure(StackMobException e) {
                                        Log.d("TOUTAFAIT", "save resetpwd " + e.getMessage());
                                    }
                                });

                            }
                        });
                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT", "resetpwd " + e.getMessage());
                    }
                });


            }

            final User user = new User(LoginFragment.username, LoginFragment.pwd);

            @Override
            public void failure(StackMobException e) {

            }
        });

    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
