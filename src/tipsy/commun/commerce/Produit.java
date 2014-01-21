package tipsy.commun.commerce;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by valoo on 04/01/14.
 */


public class Produit extends StackMobModel  implements Parcelable {

    protected int devise = Commerce.Devise.EURO;
    protected String nom = "Mon Tarif";
    protected int prix = 0;
    protected int typeProduit;

    public static int BILLET = 0;
    public static int TICKET = 1;
    public static int CONSO = 2;

    public Produit() {
        super(Produit.class);
    }


    public int getDevise() {
        return devise;
    }

    public void setDevise(int devise) {
        this.devise = devise;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        if (prix < 0) throw new ArithmeticException("Le prix d'un produit ne peut être négatif.");
        else this.prix = prix;
    }

    public int getTypeProduit() {
        return typeProduit;
    }

    public void setTypeProduit(int typeproduit) {
        this.typeProduit = typeproduit;
    }


    @Override
    public boolean equals(Object o) {
        return (this.getID() == ((Produit) o).getID());
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeInt(devise);
        dest.writeString(nom);
        dest.writeInt(prix);
        dest.writeInt(typeProduit);
    }

    public Produit(Parcel in) {
        super(Produit.class);
        setID(in.readString());
        devise = in.readInt();
        nom = in.readString();
        prix = in.readInt();
        typeProduit = in.readInt();
    }

    public static final Parcelable.Creator<Produit> CREATOR = new Parcelable.Creator<Produit>() {
        @Override
        public Produit createFromParcel(Parcel source) {
            return new Produit(source);
        }

        @Override
        public Produit[] newArray(int size) {
            return new Produit[size];
        }
    };
}
