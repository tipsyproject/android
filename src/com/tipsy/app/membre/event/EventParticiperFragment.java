package com.tipsy.app.membre.event;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.tipsy.app.R;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Commande;
import com.tipsy.lib.Commerce;
import com.tipsy.lib.Item;
import com.tipsy.lib.Panier;
import com.tipsy.lib.Participant;
import com.tipsy.lib.TipsyUser;

import java.io.Serializable;

/**
 * Created by Valentin on 30/12/13.
 */
public class EventParticiperFragment extends Fragment {

    private Panier panier;
    private AchatArrayAdapter adapter;
    private Commande achats;
    private int userParticipation = -1;
    private ListView listView;
    private EventMembreListener callback;


    public static EventParticiperFragment init(Panier p) {
        EventParticiperFragment frag = new EventParticiperFragment();
        Bundle args = new Bundle();
        args.putParcelable("Panier", p);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EventMembreListener) activity;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null && bundle.containsKey("Achats")) {
            achats = bundle.getParcelable("Achats");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelable("Achats", achats);
        // Add variable to outState here
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        panier = getArguments().getParcelable("Panier");

        View view = inflater.inflate(R.layout.frag_event_participer, container, false);
        listView = (ListView) view.findViewById(R.id.list);

        if (savedInstanceState == null) {
            achats = new Commande();
            for (Item item : panier) {
                for (int i = 0; i < item.getQuantite(); ++i) {
                    Achat a = new Achat(item.getTicket());
                    a.setParticipant(new Participant(callback.getEvent()));
                    achats.add(a);
                }
            }

        }

        adapter = new AchatArrayAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editParticipant(achats.get(i));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Si l'utilisateur s'était déjà attribué un billet
                if (userParticipation >= 0)
                    achats.get(userParticipation).getParticipant().setUser(null);
                achats.get(i).getParticipant().setUser(TipsyUser.getCurrentUser());
                adapter.notifyDataSetChanged();
                userParticipation = i;
                return true;
            }
        });
        ImageButton buttonPay = (ImageButton) view.findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validate();
            }
        });

        return view;
    }

    /* Message d'infos sur long click billet user */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity(), "Appuyez longtemps sur votre propre billet !", Toast.LENGTH_SHORT).show();
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

    public void validate() {
        for (Achat a : achats) {
            if (!a.isParticipantDefined()) {
                Toast.makeText(getActivity(), "Tous les billets sont nominatifs !", Toast.LENGTH_SHORT).show();
                return;
            }

            // On définit les billets comme non utilisés
            a.setUsed(false);
        }
        callback.goToCommande(panier, new Commande(achats));
    }


    public class AchatArrayAdapter extends ArrayAdapter<Achat> implements Serializable {

        public AchatArrayAdapter() {
            super(getActivity(), R.layout.frag_event_participer_billet, achats);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Achat a = achats.get(position);

            int layout = a.isParticipantDefined() ?
                    R.layout.frag_event_participer_billet_ok :
                    R.layout.frag_event_participer_billet;

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(layout, parent, false);

            /* Nom du billet */
            TextView nom_billet = (TextView) view.findViewById(R.id.nom_billet);
            nom_billet.setText(a.getProduit().getNom());

            /* Prix du billet */
            TextView prix_billet = (TextView) view.findViewById(R.id.prix_billet);
            prix_billet.setText(Commerce.prixToString(a.getProduit().getPrix(), a.getProduit().getDevise()));

            if (a.isParticipantDefined()) {
                /* Prénom participant */
                TextView prenomParticipant = (TextView) view.findViewById(R.id.prenom_participant);
                prenomParticipant.setText(a.getParticipant().getPrenom());

                /* Nom participant */
                TextView nomParticipant = (TextView) view.findViewById(R.id.nom_participant);
                nomParticipant.setText(a.getParticipant().getNom());

                /* Email participant */
                TextView emailParticipant = (TextView) view.findViewById(R.id.email_participant);
                emailParticipant.setText(a.getParticipant().getEmail());
            }

            return view;
        }
    }


    public static class ParticipantDialogFragment extends DialogFragment implements Validator.ValidationListener {

        private Achat achat;
        private AchatArrayAdapter adapter;
        @Required(order = 1, message = "Champ requis")
        private EditText inputNom;
        @Required(order = 2, message = "Champ requis")
        private EditText inputPrenom;
        @Required(order = 3, message = "Champ requis")
        @Email(order = 4, message = "Email incorrect")
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
            builder.setTitle(achat.getProduit().getNom() + " - " +
                    Commerce.prixToString(achat.getProduit().getPrix(), achat.getProduit().getDevise()));

            // Definition du contenu du Dialog
            View view = inflater.inflate(R.layout.frag_event_edit_participant, null);
            builder.setView(view);

            inputNom = (EditText) view.findViewById(R.id.input_nom);
            inputPrenom = (EditText) view.findViewById(R.id.input_prenom);
            inputEmail = (EditText) view.findViewById(R.id.input_email);
            // Préremplissage des widgets avec les valeur du billet si c'est une modification
            // sinon rien pour une creation
            if (achat.isParticipantDefined()) {
                inputNom.setText(achat.getParticipant().getNom());
                inputPrenom.setText(achat.getParticipant().getPrenom());
                inputEmail.setText(achat.getParticipant().getEmail());
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
            // On recupère le contenu des champs
            achat.getParticipant().setNom(inputNom.getText().toString());
            achat.getParticipant().setPrenom(inputPrenom.getText().toString());
            achat.getParticipant().setEmail(inputEmail.getText().toString());
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
