package com.tipsy.app.membre.wallet;

import com.tipsy.lib.commerce.Commande;
import com.tipsy.lib.commerce.Panier;
import com.tipsy.lib.commerce.Transaction;
import com.tipsy.lib.commerce.Wallet;

/**
 * Created by valoo on 04/01/14.
 */
public interface WalletListener {

    public Wallet getWallet();

    public void goToResume(boolean addToBackStack);

    public void goToFormule();

    public void goToCredit();

    public void goToDetailsTransaction(Transaction t);

    public void goToCommande(boolean addToBackStack, Panier p, Commande c);

    public void goToHomeMembre();
}
