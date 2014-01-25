package com.tipsy.app.orga.billetterie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.lib.Commerce;
import com.tipsy.lib.Ticket;

/**
 * Created by valoo on 25/01/14.
 */
public class EditBilletFragment extends Fragment implements Validator.ValidationListener {

    private BilletterieListener callback;
    private int index;
    private Ticket billet;

    @Required(order = 1)
    private EditText inputNom;
    @Required(order = 2)
    @NumberRule(order = 3, type = NumberRule.NumberType.FLOAT)
    private EditText inputPrix;

    private TextView devise;
    private Validator validator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BilletterieListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState == null){
            if(getArguments() != null && getArguments().containsKey("BILLET_INDEX")){
                index = getArguments().getInt("BILLET_INDEX");
                billet = callback.getBilletterie().get(index);
            }else{
                index = -1;
                billet = new Ticket();
                billet.setType(Ticket.BILLET);
            }
        }else{
            index = savedInstanceState.getInt("index");
            billet = savedInstanceState.getParcelable("Billet");
        }

        View view = inflater.inflate(R.layout.frag_billetterie_edit, container, false);

        inputNom = (EditText) view.findViewById(R.id.input_nom);
        inputPrix = (EditText) view.findViewById(R.id.input_prix);
        devise = (TextView) view.findViewById(R.id.devise);
        // Préremplissage des widgets avec les valeur du billet si c'est une modification
        // sinon rien pour une creation
        if (index >=0) {
            inputNom.setText(billet.getNom());
            inputPrix.setText(Commerce.prixToString(billet.getPrix()));
            devise.setText(Commerce.Devise.getSymbol(billet.getDevise()));
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        if(outState == null)
            outState = new Bundle();
        outState.putParcelable("Billet",billet);
        outState.putInt("index", index);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                callback.backToHome();
                return true;
            case R.id.action_validate:
                validator.validate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onValidationSucceeded() {
        // Si billet nouveau
        if(index < 0)
            billet.setEvent(callback.getEvent());
        // On recupère le contenu des champs
        billet.setNom(inputNom.getText().toString());
        billet.setPrix(Commerce.parsePrix(inputPrix));

        final ProgressDialog wait = ProgressDialog.show(getActivity(),"","Enregistrement...",true,true);

        billet.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    // Si billet nouveau
                    if (index < 0)
                        callback.getBilletterie().add(billet);
                    else
                        callback.getBilletterie().set(index, billet);
                    callback.goToListeBillets();
                }else
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                wait.dismiss();
            }
        });
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        }else Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
