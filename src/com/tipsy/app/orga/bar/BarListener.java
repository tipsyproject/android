package com.tipsy.app.orga.bar;

import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.EventModule;
import com.tipsy.lib.util.Item;
import com.tipsy.lib.util.Panier;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;

/**
 * Created by tech on 11/03/14.
 */
public interface BarListener extends EventModule {

    public Panier getPanier();

    public ArrayList<Ticket> getConso();

    public void goToQuantity(Ticket ticket);

    public void loadEventConso(String eventId, final QueryCallback callback_e);

    public void addItemToPanier(Item item);

    public void onRemove(int position);
}
