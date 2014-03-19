package com.tipsy.app.orga.bar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.app.R;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;
import com.tipsy.lib.util.Item;
import com.tipsy.lib.util.Panier;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tech on 10/03/14.
 */
public class BarActivity extends FragmentActivity implements BarListener {

    protected Panier panier = new Panier();
    protected ArrayList<Ticket> conso = new ArrayList<Ticket>();
    protected BarPanierFragment fragPanier;
    protected BarQuantiteFragment fragQuantite;
    protected LinearLayout layoutQuantite;
    protected BarNFCFragment fragNFC;

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
        panier = new Panier(new ArrayList<Item>());
        fragQuantite = (BarQuantiteFragment) getSupportFragmentManager().findFragmentById(R.id.frag_quantite);
        layoutQuantite = (LinearLayout) findViewById(R.id.layout_quantite);
        fragPanier = (BarPanierFragment) getSupportFragmentManager().findFragmentById(R.id.frag_panier);
        //fragNFC = (BarNFCFragment) getSupportFragmentManager().findFragmentById(R.id.frag_panier);

        if (savedInstanceState == null) {
            final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement...", true, true);
            InitBarFragment initBarFragment = new InitBarFragment();
            Bundle args = new Bundle();
            args.putString("EVENT_ID", getIntent().getStringExtra("EVENT_ID"));
            initBarFragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(initBarFragment, "init");
            ft.commit();
            wait.dismiss();
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

    @Override
    protected void onNewIntent(Intent intent) {

        final ProgressDialog wait = ProgressDialog.show(this, "", "Vérification en cours...", true, true);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        final String tagID = Bracelet.bytesToHex(tag.getId());

        Iterator it = conso.iterator();
        Achat newConso = null;
        boolean found = false;
        while (it.hasNext() && !found) {
            newConso = (Achat) it.next();
            if (newConso.getParticipant() != null && newConso.getParticipant().getBracelet() != null)
                found = newConso.getParticipant().getBracelet().equals(tagID);
        }
        String message;
        if (found) {
            newConso.getParticipant();
        }
        wait.dismiss();
    }

    public void goToQuantity(Ticket ticket) {
        int index = -1;
        // Init item à quantité = 1
        Item item = new Item(ticket, 1);
        /* On récupère l'index de l'item dans le panier s'il y est présent (sinon -1) */
        index = panier.indexOf(item);

        /* Si le panier ne contient pas l'item (index = -1)
         * passage de l'item nouvellement créé et initialisé avec une quantité de 1
         */
        if (index == -1) {
            fragQuantite.setItem(item);
        } else { // Sinon passage de l'item du panier
            fragQuantite.setItem(panier.get(index));
        }
        layoutQuantite.setVisibility(View.VISIBLE);
    }

    public void addItemToPanier(Item item) {
        /* Si le panier ne contient pas déjà l'item,
         * on ajoute l'item au panier
         */
        if (!panier.contains(item))
            panier.add(item);
        /* Mise à jour du PanierAdapter */
        ((ArrayAdapter<Item>) fragPanier.getListAdapter()).notifyDataSetChanged();
        layoutQuantite.setVisibility(View.GONE);
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
