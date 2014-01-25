package com.tipsy.app.membre;

import com.tipsy.app.MenuListener;
import com.tipsy.lib.Event;

import java.util.ArrayList;

/**
 * Created by Valentin on 29/12/13.
 */
public interface MembreListener extends MenuListener {

    public ArrayList<Event> getSearchResults();

    public void goToTableauDeBord(boolean addToBackStack);

    public void goToAccount();

    public void goToBracelet();

    public void goToWallet();

    public void goToEvents();

    public void goToSearchResults();

    public void goToEvent(Event e);

}
