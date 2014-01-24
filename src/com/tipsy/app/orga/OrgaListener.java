package com.tipsy.app.orga;

import com.tipsy.app.MenuListener;
import com.tipsy.lib.Event;

import java.util.ArrayList;

/**
 * Created by valoo on 22/12/13.
 */
public interface OrgaListener extends MenuListener {

    public ArrayList<Event> getEvents();

    public void goToNewEvent();
    public void goToEvent(int index);

    public void account();
    public void tableauDeBord(boolean addToBackStack);

}
