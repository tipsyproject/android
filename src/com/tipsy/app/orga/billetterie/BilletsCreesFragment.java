package com.tipsy.app.orga.billetterie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.io.Serializable;
import java.util.ArrayList;

import com.tipsy.app.R;
import com.tipsy.lib.Event;
import com.tipsy.lib.billetterie.Billet;
import com.tipsy.lib.commerce.Commerce;
import com.tipsy.lib.commerce.Produit;

/**
 * Created by valoo on 27/12/13.
 */
public class BilletsCreesFragment extends Fragment {

    private BilletsArrayAdapter adapter;
    private Event event;
    private ListView listView;
    private BilletterieListener callback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BilletterieListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_billetterie_liste_billets, container, false);
        /* On récupère l'eventOld courant */
        event = callback.getEvent();

        listView = (ListView) view.findViewById(R.id.list);
        adapter = new BilletsArrayAdapter(getActivity(), callback.getBilletterie());
        listView.setAdapter(adapter);
        listView.setEmptyView( inflater.inflate(R.layout.empty_liste_billets, null));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editBillet(position);
            }
        });
        return view;
    }

    // Redéfinition de l'actionBar: Bouton de validation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_billetterie_liste_billets, menu);
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_new_billet:
                newBillet();
                return true;
            case R.id.action_liste_ventes:
                callback.showListeVentes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void editBillet(int indexBillet) {
        // Create an instance of the dialog fragment and show it
        EditBilletDialogFragment dialog = new EditBilletDialogFragment();
        Bundle args = new Bundle();
        args.putInt("INDEX_BILLET", indexBillet);
        args.putSerializable("Adapter", adapter);
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "EditBilletDialogFragment");
    }

    public void newBillet() {
        // Create an instance of the dialog fragment and show it
        EditBilletDialogFragment dialog = new EditBilletDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("Adapter", adapter);
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "EditBilletDialogFragment");
    }




    // Adapter BILLETS
    public class BilletsArrayAdapter extends ArrayAdapter<Billet> implements Serializable {
        private Context context;
        private ArrayList<Billet> billets;

        public BilletsArrayAdapter(Context context, ArrayList<Billet> billets) {
            super(context, R.layout.frag_billet_list, billets);
            this.context = context;
            this.billets = billets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewBillet = inflater.inflate(R.layout.frag_billet_list, parent, false);
            TextView nomBillet = (TextView) viewBillet.findViewById(R.id.nom_billet);
            TextView prixBillet = (TextView) viewBillet.findViewById(R.id.prix_billet);
            Billet b = billets.get(position);
            nomBillet.setText(b.getNom());
            prixBillet.setText(Commerce.prixToString(b.getPrix(), b.getDevise()));
            return viewBillet;
        }
    }


    // Dialog Fragment permettant de créer/modifier un billet

    public static class EditBilletDialogFragment extends DialogFragment implements Validator.ValidationListener {

        private Billet billet;
        private boolean newBillet;
        private BilletsArrayAdapter adapter;
        private BilletterieListener callback;

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

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            newBillet = !getArguments().containsKey("INDEX_BILLET");
            if(newBillet){
                billet = new Billet();
                billet.setType(Produit.BILLET);
            }
            else
                billet = callback.getBilletterie().get(getArguments().getInt("INDEX_BILLET"));

            adapter = (BilletsArrayAdapter) getArguments().getSerializable("Adapter");
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Definition du titre du Dialog
            builder.setCustomTitle(inflater.inflate(R.layout.frag_edit_billet_title, null));
            // Definition du contenu du Dialog
            View view = inflater.inflate(R.layout.frag_edit_billet, null);
            builder.setView(view);

            inputNom = (EditText) view.findViewById(R.id.input_nom);
            inputPrix = (EditText) view.findViewById(R.id.input_prix);
            devise = (TextView) view.findViewById(R.id.devise);
            // Préremplissage des widgets avec les valeur du billet si c'est une modification
            // sinon rien pour une creation
            if (!newBillet) {
                inputNom.setText(billet.getNom());
                inputPrix.setText(Commerce.prixToString(billet.getPrix()));
                devise.setText(Commerce.Devise.getSymbol(billet.getDevise()));
            }

            validator = new Validator(this);
            validator.setValidationListener(this);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Validation du formulaire de Billet
                    validator.validate();
                }
            })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        public void onValidationSucceeded() {
            if(newBillet)
                billet.setEvent(callback.getEvent());

            // On recupère le contenu des champs
            billet.setNom(inputNom.getText().toString());
            billet.setPrix(Commerce.parsePrix(inputPrix));

            final ProgressDialog wait = ProgressDialog.show(getActivity(),"","Enregistrement...",true,true);

            billet.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        if (newBillet)
                            callback.getBilletterie().add(billet);
                        adapter.notifyDataSetChanged();
                    }
                    wait.dismiss();
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

}
