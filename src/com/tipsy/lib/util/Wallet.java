package com.tipsy.lib.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Depot;
import com.tipsy.lib.Participant;
import com.tipsy.lib.TipsyUser;

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
    private Participant participant;

    public Wallet(TipsyUser user) {
        this.user = user;
    }

    public Wallet(Participant participant) {
        this.participant = participant;
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
    public void load(final QueryCallback callback) {
        final List<Transaction> transactions = new ArrayList<Transaction>();
        /* LISTE DES DEPOTS DU USER */
        ParseQuery<Depot> queryD = ParseQuery.getQuery(Depot.class);
        if (isTipsyUser())
            queryD.whereEqualTo("user", user);
        else
            queryD.whereEqualTo("participant", participant);

        queryD.findInBackground(new FindCallback<Depot>() {
            @Override
            public void done(List<Depot> depots, ParseException e) {
                if (e == null) {
                    transactions.addAll(depots);
                    /* LISTE DES ACHATS USER */
                    ParseQuery<Achat> queryA = ParseQuery.getQuery(Achat.class);
                    queryA.include("ticket.event");
                    if (isTipsyUser())
                        queryA.whereEqualTo("paiementuser", user);
                    else
                        queryA.whereEqualTo("paiementparticipant", participant);
                    queryA.findInBackground(new FindCallback<Achat>() {
                        @Override
                        public void done(List<Achat> achats, ParseException e) {
                            if (e == null) {
                                transactions.addAll(achats);

                                    /* TRI DE LA TRANSACTION LA PLUS RECENTE A LA MOINS RECENTE */
                                Collections.sort(transactions, new Comparator<Transaction>() {
                                    public int compare(Transaction t1, Transaction t2) {
                                        return t1.getCreatedAt().before(t2.getCreatedAt()) ? 1 : -1;
                                    }
                                });
                                    /* On ne reinitialise qu'une fois qu'on est sûr que tout s'est bien déroulé */
                                clear();
                                addAll(transactions);
                                callback.done(null);
                            } else callback.done(e);
                        }
                    });
                } else callback.done(e);

            }
        });
    }

    public void credit(int montant, final QueryCallback callback) {

        if (montant <= 0)
            callback.done(new Exception("Le montant doit être positif."));
        else {
            final Depot depot = new Depot(montant, user, Commerce.Devise.getLocale());
            depot.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        add(0, depot);
                        callback.done(null);
                    } else
                        callback.done(e);
                }
            });
        }
    }

    public void pay(final Commande cmd, final QueryCallback callback) {
        // Mise en attente de l'utilisateur
        if (getSolde() < cmd.getPrixTotal())
            callback.done(new Exception("Fonds insuffisants."));
        else {
            cmd.setPayeur(user);

            ArrayList<ParseObject> achats = new ArrayList<ParseObject>();
            for (Achat a : cmd)
                achats.add(a);

            Achat.saveAllInBackground(achats, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        addAll(0, cmd);
                        callback.done(null);
                    } else callback.done(e);
                }
            });

        }
    }

    public boolean isTipsyUser() {
        return user != null;
    }

    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog wait = new ProgressDialog(context);
        wait.setMessage("Paiement en cours...");
        wait.setIndeterminate(true);
        return wait;
    }

}
