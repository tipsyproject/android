package com.tipsy.lib.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by valoo on 04/01/14.
 */
public class Panier extends ArrayList<Item> implements Parcelable {

    protected int devise = Commerce.Devise.getLocale();

    public Panier() {
    }

    public Panier(ArrayList<Item> items) {
        for (Item item : items)
            add(item);
    }

    public int getDevise() {
        return devise;
    }

    public void setDevise(int devise) {
        this.devise = devise;
    }

    public int getPrixTotal() {
        int prixTotal = 0;
        for (Item item : this)
            prixTotal += item.getPrixTotal();
        return prixTotal;
    }

    @Override
    public boolean isEmpty() {
        for (Item item : this)
            if (item.getQuantite() > 0)
                return false;
        return true;
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(devise);
        dest.writeList(this);
    }

    public Panier(Parcel in) {
        devise = in.readInt();
        in.readList(this, Item.class.getClassLoader());
    }

    public static final Parcelable.Creator<Panier> CREATOR = new Parcelable.Creator<Panier>() {
        @Override
        public Panier createFromParcel(Parcel source) {
            return new Panier(source);
        }

        @Override
        public Panier[] newArray(int size) {
            return new Panier[size];
        }
    };
}
