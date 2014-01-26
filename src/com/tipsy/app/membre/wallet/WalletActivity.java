package com.tipsy.app.membre.wallet;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.tipsy.app.R;
import com.tipsy.app.membre.MembreActivity;
import com.tipsy.lib.TipsyUser;
import com.tipsy.lib.Commande;
import com.tipsy.lib.Panier;
import com.tipsy.lib.Transaction;
import com.tipsy.lib.Wallet;

/**
 * Created by tech on 05/12/13.
 */
public class WalletActivity extends FragmentActivity implements WalletListener {

    private Wallet wallet;
    public final static String ACTION = "action";
    public final static int COMMANDE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_wallet);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Tipsy Wallet");

        wallet = new Wallet(TipsyUser.getCurrentUser());
        /* Chargement du Tipsy Wallet */
        final ProgressDialog wait = ProgressDialog.show(this,null,"Chargement du Tipsy Wallet...",true,false);
        wallet.load();
        wait.dismiss();

        if (savedInstanceState == null) {
            switch (getIntent().getIntExtra(ACTION, -1)) {
                case COMMANDE:
                    Panier panier = getIntent().getParcelableExtra("Panier");
                    Commande commande = getIntent().getParcelableExtra("Commande");
                    goToCommande(false,panier,commande);
                    break;
                default:
                    goToResume(false);
            }
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
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Wallet getWallet(){
        return wallet;
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

    /* Détails sur une transaction */
    public void goToCommande(boolean addToBackStack, Panier p, Commande c) {
        WalletCommandeFragment frag = WalletCommandeFragment.init(p,c);
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

    public void goToHomeMembre(){
        Intent intent = new Intent(this, MembreActivity.class);
        startActivity(intent);
    }
}
