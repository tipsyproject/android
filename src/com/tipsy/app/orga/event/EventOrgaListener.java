package com.tipsy.app.orga.event;

import com.tipsy.lib.Event;

/**
 * Created by valoo on 22/01/14.
 */
public interface EventOrgaListener {

    public Event getEvent();

    public void goToAcces();

    public void goToBilletterie();

    public void goToEditEvent();

    public void home(boolean addTobackStack);
}
