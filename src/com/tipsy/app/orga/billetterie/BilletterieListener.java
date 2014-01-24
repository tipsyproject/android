package com.tipsy.app.orga.billetterie;

import com.tipsy.lib.Event;
import com.tipsy.lib.billetterie.Billetterie;

/**
 * Created by valoo on 27/12/13.
 */
public interface BilletterieListener {

    public void showListeBillets(boolean addToStackBack);

    public void showListeVentes();

    public void backToEventOrga();

    public Event getEvent();

    public Billetterie getBilletterie();


}
