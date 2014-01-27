package com.tipsy.app.orga.billetterie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.app.R;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valoo on 27/12/13.
 */
public class BilletterieActivity extends FragmentActivity implements BilletterieListener {

    private ArrayList<Ticket> billetterie = new ArrayList<Ticket>();
    private ArrayList<Achat> ventes = new ArrayList<Achat>();
    private Event event;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_billetterie);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Billetterie");

        /* On récupère l'event, la billetterie et les billets vendus */
        if (savedInstanceState == null) {
            final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement...", true, true);
            ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
            query.getInBackground(getIntent().getStringExtra("EVENT_ID"), new GetCallback<Event>() {
                @Override
                public void done(Event ev, ParseException e) {
                    if (ev != null) {
                        event = ev;
                        event.findBilletterie(new FindCallback<Ticket>() {
                            @Override
                            public void done(List<Ticket> billets, ParseException e) {
                                if (e == null) {
                                    Log.d("TOUTAFAIT", "find ");
                                    billetterie.clear();
                                    billetterie.addAll(billets);
                                    Ticket.loadVentes(billetterie, new FindCallback<Achat>() {
                                        @Override
                                        public void done(List<Achat> achats, ParseException e) {
                                            wait.dismiss();
                                            /* Affichage accueil Billetterie */
                                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                            ft.add(R.id.content, new HomeBilletterieFragment());
                                            ft.commit();
                                        }
                                    });
                                } else
                                    Toast.makeText(BilletterieActivity.this, getString(R.string.erreur_interne), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        wait.dismiss();
                    }
                }
            });
        } else {
            event = savedInstanceState.getParcelable("Event");
            billetterie = savedInstanceState.getParcelableArrayList("Billetterie");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelable("Event", event);
        outState.putParcelableArrayList("Billetterie", billetterie);
        super.onSaveInstanceState(outState);
    }

    /*
      IMPLEMENTATION DES FONCTIONS DE l'INTERFACE BilletterieListener
      */

    /* GETTERS */
    public ArrayList<Ticket> getBilletterie() {
        return billetterie;
    }

    public Event getEvent() {
        return event;
    }

    public ArrayList<Achat> getVentes() {
        return ventes;
    }

    public void backToHome() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new HomeBilletterieFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToEditBillet(int index) {
        EditBilletFragment fragment = new EditBilletFragment();
        Bundle args = new Bundle();
        args.putInt("BILLET_INDEX", index);
        fragment.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToListeBillets() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new ListeBilletsFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToListeVentes() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new ListeVentesFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToNouveauBillet() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new EditBilletFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToVendreBillet() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new VendreBilletFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    /* retour à l'Activité parente */
    public void backToEventOrga() {
        Intent intent = new Intent(this, EventOrgaActivity.class);
        intent.putExtra("EVENT_ID", event.getObjectId());
        startActivity(intent);
    }
}
