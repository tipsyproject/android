package com.tipsy.app.orga.vestiaire.in;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tipsy.app.R;
import com.tipsy.lib.Vestiaire;

import java.util.ArrayList;

/**
 * Created by tech on 11/03/14.
 */
public class CarnetFragment extends Fragment {

    private CarnetListener listener;
    private CarnetAdapter listAdapter;

    ImageButton buttonVetements;
    ImageButton buttonSacs;

    /* Carnet de tickets */
    private Carnet carnetVetements;
    private Carnet carnetSacs;
    private Carnet currentCarnet;
    private int currentCarnetType;

    /* Pour connaitre l'action à effectuer
    lorsqu'un numéro est généré via la dialog */
    private boolean newTicket;


    public interface CarnetListener {
        public void onTicketChoosen(Vestiaire ticket);
        public String getEventId();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (CarnetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CarnetListener");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_vestiaire_tickets, container, false);

        /* Bouton MODE VETEMENTS */
        buttonVetements = (ImageButton) view.findViewById(R.id.buttonVetements);
        buttonVetements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCarnet(Vestiaire.VETEMENTS);
            }
        });

        /* BOUTON MODE SACS */
        buttonSacs = (ImageButton) view.findViewById(R.id.buttonSacs);
        buttonSacs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCarnet(Vestiaire.SACS);
            }
        });

        /* AJOUTER UN TICKET */
        ImageButton buttonNewTicket = (ImageButton) view.findViewById(R.id.buttonNewTicket);
        buttonNewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTicket = true;
                DialogFragment newFragment = new TicketNumberFragment("Ajouter un ticket");
                newFragment.show(getChildFragmentManager(), "numberpicker");
            }
        });

        /* DEFINIR LE POINT DE DEPART */
        ImageButton buttonRefresh = (ImageButton) view.findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTicket = false;
                DialogFragment newFragment = new TicketNumberFragment("Ticket de départ",currentCarnet.guessNextTicketNumber());
                newFragment.show(getChildFragmentManager(), "numberpicker");
            }
        });


        /*
        if(savedInstanceState != null){
            carnetVetements = savedInstanceState.getParcelable("carnetVetements");
            carnetSacs = savedInstanceState.getParcelable("carnetSacs");
            currentCarnetType = savedInstanceState.getInt("currentCarnetType");
        }else {
            carnetVetements = new Carnet(listener.getEventId(), Vestiaire.VETEMENTS);
            carnetSacs = new Carnet(listener.getEventId(), Vestiaire.SACS);
            currentCarnetType = Vestiaire.VETEMENTS;
        }
        currentCarnet = (currentCarnetType == Vestiaire.VETEMENTS) ? carnetVetements : carnetSacs;
        */
        carnetVetements = new Carnet(listener.getEventId(), Vestiaire.VETEMENTS);
        carnetSacs = new Carnet(listener.getEventId(), Vestiaire.SACS);
        currentCarnet = carnetVetements;

        ListView listView = (ListView) view.findViewById(R.id.listTickets);
        listAdapter = new CarnetAdapter(getActivity(),currentCarnet.getNextTickets());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* On détache le ticket du carnet */
                Vestiaire ticket = currentCarnet.detach(position);
                /* On passe le ticket à l'activity */
                listener.onTicketChoosen(ticket);
                listAdapter.notifyDataSetChanged();
            }
        });

        /* Suppression d'un ticket sur un long click */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Retirer ce ticket");
                builder.setMessage("Ce ticket sera retiré de la liste jusqu'au prochain démarrage.")
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                currentCarnet.detach(position);
                                listAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
                return true;
            }
        });


        //setCarnet(currentCarnetType);
        setCarnet(Vestiaire.VETEMENTS);
        return view;
    }

    public void numberDefined(int num){
        if(newTicket){
            listener.onTicketChoosen(new Vestiaire(num,currentCarnet.getType(),listener.getEventId()));
        }else{
            currentCarnet.genNextTickets(num,true);
            listAdapter.notifyDataSetChanged();
        }
    }

    public void add(Vestiaire ticket){
        if(ticket.getType() == Vestiaire.VETEMENTS)
            carnetVetements.attach(ticket);
        else
            carnetSacs.attach(ticket);
        listAdapter.notifyDataSetChanged();
    }

    public void updateCarnets(ArrayList<Vestiaire> vestiaires){
        carnetVetements.synchronize(vestiaires);
        carnetSacs.synchronize(vestiaires);
        listAdapter.notifyDataSetChanged();
    }

    private void setCarnet(int carnet){
        if(carnet == Vestiaire.VETEMENTS) {
            currentCarnet = carnetVetements;
            buttonVetements.setBackgroundResource(R.color.primary);
            buttonSacs.setBackgroundResource(R.color.background_dark);
        }else {
            currentCarnet = carnetSacs;
            buttonVetements.setBackgroundResource(R.color.background_dark);
            buttonSacs.setBackgroundResource(R.color.primary);
        }
        listAdapter.useCarnet(currentCarnet);
        listAdapter.notifyDataSetChanged();
    }


    /*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelable("carnetVetements", carnetVetements);
        outState.putParcelable("carnetSacs", carnetSacs);
        outState.putInt("currentCarnetType", currentCarnetType);
        super.onSaveInstanceState(outState);
    }*/
}
