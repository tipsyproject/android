package com.tipsy.app.orga.event;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.tipsy.app.R;
import com.tipsy.app.orga.OrgaActivity;
import com.tipsy.app.orga.entree.EntreeActivity;
import com.tipsy.app.orga.billetterie.BilletterieActivity;
import com.tipsy.app.orga.event.edit.EditEventActivity;
import com.tipsy.lib.Event;

/**
 * Created by valoo on 22/01/14.
 */
public class EventOrgaActivity extends FragmentActivity implements EventOrgaListener {

    private Event event;
    private ProgressDialog initDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        //overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        InitEventFragment initEventFragment = (InitEventFragment) fm.findFragmentByTag("init");

        if(initEventFragment == null){
            initDialog = ProgressDialog.show(this, null, "Chargement...", true, true);
            initDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    getSupportFragmentManager().popBackStack();
                    backToOrga();
                }
            });
            initEventFragment = new InitEventFragment();
            Bundle args = new Bundle();
            args.putString("EVENT_ID",getIntent().getStringExtra("EVENT_ID"));
            initEventFragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(initEventFragment,"init");
            ft.commit();
        }else
            event = savedInstanceState.getParcelable("Event");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelable("Event", event);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToOrga();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init(Event e) {
        event = e;
        getActionBar().setTitle(event.getNom());
        if(initDialog != null)
            initDialog.dismiss();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new TDBEventFragment());
        ft.commit();
    }

    public Event getEvent() {
        return event;
    }

    public void home(boolean addTobackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new TDBEventFragment());
        if (addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    // clique sur le bouton de la Billetterie
    public void goToAcces() {
        Intent intent = new Intent(this, EntreeActivity.class);
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

    public void backToOrga(){
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
        Intent intent = new Intent(this, OrgaActivity.class);
        startActivity(intent);
    }
}
