package com.tipsy.lib.commerce;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

import java.util.Date;

/**
 * Created by Valentin on 21/01/14.
 */
public class Depot extends StackMobModel implements Transaction {

    private Date date;
    private int devise;
    private int montant;
    private String username;

    public Depot(){
        super(Depot.class);
    }

    public Depot(int montant, String username, int devise){
        super(Depot.class);
        this.date = new Date();
        this.devise = devise;
        this.montant = montant;
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription(){
        return "";
    }

    public String getTitre(){
        return "Rechargement";
    }

    public boolean isDepot(){
        return true;
    }



    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeSerializable(date);
        dest.writeInt(devise);
        dest.writeInt(montant);
        dest.writeString(username);
    }

    public Depot(Parcel in) {
        super(Produit.class);
        setID(in.readString());
        date = (Date) in.readSerializable();
        devise = in.readInt();
        montant = in.readInt();
        username = in.readString();
    }

    public static final Parcelable.Creator<Depot> CREATOR = new Parcelable.Creator<Depot>() {
        @Override
        public Depot createFromParcel(Parcel source) {
            return new Depot(source);
        }

        @Override
        public Depot[] newArray(int size) {
            return new Depot[size];
        }
    };
}
