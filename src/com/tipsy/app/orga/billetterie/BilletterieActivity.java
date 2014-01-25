package com.tipsy.app.orga.billetterie;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import com.tipsy.app.R;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Event;
import com.tipsy.lib.billetterie.Billet;
import com.tipsy.lib.billetterie.Billetterie;
import com.tipsy.lib.commerce.Produit;

import java.util.List;

/**
 * Created by valoo on 27/12/13.
 */
public class BilletterieActivity extends FragmentActivity implements BilletterieListener {

    private Billetterie billetterie = new Billetterie();
    private Event event;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Billetterie");

        final ProgressDialog wait = ProgressDialog.show(this,null,"Chargement...",true,true);
        wait.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                backToEventOrga();
            }
        });
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(getIntent().getStringExtra("EVENT_ID"), new GetCallback<Event>() {
            @Override
            public void done(Event ev, ParseException e) {
                if (ev != null) {
                    event = ev;
                    event.findBilletterie(new FindCallback<Billet>() {
                        @Override
                        public void done(List<Billet> billets, ParseException e) {
                            if(e==null){
                                billetterie.clear();
                                billetterie.addAll(billets);
                                wait.dismiss();
                                if(savedInstanceState == null)
                                    showListeBillets(false);
                            }else
                                Toast.makeText(BilletterieActivity.this,getString(R.string.erreur_interne),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    wait.dismiss();
                    Log.d("TOUTAFAIT", "Erreur fetch event/ EventOrgaActivity:Oncreate: " + e.getMessage());
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                backToEventOrga();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void backToEventOrga(){
        Intent intent = new Intent(this, EventOrgaActivity.class);
        intent.putExtra("EVENT_ID", event.getObjectId());
        startActivity(intent);
    }

    // IMPLEMENTATION DES FONCTIONS DE l'INTERFACE BilletterieListener

    public void showListeBillets(boolean addTobackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new BilletsCreesFragment());
        if (addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void showListeVentes(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new BilletsVendusFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public Billetterie getBilletterie(){
        return billetterie;
    }
    public Event getEvent(){
        return event;
    }
}
