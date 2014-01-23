package tipsy.app.orga.billetterie;

import tipsy.commun.Event;

/**
 * Created by valoo on 27/12/13.
 */
public interface BilletterieListener {

    public void showListeBillets(boolean addToStackBack);

    public void showListeVentes();

    public void backToEventOrga();

    public Event getEvent();


}
