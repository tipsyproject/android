package tipsy.app.orga.event.edit;

import android.view.View;

import tipsy.commun.Event;

/**
 * Created by valoo on 22/01/14.
 */
public interface EditEventListener {

    public Event getEvent();

    public void backToEventOrga();
    public void backToOrga();

    public void onDescFragCreated(View v);
    public void onLocFragCreated(View v);
    public void onDateFragCreated(View v);
}
