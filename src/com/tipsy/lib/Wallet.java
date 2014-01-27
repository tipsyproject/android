package com.tipsy.lib;

import android.app.ProgressDialog;
import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by valoo on 05/01/14.
 */
public class Wallet extends ArrayList<Transaction> {
    private int devise = Commerce.Devise.EURO;
    private TipsyUser user;

    public Wallet(TipsyUser user) {
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
        for (Transaction t : this) {
            if (t.isDepot())
                solde += t.getMontant();
            else solde -= t.getMontant();
        }
        return solde;
    }

    /* Récupère la liste des transactions du user (Depots + Achats) */
    public void load() {
        try {
            List<Transaction> transactions = new ArrayList<Transaction>();
            /* LISTE DES DEPOTS DU USER */
            ParseQuery<Depot> queryD = ParseQuery.getQuery(Depot.class);
            queryD.whereEqualTo("username", user.getObjectId());
            List<Depot> depots = queryD.find();
            transactions.addAll(depots);

            /* LISTE DES ACHATS USER */
            ParseQuery<Achat> queryA = ParseQuery.getQuery(Achat.class);
            queryA.include("produit");
            queryA.whereEqualTo("payeur", user.getObjectId());
            List<Achat> achats = queryA.find();
            transactions.addAll(achats);
            /* TRI DE LA TRANSACTION LA PLUS RECENTE A LA MOINS RECENTE */
            Collections.sort(transactions, new Comparator<Transaction>() {
                public int compare(Transaction t1, Transaction t2) {
                    return t1.getDate().before(t2.getDate()) ? 1 : -1;
                }
            });
            /* On ne reinitialise qu'une fois qu'on est sûr que tout s'est bien déroulé */
            clear();
            addAll(transactions);
        } catch (ParseException e) {

        }
    }

    public void credit(int montant, final WalletCallback callback) {
        callback.onWait();

        if (montant <= 0)
            callback.onFailure(new Exception("Le montant doit être positif."));
        else {
            final Depot depot = new Depot(montant, user.getUsername(), Commerce.Devise.getLocale());
            depot.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        add(0, depot);
                        callback.onSuccess();
                    } else
                        callback.onFailure(e);
                }
            });
        }
    }

    public void pay(final Commande cmd, final WalletCallback callback) {
        // Mise en attente de l'utilisateur
        callback.onWait();
        if (getSolde() < cmd.getPrixTotal())
            callback.onFailure(new Exception("Fonds insuffisants."));
        else {
            cmd.setPayeur(user);

            ArrayList<ParseObject> achats = new ArrayList<ParseObject>();
            for (Achat a : cmd)
                achats.add(a);

            try {
                Achat.saveAll(achats);
                addAll(0, cmd);
                callback.onSuccess();
            } catch (ParseException e) {
                callback.onFailure(e);

            }

        }
    }

    public static ProgressDialog getProgressDialog(Context context) {
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

        public abstract void failure(ParseException e);
    }

}
