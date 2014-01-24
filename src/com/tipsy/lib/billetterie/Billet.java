package com.tipsy.lib.billetterie;

import android.os.Parcel;
import android.os.Parcelable;

import com.tipsy.lib.commerce.Produit;

/**
 * Created by valoo on 27/12/13.
 */
public class Billet extends Produit {

    public Billet() {
        super();
        this.setTypeProduit(Produit.BILLET);
    }

    // Nom du schema dans Stackmob
    public static String overrideSchemaName() {
        return "produit";
    }


    /* Implementation Parcelable */

    public Billet(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Billet> CREATOR = new Parcelable.Creator<Billet>() {
        @Override
        public Billet createFromParcel(Parcel source) {
            return new Billet(source);
        }

        @Override
        public Billet[] newArray(int size) {
            return new Billet[size];
        }
    };



}
