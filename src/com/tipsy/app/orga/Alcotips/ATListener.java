package com.tipsy.app.orga.Alcotips;

import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.Item;
import com.tipsy.lib.util.Panier;

import java.util.ArrayList;

/**
 * Created by tech on 11/03/14.
 */
public interface ATListener {

    public String getEventId();
    public void  setEvent(Event ev);
    public ArrayList<Participant> getParticipants();

    public void modelUpdated();

}
