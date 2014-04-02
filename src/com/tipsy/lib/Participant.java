package com.tipsy.lib;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.tipsy.lib.util.Bracelet;

import java.util.Comparator;

/**
 * Created by valoo on 27/01/14.
 */
@ParseClassName("Participant")
public class Participant extends ParseObject implements Parcelable {

    public Participant() {
    }

    public String getBracelet() {
        return getString("bracelet");
    }

    public void setBracelet(Bracelet bracelet) {
        put("bracelet", bracelet.getTag());
    }

    public void setBracelet(String bracelet) {
        put("bracelet", bracelet);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        if(email != null)
            put("email", email);
    }

    public String getNom() {
        return getString("nom").toUpperCase();
    }

    public void setNom(String nom) {
        if(nom != null)
            put("nom", nom);
    }

    public String getPrenom() {
        if(getString("prenom").isEmpty())
            return "";
        else
            return Character.toUpperCase(getString("prenom").charAt(0)) + getString("prenom").substring(1).toLowerCase();
    }

    public void setPrenom(String prenom) {
        if(prenom != null)
            put("prenom", prenom);
    }

    public boolean isAnonymous(){
        return (getPrenom().equals("") && getNom().equals(""));
    }

    public String getFullName(){
        if(isAnonymous()){
            return "Xxxx XXXX";
        }else{
            return getPrenom() + " " + getNom();
        }
    }

    public boolean hasBracelet(){
        return getBracelet() != null && getBracelet() != "";
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeString(getEmail());
        dest.writeString(getNom());
        dest.writeString(getPrenom());
        dest.writeString(getBracelet());
    }

    public Participant(Parcel in) {
        setObjectId(in.readString());
        setEmail(in.readString());
        setNom(in.readString());
        setPrenom(in.readString());
        try {
            setBracelet(in.readString());
        } catch (Exception e) {
        }
    }

    public static final Parcelable.Creator<Participant> CREATOR = new Parcelable.Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel source) {
            return new Participant(source);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };
}
