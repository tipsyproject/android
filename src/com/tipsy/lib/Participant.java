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
        if(email == null)
            email = "";
        put("email", email);
    }

    public String getNom() {
        String nom;
        if(getString("nom").isEmpty())
            nom = "";
        else {
            nom = Character.toUpperCase(getString("nom").charAt(0)) + "";
            if(getString("nom").length() > 1)
                nom += getString("nom").substring(1).toLowerCase();
        }
        return nom;
    }

    public void setNom(String nom) {
        if(nom == null)
            nom = "";
        put("nom", nom);
    }

    public String getPrenom() {
        String prenom;
        if(getString("prenom").isEmpty())
            prenom = "";
        else {
            prenom = Character.toUpperCase(getString("prenom").charAt(0)) + "";
            if(getString("prenom").length() > 1)
                prenom += getString("prenom").substring(1).toLowerCase();
        }
        return prenom;
    }

    public void setPrenom(String prenom) {
        if(prenom == null)
            prenom = "";
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


    /* Tri par ordre alphabetique des prénoms */
    public static Comparator<Participant> SORT_BY_FULLNAME = new Comparator<Participant>() {
        public int compare(Participant one, Participant other) {
            return one.getFullName().compareTo(other.getFullName());
        }
    };

    @Override
    public boolean equals(Object o) {
        return (this.getObjectId() == ((Participant) o).getObjectId());
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
