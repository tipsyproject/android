package com.tipsy.app.orga.entree.vente;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeFragment;
import com.tipsy.lib.Participant;

/**
 * Created by valoo on 25/01/14.
 */
public class ParticipantFragment extends EntreeFragment implements Validator.ValidationListener {

    private EditText inputNom;
    private EditText inputPrenom;
    @Email(order = 4, message = "Email incorrect")
    private EditText inputEmail;
    private Validator validator;

    private ModeVenteListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (ModeVenteListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_billetterie_popup, container, false);
        inputNom = (EditText) view.findViewById(R.id.input_nom);
        inputPrenom = (EditText) view.findViewById(R.id.input_prenom);
        inputEmail = (EditText) view.findViewById(R.id.input_email);
        Button buttoNValidate = (Button) view.findViewById(R.id.button_validate);
        validator = new Validator(this);
        validator.setValidationListener(this);
        buttoNValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
        return view;
    }

    public void onValidationSucceeded() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }catch(Exception e){}

        // On recupère le contenu des champs
        Participant p = callback.getPrevente().getParticipant();
        p.setNom(inputNom.getText().toString());
        p.setPrenom(inputPrenom.getText().toString());
        p.setEmail(inputEmail.getText().toString());
        inputNom.setText("");
        inputPrenom.setText("");
        inputEmail.setText("");
        callback.finishPrevente();
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        Toast.makeText(getActivity(), "Formulaire incomplet...", Toast.LENGTH_SHORT).show();
    }
}
