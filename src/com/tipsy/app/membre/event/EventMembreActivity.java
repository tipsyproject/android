package com.tipsy.app.membre.event;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

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
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            event = getIntent().getParcelableExtra("Event");
            event.fetch(StackMobOptions.depthOf(1), new StackMobModelCallback() {
                @Override
                public void success() {
                    goToEventBillets();
                }

                @Override
                public void failure(StackMobException e) {
                }
            });
        }
        else event = savedInstanceState.getParcelable("Event");

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(event.getNom());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(outState==null)
            outState = new Bundle();
        outState.putParcelable("Event", event);
        // Add variable to outState here
        super.onSaveInstanceState(outState);
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
        EventBilletsFragment frag = new EventBilletsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, frag)
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
