package com.tipsy.app.membre.wallet;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.tipsy.app.R;
import com.tipsy.lib.util.Commande;
import com.tipsy.lib.util.Transaction;

/**
 * Created by tech on 05/12/13.
 */
public class WalletActivity extends FragmentActivity implements WalletListener {

    private Commande commande = new Commande();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelable("Commande", commande);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_wallet);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Tipsy Wallet");

        if (savedInstanceState == null) {
            if (getIntent().hasExtra("Commande")) {
                commande = getIntent().getParcelableExtra("Commande");
                goToCommande(false);
            } else goToResume(false);
        } else {
            commande = savedInstanceState.getParcelable("Commande");
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

    public Commande getCommande() {
        return commande;
    }


    /* Résumé du Wallet */
    public void goToResume(boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new WalletResumeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    /* Créditer le Wallet */
    public void goToFormule() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new WalletFormuleFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    /* Créditer le Wallet */
    public void goToCredit() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new WalletCreditFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    /* Paiement d'une commande avec le Tipsy Wallet */
    public void goToCommande(boolean addToBackStack) {
        WalletCommandeFragment frag = new WalletCommandeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    /* Détails sur une transaction */
    public void goToDetailsTransaction(Transaction t) {
        WalletDetailsTransactionFragment frag = WalletDetailsTransactionFragment.init(t);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
}
