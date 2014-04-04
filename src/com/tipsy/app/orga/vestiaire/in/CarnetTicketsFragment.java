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
import java.util.Collections;

/**
 * Created by tech on 11/03/14.
 */
public class CarnetTicketsFragment extends Fragment {

    private CarnetListener listener;
    private TicketsArrayAdapter listAdapter;

    /* Liste des prochains tickets */
    private ArrayList<Vestiaire> nextTickets = new ArrayList<Vestiaire>();
    private ArrayList<Vestiaire> commande = new ArrayList<Vestiaire>();
    private ArrayList<Vestiaire> corbeille = new ArrayList<Vestiaire>();

    /* Pour connaitre l'action à effectuer
    lorsqu'un numéro est généré via la dialog */
    private boolean newTicket;


    public interface CarnetListener {
        public void onTicketChoosen(Vestiaire ticket);
        public ArrayList<Vestiaire> getTickets();
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

        ImageButton buttonNewTicket = (ImageButton) view.findViewById(R.id.buttonNewTicket);
        buttonNewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTicket = true;
                DialogFragment newFragment = new TicketNumberFragment("Ajouter un ticket");
                newFragment.show(getChildFragmentManager(), "numberpicker");
            }
        });

        ImageButton buttonRefresh = (ImageButton) view.findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTicket = false;
                DialogFragment newFragment = new TicketNumberFragment("Changer numéro");
                newFragment.show(getChildFragmentManager(), "numberpicker");
            }
        });
        ListView listView = (ListView) view.findViewById(R.id.listTickets);
        listAdapter = new TicketsArrayAdapter(getActivity(),nextTickets);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* On passe le ticket à l'activity */
                listener.onTicketChoosen(nextTickets.get(position));
                /* On le met temporairement utilisé pour qu'il ne soit pas automatiquement regénéré */
                commande.add(nextTickets.get(position));
                /* On supprime le ticket de la liste */
                nextTickets.remove(position);
                /* On regénère la liste à partir du premier ticket restant */
                genTicketList(nextTickets.get(0).getNumber(),false);
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
                                removeTicket(nextTickets.get(position));
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

        /* Génération automatique d'une première liste de tickets */
        genTicketList(guessNextTicketNumber(),true);
        return view;
    }

    public void numberDefined(int num){
        if(newTicket){
            listener.onTicketChoosen(new Vestiaire(num,listener.getEventId()));
        }else{
            genTicketList(num,true);
        }
    }

    /* Devine le prochain numéro de ticket
        à partir de tous les tickets vendus
     */
    private int guessNextTicketNumber(){
        if(listener.getTickets().isEmpty())
            return 1;
        else {
            ArrayList<Vestiaire> tickets = new ArrayList<Vestiaire>();
            tickets.addAll(listener.getTickets());
            Collections.sort(tickets, Vestiaire.SORT_BY_TICKET);
            return tickets.get(tickets.size() - 1).getNumber() + 1;
        }
    }

    /* Génère la liste de tickets */
    private void genTicketList(int from, boolean clear){
        if(clear)
            nextTickets.clear();
        Vestiaire ticket;
        int number = from;
        while(nextTickets.size() < 10){
            ticket = new Vestiaire(number,listener.getEventId());
            /* On affiche seulement des numéros qui ne sont pas
                en attente d'être validés
                déjà utilisés
                ou jetés
            */
            if(!nextTickets.contains(ticket) &&
                    !commande.contains(ticket) &&
                !listener.getTickets().contains(ticket) &&
                !corbeille.contains(ticket))
                nextTickets.add(ticket);
            number++;
        }
        listAdapter.notifyDataSetChanged();
    }

    private void removeTicket(Vestiaire ticket){
        corbeille.add(ticket);
        nextTickets.remove(ticket);
        listAdapter.notifyDataSetChanged();
    }

    public void add(Vestiaire ticket){
        commande.remove(ticket);
        corbeille.remove(ticket);
        nextTickets.add(ticket);
        Collections.sort(nextTickets, Vestiaire.SORT_BY_TICKET);
        listAdapter.notifyDataSetChanged();
    }

    public void updateCarnet(){
        /* Génération automatique d'une première liste de tickets */
        genTicketList(guessNextTicketNumber(),true);
    }

    public void clearCommande(){
        commande.clear();
    }
}
