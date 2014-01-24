package com.tipsy.app.membre.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.lib.commerce.Commerce;
import com.tipsy.lib.commerce.Wallet;
import com.tipsy.lib.commerce.WalletCallback;

/**
 * Created by Alexandre on 23/12/13.
 */
public class WalletCreditFragment extends Fragment implements Validator.ValidationListener {

    private WalletListener callback;

    @Required(order = 1, message = "Champ Requis")
    @NumberRule(order = 2, type = NumberRule.NumberType.DOUBLE)
    private EditText inputMontant;
    private Button buttonCredit;
    private Validator validator;
    private Wallet wallet;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (WalletListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet_credit, container, false);

        TipsyApp app = (TipsyApp) getActivity().getApplication();
        //wallet = app.getWallet();

        inputMontant = (EditText) view.findViewById(R.id.input_montant);
        buttonCredit = (Button) view.findViewById(R.id.button_credit);
        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonCredit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validator.validate();
            }
        });
        return view;
    }


    public void onValidationSucceeded() {
        // On recupère le montant du crédit
        int montant = Commerce.parsePrix(inputMontant);
        final ProgressDialog wait = Wallet.getProgressDialog(getActivity());
        wallet.credit(montant, new WalletCallback() {
            @Override
            public void onWait() {
                wait.show();
            }

            @Override
            public void onSuccess() {
                wait.dismiss();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Rechargement effectué", Toast.LENGTH_LONG).show();
                    }
                });
                getActivity().finish();
            }

            @Override
            public void onFailure(Exception e) {
                wait.dismiss();
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }
}
