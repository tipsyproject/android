package tipsy.commun.commerce;

import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.api.StackMobQueryField;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tipsy.commun.User;
import tipsy.commun.commerce.Transaction;

/**
 * Created by valoo on 05/01/14.
 */
public class Wallet {
    private int devise = Commerce.Devise.EURO;
    private ArrayList<Transaction> transactions;
    private User user;

    public Wallet(User user){
        this.user = user;
    }

    public int getDevise() {
        return devise;
    }

    public void setDevise(int devise) {
        this.devise = devise;
    }

    /* Récupère la liste des transactions dont fait partie le user */
    public void init(WalletInitCallback cb){
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
                transactions = new ArrayList<Transaction>(result);
                callback.success();
            }

            @Override
            public void failure(StackMobException e) {
                callback.failure(e);
            }
        });
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public Transaction credit(int montant){
        Transaction t = new Transaction(montant, user);
        transactions.add(t);
        return t;
    }

    public int getSolde(){
        int solde = 0;
        Transaction t;
        Iterator it = transactions.iterator();
        while (it.hasNext()){
            t = (Transaction) it.next();
            if(t.isCredit(user)){
                solde += t.getMontant();
            }else solde -= t.getMontant();
        }
        return solde;
    }


    /*
    Callback à utiliser lors du chargement du portefeuille
     */
    public abstract class WalletInitCallback {
        public WalletInitCallback() {}

        public abstract void success();

        public abstract void failure(com.stackmob.sdk.exception.StackMobException e);
    }

}
