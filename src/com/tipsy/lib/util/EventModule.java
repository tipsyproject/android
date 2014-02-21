package com.tipsy.lib.util;

import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;

import java.util.ArrayList;

/**
 * Created by valoo on 27/01/14.
 */
public interface EventModule {
    public Event getEvent();

    public ArrayList<Ticket> getBilletterie();

    public void loadEventBilletterie(String eventId, QueryCallback callback);
}
