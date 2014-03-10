package com.tipsy.app.orga.event;

import com.tipsy.lib.Event;

/**
 * Created by valoo on 22/01/14.
 */
public interface EventOrgaListener {

    public void init(Event e);

    public Event getEvent();

    public void goToAcces();

    public void goToBar();

    public void goToVestiaire();

    public void goToBilletterie();

    public void goToEditEvent();

    public void backToOrga();

    public void home(boolean addTobackStack);
}
