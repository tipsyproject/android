package com.tipsy.app.membre.wallet;

import com.tipsy.lib.util.Commande;
import com.tipsy.lib.util.Transaction;

/**
 * Created by valoo on 04/01/14.
 */
public interface WalletListener {

    public Commande getCommande();

    public void goToResume(boolean addToBackStack);

    public void goToFormule();

    public void goToCredit();

    public void goToDetailsTransaction(Transaction t);

    public void goToCommande(boolean addToBackStack);
}
