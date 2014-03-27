package com.tipsy.app.orga.entree;

import com.tipsy.app.orga.entree.old.EntreeNFCFragment;
import com.tipsy.app.orga.entree.old.NFCCallback;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;

import java.util.ArrayList;

/**
 * Created by vquefele on 20/01/14.
 */
public interface EntreeListener {

    public String getEventId();
    public Event getEvent();
    public void setEvent(Event event);
    public ArrayList<Achat> getEntrees();
    public ArrayList<Ticket> getBilletterie();
    public int getCurrentMode();

    public void modelUpdated();


    public void OK(String m1, String m2);
    public void KO(String m1, String m2);

    /*
    public EntreeNFCFragment getFragNFC();
    public void setNFCCallback(NFCCallback cb);
    public void updateProgress();
    public int findBracelet(Bracelet b);
    public void activationManuelle(final Achat entree);
    public void setTarifVente(Ticket ticket);
    public void setParticipantInfos(Participant p);

    public void modeStats();
    public void modeQRCode();
    public void modeListe();
    public void modeVente();
    public void OK(String m1, String m2);
    public void KO(String m1, String m2);*/
}
