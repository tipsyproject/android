package tipsy.app.membre.event;

import tipsy.commun.Event;
import tipsy.commun.commerce.Commande;
import tipsy.commun.commerce.Panier;

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
