package com.tipsy.app.orga.entree.vente;

import com.tipsy.app.orga.entree.EntreeListener;
import com.tipsy.lib.Achat;

/**
 * Created by vquefele on 20/01/14.
 */
public interface ModeVenteListener extends EntreeListener{

    public Achat getPrevente();

    public void stepWait();
    public void stepTarifs();
    public void stepParticipant();

    public void finishPrevente();

}
