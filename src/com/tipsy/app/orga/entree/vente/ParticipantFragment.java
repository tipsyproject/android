package com.tipsy.app.orga.entree.vente;

import android.app.Activity;
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
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeListener;
import com.tipsy.app.orga.prevente.PreventeListener;
import com.tipsy.lib.Participant;

/**
 * Created by valoo on 25/01/14.
 */
public class ParticipantFragment extends Fragment implements Validator.ValidationListener {

    private EntreeListener callback;
    private EditText inputNom;
    private EditText inputPrenom;
    @Email(order = 4, message = "Email incorrect")
    private EditText inputEmail;
    private Validator validator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
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
        // On recupère le contenu des champs
        Participant p = new Participant();
        p.setNom(inputNom.getText().toString());
        p.setPrenom(inputPrenom.getText().toString());
        p.setEmail(inputEmail.getText().toString());
        callback.setParticipantInfos(p);
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        Toast.makeText(getActivity(), "Formulaire incomplet...", Toast.LENGTH_SHORT).show();
    }
}
