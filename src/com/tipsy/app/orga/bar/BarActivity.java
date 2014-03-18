package com.tipsy.app.orga.bar;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Item;
import com.tipsy.lib.util.Panier;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tech on 10/03/14.
 */
public class BarActivity extends FragmentActivity implements BarListener {

    private Panier panier = new Panier();
    private ArrayList<Ticket> conso = new ArrayList<Ticket>();
    private BarPanierFragment fragPanier;
    private BarQuantiteFragment fragQuantite;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_bar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Bar");

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // Get the content view
            View contentView = findViewById(android.R.id.content);

            // Make sure it's a valid instance of a FrameLayout
            if (contentView instanceof FrameLayout) {
                TypedValue tv = new TypedValue();

                // Get the windowContentOverlay value of the current theme
                if (getTheme().resolveAttribute(
                        android.R.attr.windowContentOverlay, tv, true)) {

                    // If it's a valid resource, set it as the foreground drawable
                    // for the content view
                    if (tv.resourceId != 0) {
                        ((FrameLayout) contentView).setForeground(
                                getResources().getDrawable(tv.resourceId));
                    }
                }
            }
        }
        if (savedInstanceState == null) {
            final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement...", true, true);
            loadEventConso(getIntent().getStringExtra("EVENT_ID"), new QueryCallback() {
                @Override
                public void done(Exception e) {
                    if (e == null) {
                        wait.dismiss();

                    } else {
                        Toast.makeText(BarActivity.this, getString(R.string.erreur_interne), Toast.LENGTH_SHORT).show();
                        Log.d(TipsyApp.TAG,e.getMessage());
                        wait.dismiss();
                    }
                }
            });
        }

    }

    public void findConso(Event ev, FindCallback cb) {
        ParseQuery<Ticket> query = ParseQuery.getQuery(Ticket.class);
        query.include("event");
        query.whereEqualTo("event", ev);
        query.whereEqualTo("type", Ticket.CONSO);
        query.findInBackground(cb);
    }

    public void loadEventConso(String eventId, final QueryCallback callback_e) {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(eventId, new GetCallback<Event>() {
            @Override
            public void done(Event ev, ParseException e) {
                if (ev != null) {
                    findConso(ev, new FindCallback<Ticket>() {
                        @Override
                        public void done(List<Ticket> consos, ParseException e) {
                            if (e == null) {
                                getConso().clear();
                                getConso().addAll(consos);
                                BarConsoFragment.gridAdapter.notifyDataSetChanged();
                            }
                            callback_e.done(e);
                        }
                    });
                } else {
                    callback_e.done(e);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
    }

    public void goToQuantity(Ticket ticket) {
        int index = -1;
        // Init item à quantité = 1
        Item item = new Item(ticket,1);
        /* On récupère l'index de l'item dans le panier s'il y est présent (sinon -1) */
        index = panier.indexOf(item);

        /* Si le panier ne contient pas l'item (index = -1)
         * passage de l'item nouvellement créé et initialisé avec une quantité de 1
         */
        if(index == -1){
            fragQuantite.setItem(item);
        }else{ // Sinon passage de l'item du panier
            fragQuantite.setItem(panier.get(index));
        }
        fragQuantite.getView().setVisibility(View.VISIBLE);
    }

    public void addItemToPanier(Item item){
        /* Si le panier ne contient pas déjà l'item,
         * on ajoute l'item au panier
         */
        if(!panier.contains(item))
            panier.add(item);
        /* Mise à jour du PanierAdapter */
        ((ArrayAdapter<Item>)fragPanier.getListAdapter()).notifyDataSetChanged();
        fragQuantite.getView().setVisibility(View.GONE);
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    @Override
    public Event getEvent() {
        return null;
    }

    @Override
    public ArrayList<Ticket> getBilletterie() {
        return null;
    }

    @Override
    public ArrayList<Ticket> getConso() {
        return conso;
    }

    @Override
    public void loadEventBilletterie(String eventId, QueryCallback callback) {

    }
}
