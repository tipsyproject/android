package com.tipsy.app.orga.bar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;
import com.tipsy.lib.util.Commande;
import com.tipsy.lib.util.Commerce;
import com.tipsy.lib.util.Item;
import com.tipsy.lib.util.Panier;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tech on 10/03/14.
 */
public class BarActivity extends FragmentActivity implements BarListener {

    private ArrayList<Participant> entrees = new ArrayList<Participant>();
    protected Panier panier = new Panier();
    protected ArrayList<Ticket> conso = new ArrayList<Ticket>();
    protected BarPanierFragment fragPanier;
    protected BarQuantiteFragment fragQuantite;
    protected BarNFCFragment fragNFC;
    protected TextView prixTotal;
    protected Integer prixInt = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_bar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragQuantite);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        fragPanier = (BarPanierFragment) getSupportFragmentManager().findFragmentById(R.id.frag_panier);
        prixTotal = (TextView) findViewById(R.id.prix_total);
        FragmentManager fm = getSupportFragmentManager();
        InitBarFragment initEntreeFragment = (InitBarFragment) fm.findFragmentByTag("init");
        if (savedInstanceState == null && initEntreeFragment == null) {
            final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement...", true, true);
            InitBarFragment initBarFragment = new InitBarFragment();
            Bundle args = new Bundle();
            args.putString("EVENT_ID", getIntent().getStringExtra("EVENT_ID"));
            initBarFragment.setArguments(args);
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.add(initBarFragment, "init");
            ft2.commit();
            wait.dismiss();
        } else {
            entrees = savedInstanceState.getParcelableArrayList("Entrees");
            conso = savedInstanceState.getParcelableArrayList("Conso");
            panier = savedInstanceState.getParcelable("Panier");
            prixTotal.setText(Commerce.prixToString(panier.getPrixTotal()));
        }

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelableArrayList("Entrees", entrees);
        outState.putParcelableArrayList("Conso", conso);
        outState.putParcelable("Panier", panier);
        super.onSaveInstanceState(outState);
    }

    public void onClickOk(View view){
        if (!fragPanier.getListAdapter().isEmpty()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragNFC = new BarNFCFragment();
            ft.addToBackStack(null);
            ft.add(R.id.layout, fragNFC);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
        else
            Toast.makeText(BarActivity.this, "Commande vide", Toast.LENGTH_SHORT).show();
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
        Log.d(TipsyApp.TAG, tagID);

        Ticket.loadParticipants(tagID, new FindCallback<Participant>() {
            @Override
            public void done(List<Participant> participants, ParseException e) {
                if (e == null && participants.size()>0) {
                    Commande commande = new Commande();
                    for (Item item : panier)
                        for (int i = 0; i < item.getQuantite(); ++i) {
                            Achat achat = new Achat(item.getTicket());
                            achat.setParticipant(participants.get(0));
                            achat.setUsed(true);
                            commande.add(achat);
                        }
                    Achat.saveAllInBackground(((ArrayList) commande), new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d(TipsyApp.TAG, "ok");
                                panier.clear();
                                ((ArrayAdapter<Item>) fragPanier.getListAdapter()).notifyDataSetChanged();
                                prixTotal.setText(Commerce.prixToString(panier.getPrixTotal()));
                                Toast.makeText(BarActivity.this, "Paiement effectué", Toast.LENGTH_SHORT).show();
                            } else {
                                done(e);
                                Log.d(TipsyApp.TAG, e.getMessage());
                                Toast.makeText(BarActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    getSupportFragmentManager().popBackStack();
                    ft.remove(fragNFC);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                } else {
                    Toast.makeText(BarActivity.this, "Participant inexistant", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.show(fragQuantite);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        if (index == -1) {
            fragQuantite.setItem(item);
        } else { // Sinon passage de l'item du panier
            fragQuantite.setItem(panier.get(index));
        }
    }

    public void addItemToPanier(Item item) {
        /* Si le panier ne contient pas déjà l'item,
         * on ajoute l'item au panier
         */
        if (!panier.contains(item))
            panier.add(item);
        /* Mise à jour du PanierAdapter */
        ((ArrayAdapter<Item>) fragPanier.getListAdapter()).notifyDataSetChanged();
        prixTotal.setText(Commerce.prixToString(panier.getPrixTotal()));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack();
        ft.hide(fragQuantite);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void onRemove(int position){
        ArrayAdapter<Item> arrayPanier = (ArrayAdapter<Item>) fragPanier.getListAdapter();
        arrayPanier.remove(arrayPanier.getItem(position));
        arrayPanier.notifyDataSetChanged();
        prixTotal.setText(Commerce.prixToString(panier.getPrixTotal()));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack();
        ft.hide(fragQuantite);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
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
