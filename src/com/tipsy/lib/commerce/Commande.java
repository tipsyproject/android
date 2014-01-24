package com.tipsy.lib.commerce;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Valentin on 21/01/14.
 */
public class Commande extends ArrayList<Achat> implements Parcelable {

    public Commande(){}

    public Commande(ArrayList<Achat> achats){
        clear();
        for(Achat a: achats)
            add(a);
    }

    public int getPrixTotal(){
        int prixTotal = 0;
        for(Achat a: this)
            prixTotal += a.getMontant();
        return prixTotal;
    }

    public int getDevise(){
        return get(0).getDevise();
    }

    public void setPayeur(String username){
        for(Achat a: this)
            a.setPayeur(username);
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this);
    }

    public Commande(Parcel in) {
        ArrayList<Achat> achats = new ArrayList<Achat>();
        in.readList(achats,Achat.class.getClassLoader());
        addAll(achats);
    }

    public static final Parcelable.Creator<Commande> CREATOR = new Parcelable.Creator<Commande>() {
        @Override
        public Commande createFromParcel(Parcel source) {
            return new Commande(source);
        }

        @Override
        public Commande[] newArray(int size) {
            return new Commande[size];
        }
    };
}
