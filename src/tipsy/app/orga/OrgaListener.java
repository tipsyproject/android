package tipsy.app.orga;

import tipsy.app.MenuListener;
import tipsy.commun.Event;

/**
 * Created by valoo on 22/12/13.
 */
public interface OrgaListener extends MenuListener {
    public void onBilletterieEdit(Event e);

    public void onClickResumeEvent(Event e);

    public void onEventEdit(Event e);

    public void onEventEdited();

    public void goToTableauDeBord();

    public void goToAccount();

    public void goToEvents();
}
