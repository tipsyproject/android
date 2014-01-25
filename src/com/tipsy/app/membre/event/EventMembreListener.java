package com.tipsy.app.membre.event;

import com.tipsy.lib.Event;
import com.tipsy.lib.Commande;
import com.tipsy.lib.Panier;

/**
 * Created by valoo on 22/01/14.
 */
public interface EventMembreListener {

    public Event getEvent();

    public void backToHome();

    public void goToEventBillets();

    public void goToParticiper(Panier p);

    public void goToCommande(Panier p, Commande c);
}
