package tipsy.app.access;

import tipsy.commun.Event;

/**
 * Created by vquefele on 20/01/14.
 */
public interface AccessListener {

    public void goToHome(boolean addTobackStack);
    public void refresh(Event e);
    public void goToManualAccess(Event e);
    public Event getEvent();
}
