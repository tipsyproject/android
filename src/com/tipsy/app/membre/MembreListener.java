package com.tipsy.app.membre;

import com.tipsy.app.MenuListener;
import com.tipsy.lib.Event_old;
import com.tipsy.lib.Membre;

/**
 * Created by Valentin on 29/12/13.
 */
public interface MembreListener extends MenuListener {
    public Membre getMembre();

    public void goToTableauDeBord(boolean addToBackStack);

    public void goToAccount();

    public void goToBracelet();

    public void goToWallet();

    public void goToEvents();

    public void searchEventByKeyword(String query);

    public void goToEvent(Event_old e);

}
