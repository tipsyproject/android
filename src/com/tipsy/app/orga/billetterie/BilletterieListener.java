package com.tipsy.app.orga.billetterie;

import com.tipsy.lib.Event_old;

/**
 * Created by valoo on 27/12/13.
 */
public interface BilletterieListener {

    public void showListeBillets(boolean addToStackBack);

    public void showListeVentes();

    public void backToEventOrga();

    public Event_old getEventOld();


}
