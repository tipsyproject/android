package com.tipsy.app.orga.bar;

import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.Vestiaire;
import com.tipsy.lib.util.EventModule;
import com.tipsy.lib.util.Item;
import com.tipsy.lib.util.Panier;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;

/**
 * Created by tech on 11/03/14.
 */
public interface BarListener {

    public String getEventId();
    public Panier getPanier();
    public ArrayList<Ticket> getConsos();
    public ArrayList<Participant> getParticipants();


    public void increaseConso(Ticket ticket);

    public void decreaseConso(Item item);

    public void validerPanier();

    public void modelUpdated();

}
