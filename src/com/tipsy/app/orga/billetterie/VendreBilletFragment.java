package com.tipsy.app.orga.billetterie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.tipsy.app.R;
import com.tipsy.app.membre.event.EventParticiperFragment;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Commerce;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by valoo on 25/01/14.
 */
public class VendreBilletFragment extends ListFragment {

    private BilletterieListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BilletterieListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BilletsArrayAdapter adapter = new BilletsArrayAdapter(getActivity(), callback.getBilletterie());
        setListAdapter(adapter);
        setEmptyText(getString(R.string.empty_liste_billets));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_billetterie_vendre, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_billetterie_vendre, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ParticipantDialogFragment dialog = new ParticipantDialogFragment();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "ParticipantDialogFragment");
    }

    // Adapter BILLETS
    public class BilletsArrayAdapter extends ArrayAdapter<Ticket> implements Serializable {
        private Context context;
        private ArrayList<Ticket> billets;

        public BilletsArrayAdapter(Context context, ArrayList<Ticket> billets) {
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
            Ticket b = billets.get(position);
            nomBillet.setText(b.getNom());
            prixBillet.setText(Commerce.prixToString(b.getPrix(), b.getDevise()));
            return viewBillet;
        }
    }



    public static class ParticipantDialogFragment extends DialogFragment implements Validator.ValidationListener {

        private Achat achat;
        private EventParticiperFragment.AchatArrayAdapter adapter;
        @Required(order=1, message="Champ requis")
        private EditText inputNom;
        @Required(order=2, message="Champ requis")
        private EditText inputPrenom;
        @Required(order=3, message="Champ requis")
        @Email(order=4, message="Email incorrect")
        private EditText inputEmail;
        private Validator validator;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            adapter = (EventParticiperFragment.AchatArrayAdapter) getArguments().getSerializable("Adapter");
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Definition du titre du Dialog
            builder.setTitle(achat.getTicket().getNom() + " - " +
                    Commerce.prixToString(achat.getTicket().getPrix(),achat.getTicket().getDevise()));

            // Definition du contenu du Dialog
            View view = inflater.inflate(R.layout.frag_billetterie_popup, null);
            builder.setView(view);

            inputNom = (EditText) view.findViewById(R.id.input_nom);
            inputPrenom = (EditText) view.findViewById(R.id.input_prenom);
            inputEmail = (EditText) view.findViewById(R.id.input_email);

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
            Participant user = new Participant();
            // On recupère le contenu des champs
            user.setNom(inputNom.getText().toString());
            user.setPrenom(inputPrenom.getText().toString());
            user.setEmail(inputEmail.getText().toString());
            achat.setParticipant(user);
            adapter.notifyDataSetChanged();

        }

        public void onValidationFailed(View failedView, Rule<?> failedRule) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Formulaire incomplet...", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
