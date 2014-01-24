package com.tipsy.lib.commerce;


import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by valoo on 04/01/14.
 */
public class Item implements Parcelable {
    private Produit produit;
    private int quantite;

    public Item() {
    }

    public Item(Produit a, int q) {
        produit = a;
        quantite = q;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getPrixTotal() {
        return produit.getPrix() * quantite;
    }

    @Override
    public boolean equals(Object o) {
        return this.produit.equals(((Item) o).getProduit());
    }

    @Override
    public int hashCode() {
        return produit.getObjectId().hashCode();
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(produit, flags);
        dest.writeInt(quantite);
    }

    public Item(Parcel in) {
        produit = in.readParcelable(Item.class.getClassLoader());
        quantite = in.readInt();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
