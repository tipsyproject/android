package com.tipsy.app.orga.entree;

import com.tipsy.lib.Achat;
import com.tipsy.lib.util.EventModule;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;

/**
 * Created by vquefele on 20/01/14.
 */
public interface EntreeListener {


    /*
    public void init();

    public void updateEntrees(QueryCallback cb);


    public void backToEvent();
    */
    public ArrayList<Achat> getEntrees();
    public void setCurrentEntree(Achat entree);

    public void modeStats();
    public void modeQRCode();
    public void modeListe();
    public void modeVente();
    public void modeNFC();
    public void backToEvent();
    public void setNFCMode(int mode);


    public void updateProgress();

}
