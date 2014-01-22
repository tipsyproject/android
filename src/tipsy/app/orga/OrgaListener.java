package tipsy.app.orga;

import tipsy.app.MenuListener;
import tipsy.commun.Event;
import tipsy.commun.Organisateur;

/**
 * Created by valoo on 22/12/13.
 */
public interface OrgaListener extends MenuListener {

    public void goToNewEvent();
    public void goToEvent(int index);

    public void account();
    public void tableauDeBord(boolean addToBackStack);

}
