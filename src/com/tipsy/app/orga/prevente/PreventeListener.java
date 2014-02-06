package com.tipsy.app.orga.prevente;

import com.tipsy.lib.Achat;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.EventModule;
import com.tipsy.lib.util.QueryCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by vquefele on 20/01/14.
 */
public interface PreventeListener {
    public Achat getPrevente();
    public ArrayList<Ticket> getBilletterie();
    public void backToEntrees(boolean includePrevente);
    public void goToScan();
    public void goToTarifs();
    public void goToParticipant();
}
