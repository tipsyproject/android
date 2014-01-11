package tipsy.commun.commerce;

import android.util.Log;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tipsy.commun.User;

/**
 * Created by valoo on 05/01/14.
 */
public class Wallet<T> extends ArrayList<T> {
    private int devise = Commerce.Devise.EURO;
    private User user;

    public Wallet(User user) {
        this.user = user;
    }

    public int getDevise() {
        return devise;
    }

    public void setDevise(int devise) {
        this.devise = devise;
    }

    /* Récupère la liste des transactions dont fait partie le user */
    public void init(WalletInitCallback cb) {
        /*
        Transactions comportant le user dans les champs destinataire et auteur
        de la plus récente à la plus ancienne
         */
        final WalletInitCallback callback = cb;
        StackMobQuery query =
                new StackMobQuery().fieldIsEqualTo("destinataire", user.getUsername())
                        .or(
                                new StackMobQuery().fieldIsEqualTo("auteur", user.getUsername())
                        );

        /* Requete Stackmob */
        Transaction.query(Transaction.class, query, new StackMobQueryCallback<Transaction>() {
            @Override
            public void success(List<Transaction> result) {
                Iterator it = result.iterator();
                while (it.hasNext()) {
                    add((T) it.next());
                }
                callback.success();
            }

            @Override
            public void failure(StackMobException e) {
                callback.failure(e);
            }
        });
    }

    public Transaction credit(int montant) throws ArithmeticException{
        if(montant <= 0)
            throw new ArithmeticException("Le montant doit être positif.");
        Transaction t = new Transaction(montant, user.getUsername(), Commerce.Devise.getLocale());
        add((T) t);
        return t;
    }

    public int getSolde() {
        int solde = 0;
        Transaction t;
        Iterator it = iterator();
        while (it.hasNext()) {
            t = (Transaction) it.next();
            if (t.isCredit(user.getUsername())) {
                solde += t.getMontant();
            } else solde -= t.getMontant();
        }
        return solde;
    }

    public void pay(Commande cmd) throws WalletInsufficientFundsException{
        final Commande commande = cmd;
        if(getSolde() < commande.getPrixTotal())
            throw new WalletInsufficientFundsException();
        else{
            // Création d'une transaction du montant de la commande destinée à l'organisateur
            final Transaction transaction = new Transaction(user.getUsername(),commande.getPrixTotal(),commande);

            add((T) transaction);
            transaction.save(StackMobOptions.depthOf(3),new StackMobModelCallback() {
                @Override
                public void success() {
                    Log.d("TOUTAFAIT", "save commande ok");
                    /*commande.getTransactions().add(transaction);
                    commande.save(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            Log.d("TOUTAFAIT", "save commande ok");
                        }

                        @Override
                        public void failure(StackMobException e) {
                            Log.d("TOUTAFAIT", "erreur save commande: "+e.getMessage());
                        }
                    });*/
                }

                @Override
                public void failure(StackMobException e) {
                    Log.d("TOUTAFAIT", "erreur save transaction: " + e.getMessage());
                }
            });
        }
    }


    /*
    Callback à utiliser lors du chargement du portefeuille
     */
    public abstract class WalletInitCallback {
        public WalletInitCallback() {
        }

        public abstract void success();

        public abstract void failure(com.stackmob.sdk.exception.StackMobException e);
    }

    public static class WalletInsufficientFundsException extends Exception{
        public WalletInsufficientFundsException(){
            super("Fonds insuffisants.");
        }
    }

}
