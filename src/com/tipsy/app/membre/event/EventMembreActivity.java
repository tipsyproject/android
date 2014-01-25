package com.tipsy.app.membre.event;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import com.tipsy.app.R;
import com.tipsy.app.membre.MembreActivity;
import com.tipsy.app.membre.wallet.WalletActivity;
import com.tipsy.lib.Event;
import com.tipsy.lib.commerce.Commande;
import com.tipsy.lib.commerce.Panier;

/**
 * Created by valoo on 22/01/14.
 */
public class EventMembreActivity extends FragmentActivity implements EventMembreListener {

    private Event event;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);

        /* Chargement de l'event */
        final ProgressDialog wait = ProgressDialog.show(this,null,"Chargement...",true,true);
        wait.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                backToHome();
            }
        });
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(getIntent().getStringExtra("EVENT_ID"), new GetCallback<Event>() {
            @Override
            public void done(Event ev, ParseException e) {
                if (ev != null) {
                    event = ev;
                    getActionBar().setTitle(event.getNom());
                    wait.dismiss();
                    getActionBar().setTitle(event.getNom());
                    if (savedInstanceState == null) {
                        goToEventBillets();
                    }
                } else {
                    wait.dismiss();
                    Log.d("TOUTAFAIT", "Erreur: " + e.getMessage());
                }
            }
        });
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                backToHome();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Event getEvent(){
        return event;
    }

    public void backToHome(){
        Intent intent = new Intent(this, MembreActivity.class);
        startActivity(intent);
    }

    public void goToEventBillets() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new EventBilletsFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    public void goToParticiper(Panier p) {
        EventParticiperFragment frag = EventParticiperFragment.init(p);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, frag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void goToCommande(Panier p, Commande c) {
        Intent intent = new Intent(this, WalletActivity.class);
        intent.putExtra(WalletActivity.ACTION, WalletActivity.COMMANDE);
        intent.putExtra("Panier", (Parcelable) p);
        intent.putExtra("Commande",(Parcelable) c);
        startActivity(intent);
    }
}
