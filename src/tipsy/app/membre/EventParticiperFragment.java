package tipsy.app.membre;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.Event;
import tipsy.commun.Membre;
import tipsy.commun.billetterie.Participant;
import tipsy.commun.billetterie.Participation;
import tipsy.commun.commerce.Commerce;
import tipsy.commun.commerce.Item;
import tipsy.commun.commerce.ItemArrayAdapter;
import tipsy.commun.commerce.Panier;
import tipsy.commun.commerce.QuantiteDialogFragment;

/**
 * Created by Valentin on 30/12/13.
 */
public class EventParticiperFragment extends Fragment {

    private Panier panier;
    private ParticipationArrayAdapter adapter;
    private Event event;
    private ArrayList<Participation> participations = new ArrayList<Participation>();
    private int userParticipation = -1;
    private ListView listView;
    private MembreListener callback;


    public static EventParticiperFragment init(Event e) {
        EventParticiperFragment frag = new EventParticiperFragment();
        Bundle args = new Bundle();
        args.putParcelable("Event", e);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        panier = app.getPanier();
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        if(bundle != null && bundle.containsKey("Participations")){
            participations = bundle.getParcelableArrayList("Participations");
        }
    }
    @Override
     public void onSaveInstanceState(Bundle outState) {
        if(outState==null)
            outState = new Bundle();
        outState.putParcelableArrayList("Participations",participations);
        // Add variable to outState here
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        event = getArguments().getParcelable("Event");

        View view = inflater.inflate(R.layout.frag_event_participer, container, false);

        listView = (ListView) view.findViewById(R.id.list);

        if(savedInstanceState == null){
            TipsyApp app = (TipsyApp) getActivity().getApplication();
            Membre proprietaire = app.getMembre();
            Iterator it = panier.iterator();
            Item item;
            while(it.hasNext()){
                item = (Item) it.next();
                for(int i=0; i<item.getQuantite(); ++i){
                    Participation p = new Participation();
                    p.setBillet(item.getProduit());
                    p.setEvent(event);
                    p.setProprietaire(proprietaire);
                    participations.add(p);
                }
            }

        }

        adapter = new ParticipationArrayAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editParticipant(participations.get(i));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Si l'utilisateur s'était déjà attribué un billet
                if(userParticipation>=0)
                    participations.get(userParticipation).setParticipant(null);
                participations.get(i).setProprietaireParticipant();
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
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity(),"Appuyez longtemps sur votre propre billet !",Toast.LENGTH_SHORT).show();
    }

    /* Affichage du dialog d'edition d'un participant */
    public void editParticipant(Participation p) {
        ParticipantDialogFragment dialog = new ParticipantDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("Participation", p);
        args.putSerializable("Adapter", adapter);
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "ParticipantDialogFragment");
    }

    public void validate(){
        for(Participation p: participations){
            if(p.getParticipant() == null){
                Toast.makeText(getActivity(),"Tous les billets sont nominatifs !",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        callback.goToCommande(event);
    }


    public class ParticipationArrayAdapter extends ArrayAdapter<Participation> implements Serializable {

        public ParticipationArrayAdapter() {
            super(getActivity(), R.layout.frag_event_participer_billet, participations);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Participation p = participations.get(position);

            int layout = p.getParticipant() != null ?
                    R.layout.frag_event_participer_billet_ok :
                    R.layout.frag_event_participer_billet;

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(layout, parent,false);

            /* Nom du billet */
            TextView nom_billet = (TextView) view.findViewById(R.id.nom_billet);
            nom_billet.setText(p.getBillet().getNom());

            /* Prix du billet */
            TextView prix_billet = (TextView) view.findViewById(R.id.prix_billet);
            prix_billet.setText(Commerce.prixToString(p.getBillet().getPrix(), p.getBillet().getDevise()));

            if(p.getParticipant() != null){
                /* Prénom participant */
                TextView prenomParticipant = (TextView) view.findViewById(R.id.prenom_participant);
                prenomParticipant.setText(p.getParticipant().getPrenom());

                /* Nom participant */
                TextView nomParticipant = (TextView) view.findViewById(R.id.nom_participant);
                nomParticipant.setText(p.getParticipant().getNom());

                /* Email participant */
                TextView emailParticipant = (TextView) view.findViewById(R.id.email_participant);
                emailParticipant.setText(p.getParticipant().getEmail());
            }

            return view;
        }
    }



    public static class ParticipantDialogFragment extends DialogFragment implements Validator.ValidationListener {

        private Participation participation;
        private ParticipationArrayAdapter adapter;
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
            participation = getArguments().getParcelable("Participation");
            adapter = (ParticipationArrayAdapter) getArguments().getSerializable("Adapter");
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Definition du titre du Dialog
            builder.setTitle(participation.getBillet().getNom() + " - " +
                Commerce.prixToString(participation.getBillet().getPrix(),participation.getBillet().getDevise()));

            // Definition du contenu du Dialog
            View view = inflater.inflate(R.layout.frag_event_edit_participant, null);
            builder.setView(view);

            inputNom = (EditText) view.findViewById(R.id.input_nom);
            inputPrenom = (EditText) view.findViewById(R.id.input_prenom);
            inputEmail = (EditText) view.findViewById(R.id.input_email);
            // Préremplissage des widgets avec les valeur du billet si c'est une modification
            // sinon rien pour une creation
            if (participation.getParticipant() != null) {
                inputNom.setText(participation.getParticipant().getNom());
                inputPrenom.setText(participation.getParticipant().getPrenom());
                inputEmail.setText(participation.getParticipant().getEmail());
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
            participation.setParticipant(new Participant(
                    inputNom.getText().toString(),
                    inputPrenom.getText().toString(),
                    inputEmail.getText().toString()
            ));

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
