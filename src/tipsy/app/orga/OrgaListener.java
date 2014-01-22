package tipsy.app.orga;

import tipsy.app.MenuListener;
import tipsy.commun.Event;

/**
 * Created by valoo on 22/12/13.
 */
public interface OrgaListener extends MenuListener {

    public void goToNewEvent();
    public void goToEvent(Event e);

    public void account();
    public void events();
    public void tableauDeBord(boolean addToBackStack);

}
