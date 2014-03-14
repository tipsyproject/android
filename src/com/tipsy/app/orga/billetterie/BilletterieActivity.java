package com.tipsy.app.orga.billetterie;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeArrayAdapter;
import com.tipsy.app.orga.prevente.TarifsFragment;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.EventActivity;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valoo on 27/12/13.
 */
public class BilletterieActivity extends EventActivity implements BilletterieListener {

    private ArrayList<Achat> ventes = new ArrayList<Achat>();
    EntreeArrayAdapter ventesAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_billetterie);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Billetterie");

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


        ventesAdapter = new EntreeArrayAdapter(BilletterieActivity.this, ventes);

        /* On récupère l'event, la billetterie et les billets vendus */
        if (savedInstanceState == null) {
            final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement...", true, true);
            loadEventBilletterie(getIntent().getStringExtra("EVENT_ID"), new QueryCallback() {
                @Override
                public void done(Exception e) {
                    if (e == null)
                        Ticket.loadVentes(getBilletterie(), new FindCallback<Achat>() {
                            @Override
                            public void done(List<Achat> achats, ParseException e) {
                                wait.dismiss();
                                /* Affichage accueil Billetterie */
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                ft.add(R.id.content, new HomeBilletterieFragment());
                                ft.commit();
                            }
                        });
                    else {
                        Toast.makeText(BilletterieActivity.this, getString(R.string.erreur_interne), Toast.LENGTH_SHORT).show();
                        wait.dismiss();
                    }
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
        // handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                    overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
      IMPLEMENTATION DES FONCTIONS DE l'INTERFACE BilletterieListener
      */

    /* GETTERS */

    public ArrayList<Achat> getVentes() {
        return ventes;
    }

    public EntreeArrayAdapter getVentesAdapter() {
        return ventesAdapter;
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
        ft.commit();
    }

    public void goToVendreBillet() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new TarifsFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}
