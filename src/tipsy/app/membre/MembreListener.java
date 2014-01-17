package tipsy.app.membre;

import tipsy.app.MenuListener;
import tipsy.commun.Event;
import tipsy.commun.Membre;
import tipsy.commun.billetterie.Billet;
import tipsy.commun.billetterie.Billetterie;

/**
 * Created by Valentin on 29/12/13.
 */
public interface MembreListener extends MenuListener {
    public Membre getMembre();

    public void goToTableauDeBord(boolean addToBackStack);

    public void goToAccount();

    public void goToWallet();

    public void goToEvents();

    public void searchEventByKeyword(String query);

    public void goToEvent(Event e);

    public void goToEventBillets(Event e);

    public void goToCommande(Event e);

}
