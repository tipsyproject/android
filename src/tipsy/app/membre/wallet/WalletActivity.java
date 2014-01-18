package tipsy.app.membre.wallet;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.membre.MembreActivity;
import tipsy.commun.Event;
import tipsy.commun.commerce.Transaction;

/**
 * Created by tech on 05/12/13.
 */
public class WalletActivity extends FragmentActivity implements WalletListener {

    private TipsyApp app;
    public final static String ACTION = "action";
    public final static int COMMANDE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wallet);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            switch (getIntent().getIntExtra(ACTION, -1)) {
                case COMMANDE:
                    Event event = getIntent().getParcelableExtra("Event");
                    goToCommande(false,event);
                    break;
                default:
                    goToResume(false);
            }
        }
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
    public void goToCredit() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new WalletCreditFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    /* Détails sur une transaction */
    public void goToCommande(boolean addToBackStack,Event e) {
        WalletCommandeFragment frag = WalletCommandeFragment.init(e);
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
