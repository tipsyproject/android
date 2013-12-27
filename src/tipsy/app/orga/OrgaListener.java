package tipsy.app.orga;

import tipsy.commun.Billetterie;
import tipsy.commun.Event;

/**
 * Created by valoo on 22/12/13.
 */
public interface OrgaListener {

    public void onBilletterieEdit(Billetterie b);
    public void onClickResumeEvent(Event e);
    public void onEventEdit(Event e);
    public void onEventEdited();
}
