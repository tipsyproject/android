package com.tipsy.app.membre.event;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
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
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;

import java.io.Serializable;

import com.tipsy.app.R;
import com.tipsy.lib.Participant;
import com.tipsy.lib.TipsyUser;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.Commande;
import com.tipsy.lib.util.Commerce;
import com.tipsy.lib.util.Item;
import com.tipsy.lib.util.Panier;

/**
 * Created by Valentin on 30/12/13.
 */
public class EventParticiperFragment extends ListFragment {

    private AchatArrayAdapter adapter;
    private Commande commande;
    private int userParticipation = -1;
    private EventMembreListener callback;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EventMembreListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new AchatArrayAdapter();
        setListAdapter(adapter);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Si l'utilisateur s'était déjà attribué un billet
                if (userParticipation >= 0)
                    commande.get(userParticipation).setUser(null);
                commande.get(i).setUser(TipsyUser.getCurrentUser());
                adapter.notifyDataSetChanged();
                userParticipation = i;
                return true;
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState == null)
            commande =  getArguments().getParcelable("Commande");
        else commande = savedInstanceState.getParcelable("Commande");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
        editParticipant(commande.get(position));
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
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
            case R.id.action_validate:
                validate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(outState==null)
            outState = new Bundle();
        outState.putParcelable("Commande", commande);
        super.onSaveInstanceState(outState);
    }

    /* Affichage du dialog d'edition d'un participant */
    public void editParticipant(Achat a) {
        ParticipantDialogFragment dialog = new ParticipantDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("Achat", a);
        args.putSerializable("Adapter", adapter);
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "ParticipantDialogFragment");
    }

    public void validate(){
        for(Achat a: commande){
            if(!a.isUserDefined()){
                Toast.makeText(getActivity(),"Tous les billets sont nominatifs !",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        callback.goToCommande(commande);
    }


    public class AchatArrayAdapter extends ArrayAdapter<Achat> implements Serializable {

        public AchatArrayAdapter() {
            super(getActivity(), R.layout.frag_event_participer_billet,commande);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Achat a = commande.get(position);

            int layout = a.isUserDefined() ?
                    R.layout.frag_event_participer_billet_ok :
                    R.layout.frag_event_participer_billet;

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(layout, parent,false);

            /* Nom du billet */
            TextView nom_billet = (TextView) view.findViewById(R.id.nom_billet);
            nom_billet.setText(a.getTicket().getNom());

            /* Prix du billet */
            TextView prix_billet = (TextView) view.findViewById(R.id.prix_billet);
            prix_billet.setText(Commerce.prixToString(a.getTicket().getPrix(), a.getTicket().getDevise()));

            if(a.isUserDefined()){
                /* Prénom participant */
                TextView prenomParticipant = (TextView) view.findViewById(R.id.prenom_participant);
                prenomParticipant.setText(a.getPrenom());

                /* Nom participant */
                TextView nomParticipant = (TextView) view.findViewById(R.id.nom_participant);
                nomParticipant.setText(a.getNom());

                /* Email participant */
                TextView emailParticipant = (TextView) view.findViewById(R.id.email_participant);
                emailParticipant.setText(a.getEmail());
            }

            return view;
        }
    }



    public static class ParticipantDialogFragment extends DialogFragment implements Validator.ValidationListener {

        private Achat achat;
        private AchatArrayAdapter adapter;
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
            achat = getArguments().getParcelable("Achat");
            adapter = (AchatArrayAdapter) getArguments().getSerializable("Adapter");
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Definition du titre du Dialog
            builder.setTitle(achat.getTicket().getNom() + " - " +
                Commerce.prixToString(achat.getTicket().getPrix(),achat.getTicket().getDevise()));

            // Definition du contenu du Dialog
            View view = inflater.inflate(R.layout.frag_event_edit_participant, null);
            builder.setView(view);

            inputNom = (EditText) view.findViewById(R.id.input_nom);
            inputPrenom = (EditText) view.findViewById(R.id.input_prenom);
            inputEmail = (EditText) view.findViewById(R.id.input_email);
            // Préremplissage des widgets avec les valeur du billet si c'est une modification
            // sinon rien pour une creation
            if (achat.isUserDefined()) {
                inputNom.setText(achat.getNom());
                inputPrenom.setText(achat.getPrenom());
                inputEmail.setText(achat.getEmail());
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
                    Toast.makeText(getActivity(),"Formulaire incomplet...",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
