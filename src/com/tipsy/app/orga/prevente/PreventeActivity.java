package com.tipsy.app.orga.prevente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeActivity;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;

import java.util.ArrayList;

/**
 * Created by vquefele on 20/01/14.
 */
public class PreventeActivity extends FragmentActivity implements PreventeListener {
    private String EVENT_ID;
    Achat prevente;
    ArrayList<Ticket> billetterie;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_prevente);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Mode Prévente");

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

        if (savedInstanceState == null) {
            EVENT_ID = getIntent().getStringExtra("EVENT_ID");
            prevente = new Achat();
            billetterie = getIntent().getParcelableArrayListExtra("Billetterie");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content, new ScanFragment());
            ft.commit();
        } else {
            prevente = savedInstanceState.getParcelable("Achat");
            EVENT_ID = savedInstanceState.getString("EVENT_ID");
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
        outState.putParcelable("Achat", prevente);
        outState.putString("EVENT_ID", EVENT_ID);
        outState.putParcelableArrayList("Billetterie", billetterie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Confirmation avant de quitter le mode Entrées
            case android.R.id.home:
                backToEntrees(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        final ProgressDialog wait = ProgressDialog.show(this, "", "Scan en cours...", true, true);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        final String tagID = Bracelet.bytesToHex(tag.getId());
        Participant p = new Participant();
        try {
            p.setBracelet(tagID);
        } catch (Exception e) {
        }
        prevente.setParticipant(p);
        wait.dismiss();
        goToTarifs();
    }

    public void goToScan() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new ScanFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToTarifs() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new TarifsFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToParticipant() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new ParticipantFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void backToEntrees(boolean includePrevente) {
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
        Intent intent = new Intent(this, EntreeActivity.class);
        intent.putExtra("EVENT_ID", EVENT_ID);
        if (includePrevente)
            intent.putExtra("PREVENTE", prevente);
        startActivity(intent);
    }

    public Achat getPrevente() {
        return prevente;
    }

    public ArrayList<Ticket> getBilletterie() {
        return billetterie;
    }
}
