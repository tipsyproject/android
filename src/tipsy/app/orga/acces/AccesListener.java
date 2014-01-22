package tipsy.app.orga.acces;

import java.util.ArrayList;

import tipsy.commun.Event;
import tipsy.commun.billetterie.EntreeArrayAdapter;
import tipsy.commun.commerce.Achat;

/**
 * Created by vquefele on 20/01/14.
 */
public interface AccesListener {
    public Event getEvent();
    public ArrayList<Achat> getEntrees();

    public void backToEventOrga();

    public void goToHome(boolean addTobackStack);
    public void goToManualAccess();

    public void refresh(EntreeArrayAdapter adapter);
}
