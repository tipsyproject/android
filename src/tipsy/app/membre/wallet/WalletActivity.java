package tipsy.app.membre.wallet;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.commerce.Commande;
import tipsy.commun.commerce.Transaction;
import tipsy.commun.commerce.Wallet;

/**
 * Created by tech on 05/12/13.
 */
public class WalletActivity extends FragmentActivity implements WalletListener{

    private TipsyApp app;
    private Wallet wallet;
    public final static String ACTION = "action";
    public final static int COMMANDE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_wallet);
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        app = (TipsyApp) getApplication();
        wallet = app.getWallet();

        Bundle bundle = getIntent().getExtras();
        switch(getIntent().getIntExtra(ACTION,-1)){
            case COMMANDE:
                goToCommande(false);
                break;
            default:
                goToResume(false);
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
        ft.replace(R.id.content, new WalletResumeFragment(wallet));
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    /* Créditer le Wallet */
    public void goToCredit(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new WalletCreditFragment(wallet));
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    /* Détails sur une transaction */
    public void goToCommande(boolean addToBackStack){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new WalletCommandeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    /* Détails sur une transaction */
    public void goToDetailsTransaction(Transaction t){
        WalletDetailsTransactionFragment frag = WalletDetailsTransactionFragment.init(t);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
}
