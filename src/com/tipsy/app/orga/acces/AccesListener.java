package com.tipsy.app.orga.acces;

import java.util.ArrayList;

import com.tipsy.lib.Event_old;
import com.tipsy.lib.billetterie.EntreeArrayAdapter;
import com.tipsy.lib.commerce.Achat;

/**
 * Created by vquefele on 20/01/14.
 */
public interface AccesListener {
    public Event_old getEventOld();
    public ArrayList<Achat> getEntrees();

    public void backToEventOrga();

    public void goToHome(boolean addTobackStack);
    public void goToManualAccess();

    public void refresh(EntreeArrayAdapter adapter);
}
