package tipsy.commun.commerce;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        Transaction.query(Transaction.class, query, StackMobOptions.depthOf(1), new StackMobQueryCallback<Transaction>() {
            @Override
            public void success(List<Transaction> result) {
                Collections.sort(result, new Comparator<Transaction>() {
                    public int compare(Transaction t1, Transaction t2) {
                        return t1.getDate().before(t2.getDate())? 1 : -1;
                    }
                });
                Iterator it = result.iterator();
                while (it.hasNext()) {
                    add((T) it.next());
                }
                callback.success();
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT","erreur" + e.getMessage());
                callback.failure(e);
            }
        });
    }

    public void credit(int montant, final WalletCallback callback) {
        callback.onWait();

        if (montant <= 0)
            callback.onFailure(new Exception("Le montant doit être positif."));
        else{
            Transaction transaction = new Transaction(montant, user.getUsername(), Commerce.Devise.getLocale());
            add(0,(T) transaction);
            transaction.save(new StackMobModelCallback() {
                @Override
                public void success() {
                    callback.onSuccess();
                }

                @Override
                public void failure(StackMobException e) {
                    remove(0);
                    callback.onFailure(e);
                }
            });
        }
    }

    public void pay(Commande cmd, String destinataire, final WalletCallback callback){
        // Mise en attente de l'utilisateur
        callback.onWait();

        final Commande commande = cmd;
        if (getSolde() < commande.getPrixTotal())
            callback.onFailure(new Exception("Fonds insuffisants."));
        else {
            // Création d'une transaction du montant de la commande destinée à l'organisateur
            final Transaction transaction = new Transaction(user.getUsername(), commande.getPrixTotal(), commande, destinataire);

            // ajout de la transaction en tête de liste
            add(0,(T) transaction);
            transaction.save(StackMobOptions.depthOf(3), new StackMobModelCallback() {
                @Override
                public void success() {
                    callback.onSuccess();
                }

                @Override
                public void failure(StackMobException e) {
                    //Suppression de la transaction précédemment ajoutée
                    remove(0);
                    callback.onFailure(e);
                }
            });
        }
    }

    public static ProgressDialog getProgressDialog(Context context){
        ProgressDialog wait = new ProgressDialog(context);
        wait.setMessage("Paiement en cours...");
        wait.setIndeterminate(true);
        return wait;
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

}
