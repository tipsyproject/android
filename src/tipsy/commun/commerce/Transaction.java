package tipsy.commun.commerce;

import android.util.Log;

import com.stackmob.sdk.model.StackMobModel;

import java.util.Date;

import tipsy.commun.User;

/**
 * Created by valoo on 05/01/14.
 */
public class Transaction extends StackMobModel{

    private User    auteur;
    private Date    date;
    private User    destinataire;
    private int     devise;
    private int     montant;

    public Transaction(){
        super(Transaction.class);
    }

    public Transaction(int montant, User destinataire){
        super(Transaction.class);
        this.auteur = null;
        this.date = new Date();
        this.destinataire = destinataire;
        this.montant = montant;
    }

    public Transaction(int montant, User destinataire, User auteur){
        super(Transaction.class);
        this.auteur = auteur;
        this.date = new Date();
        this.destinataire = destinataire;
        this.montant = montant;
    }

    public User getAuteur() {
        return auteur;
    }

    public void setAuteur(User auteur) {
        this.auteur = auteur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(User destinataire) {
        this.destinataire = destinataire;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    /* La transaction est un crédit pour le User u si et seulement s'il en est le destinataire */
    public boolean isCredit(User u){
        return this.destinataire.getUsername().equals(u.getUsername());
    }
    /* La transaction est un debit pour le User u si et seulement s'il en est l'auteur */
    public boolean isDebit(User u){
        return this.auteur.getUsername().equals(u.getUsername());
    }

    public String getMontantToString(){
        return Commerce.prixToString(montant,devise);
    }
}
