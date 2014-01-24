package com.tipsy.app.orga.event.edit;

import android.view.View;

import com.tipsy.lib.Event_old;

/**
 * Created by valoo on 22/01/14.
 */
public interface EditEventListener {

    public Event_old getEventOld();

    public void backToEventOrga();
    public void backToOrga();

    public void onDescFragCreated(View v);
    public void onLocFragCreated(View v);
    public void onDateFragCreated(View v);
}
