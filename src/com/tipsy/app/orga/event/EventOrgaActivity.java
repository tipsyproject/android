package com.tipsy.app.orga.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.app.orga.OrgaActivity;
import com.tipsy.app.orga.acces.AccesActivity;
import com.tipsy.app.orga.billetterie.BilletterieActivity;
import com.tipsy.app.orga.event.edit.EditEventActivity;
import com.tipsy.lib.Event_old;

/**
 * Created by valoo on 22/01/14.
 */
public class EventOrgaActivity extends FragmentActivity implements EventOrgaListener {

    private Event_old eventOld;
    private int index;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);

        TipsyApp app = (TipsyApp) getApplication();
        index = getIntent().getIntExtra("EVENT_INDEX",-1);
        //eventOld = app.getOrga().getEventOlds().get(index);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(eventOld.getNom());

        eventOld.fetch(StackMobOptions.depthOf(1),new StackMobCallback() {
            @Override
            public void success(String s) {
                if(savedInstanceState == null)
                    home(false);
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT","Erreur fetch eventOld / EventOrgaActivity:Oncreate: "+e.getMessage());
            }
        });

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

    public Event_old getEventOld(){
        return eventOld;
    }

    public void home(boolean addTobackStack){
        HomeEventOrgaFragment frag = new HomeEventOrgaFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        if(addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void backToOrga(){
        Intent intent = new Intent(this, OrgaActivity.class);
        startActivity(intent);
    }

    // clique sur le bouton de la Billetterie
    public void goToAcces() {
        Intent intent = new Intent(this, AccesActivity.class);
        intent.putExtra("EVENT_INDEX", index);
        startActivity(intent);
    }

    public void goToBilletterie() {
        Intent intent = new Intent(this, BilletterieActivity.class);
        intent.putExtra("EVENT_INDEX", index);
        startActivity(intent);
    }

    // Modification d'un événement
    public void goToEditEvent() {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("EVENT_INDEX", index);
        startActivity(intent);
    }
}
