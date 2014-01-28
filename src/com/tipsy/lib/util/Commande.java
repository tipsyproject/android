package com.tipsy.lib.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.tipsy.lib.Achat;
import com.tipsy.lib.Participant;
import com.tipsy.lib.TipsyUser;

import java.util.ArrayList;

/**
 * Created by Valentin on 21/01/14.
 */
public class Commande extends ArrayList<Achat> implements Parcelable {

    public Commande() {
    }

    public Commande(Panier panier) {
        for (Item item : panier)
            for(int i=0; i<item.getQuantite(); ++i)
                add(new Achat(item.getTicket()));
    }

    public int getPrixTotal() {
        int prixTotal = 0;
        for (Achat a : this)
            prixTotal += a.getMontant();
        return prixTotal;
    }

    public int getDevise() {
        return get(0).getDevise();
    }

    public void setPayeur(TipsyUser user) {
        for (Achat a : this)
            a.setPaiementUser(user);
    }

    public void setPayeur(Participant user) {
        for (Achat a : this)
            a.setPaiementParticipant(user);
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
        in.readList(achats, Achat.class.getClassLoader());
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
