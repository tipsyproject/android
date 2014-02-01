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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.app.R;
import com.tipsy.app.membre.wallet.WalletActivity;
import com.tipsy.lib.util.Commande;
import com.tipsy.lib.Event;
import com.tipsy.lib.util.EventActivity;
import com.tipsy.lib.util.Panier;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valoo on 22/01/14.
 */
public class EventMembreActivity extends EventActivity implements EventMembreListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_billetterie);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
        /* Chargement de l'event */
            final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement...", true, true);
            wait.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            loadEventBilletterie(getIntent().getStringExtra("EVENT_ID"), new QueryCallback() {
                @Override
                public void done(Exception e) {
                    wait.dismiss();
                    goToEventBillets();
                }
            });
        }

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
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                    overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToEventBillets() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, new EventBilletsFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    public void goToParticiper(Commande c) {
        EventParticiperFragment frag = new EventParticiperFragment();
        Bundle args = new Bundle();
        args.putParcelable("Commande", c);
        frag.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, frag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void goToCommande(Commande c) {
        Intent intent = new Intent(this, WalletActivity.class);
        intent.putExtra("Commande", (Parcelable) c);
        startActivity(intent);
    }
}
