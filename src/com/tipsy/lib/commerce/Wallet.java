package com.tipsy.lib.commerce;

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
import java.util.List;

import com.tipsy.lib.Participant;
import com.tipsy.lib.User;

/**
 * Created by valoo on 05/01/14.
 */
public class Wallet extends ArrayList<Transaction> {
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
        for(Transaction t: this){
            if (t.isDepot())
                solde += t.getMontant();
            else solde -= t.getMontant();
        }
        return solde;
    }

    /* Récupère la liste des transactions du user (Depots + Achats) */
    public void init(WalletInitCallback cb) {
        final WalletInitCallback callback = cb;

        /* Requete Stackmob */
        Depot.query(Depot.class,
                new StackMobQuery().fieldIsEqualTo("username", user.getUsername()),
                new StackMobQueryCallback<Depot>() {

            @Override
            public void success(List<Depot> result) {
                addAll(result);
                Achat.query(Achat.class,
                        new StackMobQuery().fieldIsEqualTo("payeur", user.getEmail()),
                        StackMobOptions.depthOf(2),
                        new StackMobQueryCallback<Achat>() {

                            @Override
                            public void success(List<Achat> result) {
                                addAll(result);
                                Collections.sort(Wallet.this, new Comparator<Transaction>() {
                                    public int compare(Transaction t1, Transaction t2) {
                                        return t1.getDate().before(t2.getDate()) ? 1 : -1;
                                    }
                                });
                                callback.success();
                            }

                            @Override
                            public void failure(StackMobException e) {
                                Log.d("TOUTAFAIT", "erreur achats" + e.getMessage());
                                callback.failure(e);
                            }
                        });
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT","erreur depots" + e.getMessage());
                callback.failure(e);
            }
        });
    }

    public void credit(int montant, final WalletCallback callback) {
        callback.onWait();

        if (montant <= 0)
            callback.onFailure(new Exception("Le montant doit être positif."));
        else{
            Depot depot = new Depot(montant, user.getUsername(), Commerce.Devise.getLocale());
            add(0, depot);
            depot.save(new StackMobModelCallback() {
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

    public void pay(Commande commande, final WalletCallback callback){
        // Mise en attente de l'utilisateur
        callback.onWait();

        final Commande cmd = commande;
        if (getSolde() < cmd.getPrixTotal())
            callback.onFailure(new Exception("Fonds insuffisants."));
        else {
            // Création d'un transaction du montant de la commande destinée à l'organisateur
            cmd.setPayeur(user.getEmail());

            // ajout des achats en tête de liste
            for(Achat a: cmd)
                add(0, a);
            Achat.saveMultiple(cmd, new StackMobModelCallback() {
                @Override
                public void success() {
                    ArrayList<Participant> participants = new ArrayList<Participant>();
                    for(Achat a : cmd)
                        participants.add(a.getParticipant());
                    Participant.saveMultiple(participants, new StackMobModelCallback() {
                        @Override
                        public void success() {
                            callback.onSuccess();
                        }

                        @Override
                        public void failure(StackMobException e) {
                            //Suppression de la transaction précédemment ajoutée
                            Log.d("TOUTAFAIT", "erreur save achat multiple:" + e.getMessage());
                            for (Achat a : cmd)
                                remove(0);
                            callback.onFailure(e);
                        }
                    });
                }

                @Override
                public void failure(StackMobException e) {
                    //Suppression de la transaction précédemment ajoutée
                    Log.d("TOUTAFAIT", "erreur save achat multiple:" + e.getMessage());
                    for (Achat a : cmd)
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
