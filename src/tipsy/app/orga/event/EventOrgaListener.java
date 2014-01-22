package tipsy.app.orga.event;

import tipsy.commun.Event;

/**
 * Created by valoo on 22/01/14.
 */
public interface EventOrgaListener {

    public Event getEvent();

    public void backToOrga();

    public void goToAcces();

    public void goToBilletterie();

    public void goToEditEvent();

    public void home(boolean addTobackStack);
}
