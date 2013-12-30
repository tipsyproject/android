package tipsy.app.membre;

import tipsy.app.MenuListener;
import tipsy.commun.Membre;

/**
 * Created by Valentin on 29/12/13.
 */
public interface MembreListener extends MenuListener {
    public Membre getMembre();
    public void goToTableauDeBord();
    public void goToAccount();
    public void goToSolde();
    public void goToEvents();

}
