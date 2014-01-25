package com.tipsy.lib.commerce;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.stackmob.sdk.model.StackMobModel;

import java.util.Date;

/**
 * Created by Valentin on 21/01/14.
 */
@ParseClassName("Depot")
public class Depot extends ParseObject implements Transaction {

    public Depot(){}

    public Depot(int montant, String username, int devise){
        setDate(new Date());
        setDevise(devise);
        setMontant(montant);
        setUsername(username);
    }

    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date",date);
    }

    public int getDevise() {
        return getInt("devise");
    }

    public void setDevise(int devise) {
        put("devise",devise);
    }

    public int getMontant() {
        return getInt("montant");
    }

    public void setMontant(int montant) {
        put("montant",montant);
    }

    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        put("username",username);
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
        dest.writeString(getObjectId());
        dest.writeSerializable(getDate());
        dest.writeInt(getDevise());
        dest.writeInt(getMontant());
        dest.writeString(getUsername());
    }

    public Depot(Parcel in) {
        setObjectId(in.readString());
        setDate((Date) in.readSerializable());
        setDevise(in.readInt());
        setMontant(in.readInt());
        setUsername(in.readString());
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
