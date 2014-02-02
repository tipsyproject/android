package com.tipsy.app.orga.billetterie;

import com.tipsy.app.orga.entree.EntreeArrayAdapter;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.EventModule;

import java.util.ArrayList;

/**
 * Created by valoo on 27/12/13.
 */
public interface BilletterieListener extends EventModule {

    public ArrayList<Achat> getVentes();
    public EntreeArrayAdapter getVentesAdapter();

    public void backToHome();

    public void goToEditBillet(int index);

    public void goToListeBillets();

    public void goToListeVentes();

    public void goToNouveauBillet();

    public void goToVendreBillet();


}
