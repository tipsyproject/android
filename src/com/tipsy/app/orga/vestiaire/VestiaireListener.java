package com.tipsy.app.orga.vestiaire;

import com.tipsy.app.orga.entree.NFCCallback;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.Vestiaire;
import com.tipsy.lib.util.Bracelet;

import java.util.ArrayList;

/**
 * Created by vquefele on 20/01/14.
 */
public interface VestiaireListener {

    public String getEventId();
    public ArrayList<Vestiaire> getTickets();
    public ArrayList<Participant> getParticipants();
    //public ArrayList<Vestiaire> getCommande();
    public Participant getCurrentParticipant();
    public int getCurrentNumber();

    public void modelUpdated();

}
