package tipsy.app.membre;

import android.app.Activity;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;

import java.util.ArrayList;
import java.util.Iterator;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.Event;
import tipsy.commun.Membre;
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
    private ArrayList<FormParticipation> formParticipations = new ArrayList<FormParticipation>();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        event = getArguments().getParcelable("Event");

        View view = inflater.inflate(R.layout.frag_event_participer, container, false);
        listView = (ListView) view.findViewById(R.id.list);

        panier.setPrixTotalView((TextView) view.findViewById(R.id.prix_total));


        Iterator it = panier.iterator();
        Item item;
        boolean first = true;
        while(it.hasNext()){
            item = (Item) it.next();
            for(int i=0; i<item.getQuantite(); ++i){
                Participation p = new Participation();
                p.setBillet(item.getProduit());
                p.setEvent(event);
                // On initialise le premier billet avec les infos de l'utilisateur
                if(first){
                    TipsyApp app = (TipsyApp) getActivity().getApplication();
                    p.setMembre(app.getMembre());
                    first = false;
                }
                participations.add(p);
                formParticipations.add(new FormParticipation(p));
            }
        }


        adapter = new ParticipationArrayAdapter();
        listView.setAdapter(adapter);



        ImageButton buttonPay = (ImageButton) view.findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextValidation(0);
            }
        });

        return view;
    }


    // VALIDATION DES PARTICIPATIONS
    public void nextValidation(int index){
        if(index>=formParticipations.size()){
            Toast.makeText(getActivity(),"All validate",Toast.LENGTH_SHORT).show();
            //callback.goToCommande(event);
        }else{
            formParticipations.get(index).validate(index);
        }
    }




    public class ParticipationArrayAdapter extends ArrayAdapter<Participation> {

        public ParticipationArrayAdapter() {
            super(getActivity(), R.layout.frag_event_participer_billet, participations);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if( convertView == null ){
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.frag_event_participer_billet, parent, false);
                formParticipations.get(position).setViews(convertView);
            }
            return convertView;
        }
    }

    public class FormParticipation implements Validator.ValidationListener{
        private Participation participation;
        @Required(order=1)
        private EditText inputNom;
        @Required(order=2)
        private EditText inputPrenom;
        @Required(order=3)
        @Email(order=4)
        private EditText inputEmail;
        protected Validator validator;
        private TextView nomBillet;
        private TextView prixBillet;
        private int index;

        public FormParticipation(Participation p){
            participation = p;
        }

        public void validate(int index){
            this.index = index;
            validator = new Validator(this);
            validator.setValidationListener(this);
            Log.d("TOUTAFAIT", "nom:" + inputNom.getText().toString());
            validator.validate();
        }

        public void onValidationSucceeded() {
            Log.d("TOUTAFAIT", "index valide:" + Integer.toString(index));
            nextValidation(index + 1);
        }

        public void onValidationFailed(View failedView, Rule<?> failedRule) {
            final String message = failedRule.getFailureMessage();

            if (failedView instanceof EditText) {
                failedView.requestFocus();
                ((EditText) failedView).setError(message);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                });
            }

        }

        public void setViews(View view){

            nomBillet = (TextView) view.findViewById(R.id.nom_billet);
            nomBillet.setText(participation.getBillet().getNom());

            prixBillet = (TextView) view.findViewById(R.id.prix_billet);
            prixBillet.setText(Commerce.prixToString(participation.getBillet().getPrix(), participation.getBillet().getDevise()));

            if(participation.isParticipantDefined()){
                inputNom = (EditText) view.findViewById(R.id.input_nom);
                inputNom.setText(participation.getNom());

                inputPrenom = (EditText) view.findViewById(R.id.input_prenom) ;
                inputPrenom.setText(participation.getPrenom());

                inputEmail = (EditText) view.findViewById(R.id.input_email);
                inputEmail.setText(participation.getEmail());
            }
        }
    }


}
