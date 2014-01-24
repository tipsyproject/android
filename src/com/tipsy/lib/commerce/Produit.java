package com.tipsy.lib.commerce;


import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by valoo on 04/01/14.
 */


@ParseClassName("produit")
public class Produit extends ParseObject implements Parcelable {

    protected int devise = Commerce.Devise.EURO;
    protected String nom = "Mon Tarif";
    protected int prix = 0;
    protected int typeProduit;

    public static int BILLET = 0;
    public static int TICKET = 1;
    public static int CONSO = 2;

    public Produit() {}


    public int getDevise() {
        return getInt("devise");
    }

    public void setDevise(int devise) {
        put("devise",devise);
    }

    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom",nom);
    }


    public int getPrix() {
        return getInt("prix");
    }

    public void setPrix(int prix) {
        if (prix < 0) throw new ArithmeticException("Le prix d'un produit ne peut être négatif.");
        else put("prix",prix);
    }

    public int getType() {
        return getInt("type");
    }

    public void setType(int type) {
        put("type",type);
    }


    @Override
    public boolean equals(Object o) {
        return (this.getObjectId() == ((Produit) o).getObjectId());
    }



    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeInt(getDevise());
        dest.writeString(getNom());
        dest.writeInt(getPrix());
        dest.writeInt(getType());
    }

    public Produit(Parcel in) {
        setObjectId(in.readString());
        setDevise(in.readInt());
        setNom(in.readString());
        setPrix(in.readInt());
        setType(in.readInt());
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
