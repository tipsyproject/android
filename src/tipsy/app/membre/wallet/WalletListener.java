package tipsy.app.membre.wallet;

import tipsy.commun.Event;
import tipsy.commun.commerce.Commande;
import tipsy.commun.commerce.Panier;
import tipsy.commun.commerce.Transaction;

/**
 * Created by valoo on 04/01/14.
 */
public interface WalletListener {
    public void goToResume(boolean addToBackStack);

    public void goToFormule();

    public void goToCredit();

    public void goToDetailsTransaction(Transaction t);

    public void goToCommande(boolean addToBackStack, Panier p, Commande c);

    public void goToHomeMembre();
}
