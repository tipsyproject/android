package com.tipsy.lib.util;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valoo on 27/01/14.
 */
public abstract class EventActivity extends FragmentActivity implements EventModule {

    protected ArrayList<Ticket> billetterie = new ArrayList<Ticket>();
    protected Event event;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* On récupère l'event, la billetterie */
        if (savedInstanceState != null) {
            event = savedInstanceState.getParcelable("Event");
            billetterie = savedInstanceState.getParcelableArrayList("Billetterie");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelable("Event", event);
        outState.putParcelableArrayList("Billetterie", billetterie);
        super.onSaveInstanceState(outState);
    }

    public void loadEventBilletterie(String eventId, final QueryCallback callback) {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(eventId, new GetCallback<Event>() {
            @Override
            public void done(Event ev, ParseException e) {
                if (ev != null) {
                    event = ev;
                    event.findBilletterie(new FindCallback<Ticket>() {
                        @Override
                        public void done(List<Ticket> billets, ParseException e) {
                            if (e == null) {
                                billetterie.clear();
                                billetterie.addAll(billets);
                            }
                            callback.done(e);
                        }
                    });
                } else {
                    callback.done(e);
                }
            }
        });
    }

    public ArrayList<Ticket> getBilletterie() {
        return billetterie;
    }

    public Event getEvent() {
        return event;
    }

}
