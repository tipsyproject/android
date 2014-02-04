package com.tipsy.app.orga.prevente;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.tipsy.app.R;
import com.tipsy.app.membre.event.EventParticiperFragment;
import com.tipsy.app.orga.entree.EntreeListener;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Participant;
import com.tipsy.lib.util.Commerce;
import com.tipsy.lib.util.TarifGridAdapter;

/**
 * Created by valoo on 25/01/14.
 */
public class ParticipantFragment extends Fragment implements Validator.ValidationListener {

    private PreventeListener callback;
    private EditText inputNom;
    private EditText inputPrenom;
    @Email(order=4, message="Email incorrect")
    private EditText inputEmail;
    private Validator validator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (PreventeListener) activity;
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
        callback.getPrevente().getParticipant().setNom(inputNom.getText().toString());
        callback.getPrevente().getParticipant().setPrenom(inputPrenom.getText().toString());
        callback.getPrevente().getParticipant().setEmail(inputEmail.getText().toString());
        callback.backToEntrees();
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        Toast.makeText(getActivity(), "Formulaire incomplet...", Toast.LENGTH_SHORT).show();
    }
}
