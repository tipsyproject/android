package com.tipsy.app.membre.event;

import com.tipsy.lib.util.Commande;
import com.tipsy.lib.util.EventModule;
import com.tipsy.lib.util.Panier;

/**
 * Created by valoo on 22/01/14.
 */
public interface EventMembreListener extends EventModule {

    public void goToEventBillets();

    public void goToParticiper(Commande c);

    public void goToCommande(Commande c);
}
