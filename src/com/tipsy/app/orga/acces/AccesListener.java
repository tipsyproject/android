package com.tipsy.app.orga.acces;

import com.tipsy.app.orga.billetterie.EntreeArrayAdapter;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.EventModule;

import java.util.ArrayList;

/**
 * Created by vquefele on 20/01/14.
 */
public interface AccesListener extends EventModule {
    public ArrayList<Achat> getEntrees();
    public EntreeArrayAdapter getEntreesAdapter();
    public void goToHome(boolean addTobackStack);
    public void goToManualAccess();
    public void loadVentes();
}
