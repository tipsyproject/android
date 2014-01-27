package com.tipsy.app.orga.event;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.app.R;
import com.tipsy.app.orga.OrgaActivity;
import com.tipsy.app.orga.acces.AccesActivity;
import com.tipsy.app.orga.billetterie.BilletterieActivity;
import com.tipsy.app.orga.event.edit.EditEventActivity;
import com.tipsy.lib.Event;

/**
 * Created by valoo on 22/01/14.
 */
public class EventOrgaActivity extends FragmentActivity implements EventOrgaListener {

    private Event event;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        /* Chargement de l'event */
        final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement...", true, true);
        wait.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                backToOrga();
            }
        });
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(getIntent().getStringExtra("EVENT_ID"), new GetCallback<Event>() {
            @Override
            public void done(Event ev, ParseException e) {
                if (ev != null) {
                    event = ev;
                    wait.dismiss();
                    getActionBar().setTitle(event.getNom());
                    if (savedInstanceState == null) {
                        home(false);
                    }
                } else {
                    wait.dismiss();
                    Log.d("TOUTAFAIT", "Erreur fetch event/ EventOrgaActivity:Oncreate: " + e.getMessage());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                backToOrga();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Event getEvent() {
        return event;
    }

    public void home(boolean addTobackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new HomeEventOrgaFragment());
        if (addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void backToOrga() {
        Intent intent = new Intent(this, OrgaActivity.class);
        startActivity(intent);
    }

    // clique sur le bouton de la Billetterie
    public void goToAcces() {
        Intent intent = new Intent(this, AccesActivity.class);
        intent.putExtra("EVENT_ID", event.getObjectId());
        startActivity(intent);
    }

    public void goToBilletterie() {
        Intent intent = new Intent(this, BilletterieActivity.class);
        intent.putExtra("EVENT_ID", event.getObjectId());
        startActivity(intent);
    }

    // Modification d'un événement
    public void goToEditEvent() {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("EVENT_ID", event.getObjectId());
        startActivity(intent);
    }
}
