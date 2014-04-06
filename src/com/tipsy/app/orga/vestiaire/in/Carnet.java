package com.tipsy.app.orga.vestiaire.in;

import com.tipsy.lib.Vestiaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by valoo on 05/04/14.
 */
public class Carnet {

    private final static int SIZE = 10;

    /* Liste des prochains tickets */
    private ArrayList<Vestiaire> nextTickets = new ArrayList<Vestiaire>();
    /* Liste des tickets utilisés */
    private ArrayList<Vestiaire> corbeille = new ArrayList<Vestiaire>();

    private String eventId;
    private int type;

    public Carnet(String eventId, int type){
        this.eventId = eventId;
        this.type = type;
        genNextTickets(guessNextTicketNumber(),true);
    }


    /* Génère la liste des N prochains tickets */
    public void genNextTickets(int from, boolean clear){
        if(clear)
            nextTickets.clear();
        int number = from;
        while(nextTickets.size() < SIZE){
            Vestiaire ticket = new Vestiaire(number,type,eventId);
            /* On affiche seulement des numéros qui ne sont pas
             déjà utilisé sou déjà dans la liste */
            if(!nextTickets.contains(ticket) && !corbeille.contains(ticket))
                nextTickets.add(ticket);
            number++;
        }
    }


    public Vestiaire detach(int position){
        Vestiaire ticket = nextTickets.get(position);
        corbeille.add(ticket);
        nextTickets.remove(ticket);
        genNextTickets(nextTickets.get(SIZE-2).getNumber(),false);
        return ticket;
    }

    /* Rattache au carnet un ticket qui avait été détaché */
    public void attach(Vestiaire ticket){
        corbeille.remove(ticket);
        nextTickets.add(ticket);
        Collections.sort(nextTickets, Vestiaire.SORT_BY_TICKET);
    }



    /* On synchronise le carnet à partir de tous les tickets de vestiaire utilisés  */
    public void synchronize(ArrayList<Vestiaire> used){
        Iterator it = used.iterator();
        while(it.hasNext()){
            Vestiaire ticket = (Vestiaire) it.next();
            if(ticket.getType() == type)
                corbeille.add(ticket);
        }

        /* Et on regenère les prochains tickets à partir du premier numero courant */
        genNextTickets(nextTickets.get(0).getNumber(), true);
    } /* METTRE ENSUITE A JOUR L'ADAPTER */



    /* Devine le prochain numéro de ticket
        à partir de tous les tickets vendus
     */
    public int guessNextTicketNumber(){
        if(corbeille.isEmpty())
            return 1;
        else {
            Collections.sort(corbeille, Vestiaire.SORT_BY_TICKET);
            return corbeille.get(corbeille.size() - 1).getNumber() + 1;
        }
    }

    public ArrayList<Vestiaire> getNextTickets(){
        return nextTickets;
    }

    public int getType(){
        return type;
    }

}
