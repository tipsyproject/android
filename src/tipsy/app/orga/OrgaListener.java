package tipsy.app.orga;

import tipsy.app.MenuListener;
import tipsy.commun.Event;

/**
 * Created by valoo on 22/12/13.
 */
public interface OrgaListener extends MenuListener {
    public void goToBilletterie(Event e);

    public void goToResumeEvent(Event e);

    public void onEventEdit(Event e, boolean create);

    public void onEventEdited();

    public void goToTableauDeBord(boolean addToBackStack);

    public void goToAccount();

    public void goToEvents();

    public void goToAccess(Event e);
}
