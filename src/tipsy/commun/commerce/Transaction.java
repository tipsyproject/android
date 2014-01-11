package tipsy.commun.commerce;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.stackmob.sdk.model.StackMobModel;

import java.util.Date;

/**
 * Created by valoo on 05/01/14.
 */
public class Transaction extends StackMobModel implements Parcelable {

    private String auteur = null;
    private Commande commande;
    private Date date;
    private String destinataire;
    private int devise;
    private int montant;

    public Transaction() {
        super(Transaction.class);
    }

    public Transaction(int montant, String destinataire, int devise) {
        super(Transaction.class);
        this.auteur = null;
        this.date = new Date();
        this.destinataire = destinataire;
        this.devise = devise;
        this.montant = montant;
    }

    public Transaction(String auteur, int montant, Commande commande) {
        super(Transaction.class);
        this.auteur = auteur;
        this.commande = commande;
        this.date = new Date();
        this.destinataire = commande.getDestinataire();
        this.devise = commande.getDevise();
        this.montant = montant;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public int getDevise() {
        return devise;
    }

    public void setDevise(int devise) {
        this.devise = devise;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    /* La transaction est un crédit pour le User u si et seulement s'il en est le destinataire */
    public boolean isCredit(String destinataire) {
        if (this.destinataire == null)
            Log.e("TOUTAFAIT", "destinataire null !!!");
        return this.destinataire.equals(destinataire);
    }

    /* La transaction est un debit pour le User u si et seulement s'il en est l'auteur */
    public boolean isDebit(String auteur) {
        return (this.auteur == null) ? false : this.auteur.equals(auteur);
    }

    public String getMontantToString() {
        return Commerce.prixToString(montant, devise);
    }

    public String getMontantToString(String auteur) {
        String signe = isDebit(auteur) ? "-" : "";
        return signe + Commerce.prixToString(montant, devise);
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeString(auteur);
        dest.writeSerializable(date);
        dest.writeString(destinataire);
        dest.writeInt(montant);
    }

    public Transaction(Parcel in) {
        super(Transaction.class);
        setID(in.readString());
        auteur = in.readString();
        date = (Date) in.readSerializable();
        destinataire = in.readString();
        montant = in.readInt();
    }

    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}
